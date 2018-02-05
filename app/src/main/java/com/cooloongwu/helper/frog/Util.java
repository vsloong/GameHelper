package com.cooloongwu.helper.frog;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;

/**
 * Created by CooLoongWu on 2018-2-1 14:20.
 */

public class Util {
    public static boolean writeInt(File archive, int offset, int value) {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(archive, "rwd");
            //默认情况下ras的指针为0，即从第1个字节读写到
            //ras.seek(1);//表示将ras的指针设置到8，则读写ras是从第9个字节读写到

            r.seek(offset);
            r.writeInt(value);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(r);
        }
        return false;
    }

    public static int readInt(File archive, int offset) {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(archive, "r");
            r.seek(offset);
            return r.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(r);
        }
        return -1;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    @NonNull
    public static byte[] fileToByteArray(File file, long position) {
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) (fc.size() - position));
            fc.read(byteBuffer, position);
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(fc);
        }
        return new byte[0];
    }

    public static Calendar readCalendar(File archive, int offset) {
        RandomAccessFile r = null;
        Calendar calendar = Calendar.getInstance();
        try {
            r = new RandomAccessFile(archive, "r");
            r.seek(offset);
            calendar.set(r.readInt(), r.readInt() - 1, r.readInt(), r.readInt(), r.readInt(), r.readInt());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(r);
        }
        return calendar;
    }

    public static boolean writeCalendar(File archive, int offset, Calendar value) {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(archive, "rwd");
            r.seek(offset);
            r.writeInt(value.get(Calendar.YEAR));
            r.writeInt(value.get(Calendar.MONTH) + 1);
            r.writeInt(value.get(Calendar.DAY_OF_MONTH));
            r.writeInt(value.get(Calendar.HOUR_OF_DAY));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(r);
        }
        return false;
    }
}
