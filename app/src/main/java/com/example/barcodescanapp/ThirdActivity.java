package com.example.barcodescanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcodescanapp.R;
import com.example.barcodescanapp.viewModel.ScannerViewModel;

//Google code scanner // permission less // with viewModel class
//In Java
public class ThirdActivity extends AppCompatActivity {

    Button scanQrBtn;
    TextView scannedValueTv;
    ScannerViewModel scannerViewModel;

    boolean isScannerInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        
        scanQrBtn = findViewById(R.id.scanQrBtn);
        scannedValueTv = findViewById(R.id.scannedValueTv);

        // Initialize ViewModel
        scannerViewModel = new ViewModelProvider(ThirdActivity.this).get(ScannerViewModel.class);

        initObservers();

        scannerViewModel.initializeScannerObjects(this);

        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScannerInstalled) {
                    scannerViewModel.startScanning();
                } else {
                    Toast.makeText(ThirdActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initObservers() {
        scannerViewModel.getIsScannerInstalled().observe(this, isInstalled -> {
            isScannerInstalled = isInstalled;
        });

        scannerViewModel.getIsSuccess().observe(this, scannedValue -> {
            scannedValueTv.setText(scannedValue);
        });

        scannerViewModel.getIsFailure().observe(this, failureValue -> {
            scannedValueTv.setText(failureValue);
        });

        scannerViewModel.getIsCancelled().observe(this, cancelledValue -> {
            scannedValueTv.setText(cancelledValue);
        });
    }
}