package com.cooloongwu.helper.frog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cooloongwu.helper.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

public class FrogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_FILE_PICKER = 0x1233;
    private static final int REQUEST_CODE_REQUEST_PERMISSIONS = 0x1784;

    private static final int OFFSET_CLOVER = 0x16;
    private static final int OFFSET_TICKETS = 0x1a;
    private static final int OFFSET_DATETIME = 0x049a;

    private static final int WHAT_WRITE_CALENDAR = 0x334;

    private EditText cloverInput;
    private EditText ticketsInput;
    private EditText dateInput;
    private Button cloverButton;
    private Button ticketsButton;

    private File dataDir;
    private File archive;

    private Calendar calendar;
    private AlbumsExporter exporter;
    private final Context context = this;
    private final Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frog);
        File cacheDir = getExternalCacheDir();
        if (cacheDir == null) {
            show("shared storage is not currently available.");
            throw new RuntimeException("shared storage is not currently available.");
        }
        dataDir = cacheDir.getParentFile().getParentFile();
        archive = new File(dataDir, "jp.co.hit_point.tabikaeru/files/Tabikaeru.sav");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyStoragePermissions(this);
    }

    private void pickArchive() {
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE_FILE_PICKER)
                .withTitle(getString(R.string.archive_pick))
                .withBackgroundColor("#3F51B5")
                .withFileFilter(new String[]{"sav"})
                .withMutilyMode(false)
                .withChooseMode(true)
                .withStartPath(archive.exists()
                        ? archive.getParentFile().getAbsolutePath() : dataDir.getAbsolutePath())
                .start();
    }

    private void initView() {
        cloverInput = findViewById(R.id.et_clover);
        ticketsInput = findViewById(R.id.et_tickets);
        dateInput = findViewById(R.id.et_date);
        cloverButton = findViewById(R.id.save_clover);
        ticketsButton = findViewById(R.id.save_tickets);

        Button advance_date = findViewById(R.id.advance_date);
        advance_date.setOnClickListener(this);
        cloverButton.setOnClickListener(this);
        ticketsButton.setOnClickListener(this);
        cloverInput.addTextChangedListener(new MyTextWatcher(cloverButton));
        ticketsInput.addTextChangedListener(new MyTextWatcher(ticketsButton));
    }

    private void initData() {
        if (archive.exists()) {
            if (archive.canWrite()) {
                String cloverData = getString(R.string.number, Util.readInt(archive, OFFSET_CLOVER));
                String ticketsData = getString(R.string.number, Util.readInt(archive, OFFSET_TICKETS));
                calendar = Util.readCalendar(archive, OFFSET_DATETIME);
                cloverButton.setTag(cloverData);
                ticketsButton.setTag(ticketsData);
                cloverInput.setText(cloverData);
                ticketsInput.setText(ticketsData);
                dateInput.setText(getString(R.string.calendar, calendar));
                if (exporter == null) {
                    exporter = initAlbumsExporter();
                } else {
                    exporter.refresh();
                }
            } else {
                show(getString(R.string.archive_permission_denied));
            }
        } else {
            pickArchive();
        }
    }

    private AlbumsExporter initAlbumsExporter() {
        File picture = new File(archive.getParentFile(), "Picture");
        File filesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Tabikaeru");

        return new AlbumsExporter(picture, filesDir).setProgressListener(
                new AlbumsExporter.ProgressListener() {
                    View view = View.inflate(FrogActivity.this, R.layout.progress, null);
                    ProgressBar progressBar = view.findViewById(R.id.progress_bar);
                    TextView tips = view.findViewById(R.id.progress_tips);
                    AlertDialog dialog = new Builder(FrogActivity.this)
                            .setTitle(R.string.export_albums)
                            .setView(view)
                            .setCancelable(false)
                            .create();

                    @Override
                    public void onBefore(int count) {
                        progressBar.setMax(count);
                        dialog.show();
                    }

                    @Override
                    public void inProgress(String filename, int count, int progress) {
                        progressBar.setProgress(progress);
                        tips.setText(getString(R.string.progress_tips, filename, progress, count));
                    }

                    @Override
                    public void onAfter(String path, int count) {
                        dialog.dismiss();
                        show(getString(R.string.export_albums_msg, count));
                    }

                    @Override
                    public void isEmpty() {
                        show(getString(R.string.no_albums_export));
                    }
                });
    }

    private void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"
                }, REQUEST_CODE_REQUEST_PERMISSIONS);
            } else {
                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_REQUEST_PERMISSIONS) {
            initData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILE_PICKER && data != null) {
            ArrayList<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
            archive = new File(list.get(0));
            initData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_archive_pick:
                pickArchive();
                return true;
            case R.id.action_export_albums:
                if (exporter != null) {
                    exporter.export();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_clover:
                writeInt(v, cloverInput, OFFSET_CLOVER);
                break;
            case R.id.save_tickets:
                writeInt(v, ticketsInput, OFFSET_TICKETS);
                break;
            case R.id.advance_date:
                calendar.add(Calendar.HOUR_OF_DAY, -3);
                handler.removeMessages(WHAT_WRITE_CALENDAR);
                handler.sendEmptyMessageDelayed(WHAT_WRITE_CALENDAR, 500);
                break;
        }
    }

    private void writeInt(View view, EditText editText, int offset) {
        try {
            String s = editText.getText().toString();
            boolean ret = Util.writeInt(archive, offset, Integer.parseInt(s));
            view.setTag(s);
            view.setEnabled(!ret);
            if (ret) {
                show(getString(R.string.success_msg));
            } else {
                show(getString(R.string.failure_msg));
            }
        } catch (NumberFormatException e) {
            show(getString(R.string.number_err_msg));
        }
    }

    private void writeCalendar() {
        if (Util.writeCalendar(archive, OFFSET_DATETIME, calendar)) {
            dateInput.setText(getString(R.string.calendar, calendar));
            show(getString(R.string.success_msg));
        } else {
            show(getString(R.string.failure_msg));
        }
    }

    private static class MyTextWatcher implements TextWatcher {
        private Button button;

        MyTextWatcher(Button button) {
            this.button = button;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            button.setEnabled(s.length() > 0 && !s.toString().equals(button.getTag()));
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<FrogActivity> reference;

        private MyHandler(FrogActivity reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            FrogActivity activity = reference.get();
            if (null == activity) {
                return;
            }
            switch (msg.what) {
                case WHAT_WRITE_CALENDAR:
                    activity.writeCalendar();
                    break;
            }
        }
    }

    private void show(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
