package com.cooloongwu.helper.frog;


import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by CooLoongWu on 2018-2-2 09:54.
 */

public class AlbumsExporter {

    private File pictureDir;
    private File outputDir;

    private List<File> albums;
    private ProgressListener progressListener;
    private final Handler mainHandler;

    public AlbumsExporter(File pictureDir, File outputDir) {
        this.pictureDir = pictureDir;
        this.outputDir = outputDir;
        if (!this.outputDir.exists()) {
            this.outputDir.mkdirs();
        }
        this.albums = new ArrayList<>(getAlbums());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public AlbumsExporter setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public void refresh() {
        albums.addAll(getAlbums());
    }

    public void export() {
        if (albums.isEmpty()) {
            if (progressListener != null) {
                progressListener.isEmpty();
            }
        } else {
            if (progressListener != null) {
                progressListener.onBefore(albums.size());
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int count = exportAlbums();
                    if (progressListener != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressListener.onAfter(outputDir.getAbsolutePath(), count);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private int exportAlbums() {
        int i = 0, count = 0;
        final int len = albums.size();
        Iterator<File> it = albums.iterator();
        while (it.hasNext()) {
            File album = it.next();
            final File out = new File(outputDir, album.getName().replace(".sav", ".png"));
            final int progress = ++i;
            if (progressListener != null) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressListener.inProgress(out.getName(), len, progress);
                    }
                });
            }
            byte[] bytes = Util.fileToByteArray(album, 4);
            if (bytes.length > 0) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(out));
                    bitmap.recycle();
                    it.remove();
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    private List<File> getAlbums() {
        return Arrays.asList(pictureDir.listFiles(new MyFilenameFilter(outputDir)));
    }

    private static class MyFilenameFilter implements FilenameFilter {
        private Set<String> outFileNames;

        public MyFilenameFilter(File outputDir) {
            String[] names = outputDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".png") && name.startsWith("album_");
                }
            });
            outFileNames = new HashSet<>(names.length);
            for (String name : names) {
                outFileNames.add(name.replace(".png", ".sav"));
            }
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".sav") && name.startsWith("album_") && !outFileNames.contains(name);
        }
    }

    public interface ProgressListener {
        void onBefore(int count);

        void inProgress(String filename, int count, int progress);

        void onAfter(String path, int count);

        void isEmpty();
    }
}
