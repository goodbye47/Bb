// MainActivity.java
package com.example.melodytopopsong;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1000;
    private MediaRecorder mediaRecorder;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startRecordingButton = findViewById(R.id.start_recording);
        Button stopRecordingButton = findViewById(R.id.stop_recording);
        Button generateMusicButton = findViewById(R.id.generate_music);

        if (!checkPermissionFromDevice()) {
            requestPermission();
        }

        startRecordingButton.setOnClickListener(v -> {
            if (checkPermissionFromDevice()) {
                fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio.wav";
                setupMediaRecorder();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startRecordingButton.setEnabled(false);
                stopRecordingButton.setEnabled(true);
            } else {
                requestPermission();
            }
        });

        stopRecordingButton.setOnClickListener(v -> {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }

            startRecordingButton.setEnabled(true);
            stopRecordingButton.setEnabled(false);
            generateMusicButton.setEnabled(true);
        });

        generateMusicButton.setOnClickListener(v -> {
            // Implement music generation logic here
        });
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}