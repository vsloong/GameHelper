package com.cooloongwu.helper.frog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cooloongwu.helper.R;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.File;
import java.util.ArrayList;

public class FrogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_FILE_PICKER = 0x1233;
    private static final int REQUEST_CODE_REQUEST_PERMISSIONS = 0x1784;

    private EditText editClover;
    private EditText editTickets;

    private File dataDir;
    private File archive;

    private static final int OFFSET_CLOVER = 0x16;
    private static final int OFFSET_TICKETS = 0x1a;
    private static final int OFFSET_DATETIME = 0x049a;

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
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyStoragePermissions(this);
    }

    private void initViews() {
        Button btnClover = findViewById(R.id.btn_clover);
        Button btnTickets = findViewById(R.id.btn_tickets);

        editClover = findViewById(R.id.edit_clover);
        editTickets = findViewById(R.id.edit_tickets);


        btnClover.setOnClickListener(this);
        btnTickets.setOnClickListener(this);
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
                //initData();
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
            //initData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILE_PICKER && data != null) {
            ArrayList<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
            archive = new File(list.get(0));
            //initData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clover:

                String cloverNum = editClover.getText().toString().trim();
                if (cloverNum.isEmpty()) {
                    show(R.string.number_err_msg);
                } else {
                    writeInt(view, Integer.parseInt(cloverNum), OFFSET_CLOVER);
                }

                break;
            case R.id.btn_tickets:

                String ticketsNum = editTickets.getText().toString().trim();
                if (ticketsNum.isEmpty()) {
                    show(R.string.number_err_msg);
                } else {
                    writeInt(view, Integer.parseInt(ticketsNum), OFFSET_TICKETS);
                }
                break;
            default:
                break;
        }
    }

    private void writeInt(View view, int num, int offset) {
        try {
            boolean ret = Util.writeInt(archive, offset, num);
            view.setTag(num);
            view.setEnabled(!ret);
            if (ret) {
                show(R.string.success_msg);
            } else {
                show(R.string.failure_msg);
            }
        } catch (NumberFormatException e) {
            show(R.string.number_err_msg);

        }
    }

    private void show(int str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void show(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
