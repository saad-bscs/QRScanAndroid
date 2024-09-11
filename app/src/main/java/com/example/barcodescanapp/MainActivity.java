package com.example.barcodescanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

//Google code scanner // permission less
//In Java
public class MainActivity extends AppCompatActivity {

    Button scanQrBtn;
    TextView scannedValueTv;
    boolean isScannerInstalled = false;
    GmsBarcodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        installGoogleScanner();
        registerUiListener();
    }

    private void registerUiListener() {
        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScannerInstalled) {
                    startScanning();
                } else {
                    Toast.makeText(MainActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startScanning() {

        scanner.startScan().addOnSuccessListener(barcode -> {
            String result = barcode.getRawValue();
            if (result != null) {
                String scannedValue = "Scanned Value:" + result;
                scannedValueTv.setText(scannedValue);
            }

        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }).addOnCanceledListener(() -> Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show());
    }

    private void initVars() {

        scanQrBtn = findViewById(R.id.scanQrBtn);
        scannedValueTv = findViewById(R.id.scannedValueTv);

        GmsBarcodeScannerOptions options = initializeGoogleScanner();
        scanner = GmsBarcodeScanning.getClient(this, options);

    }

    private GmsBarcodeScannerOptions initializeGoogleScanner() {

        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .enableAutoZoom().build();
    }

    private void installGoogleScanner() {

        ModuleInstallClient moduleInstall = ModuleInstall.getClient(this);
        ModuleInstallRequest moduleInstallRequest = ModuleInstallRequest
                .newBuilder()
                .addApi(GmsBarcodeScanning.getClient(this)).build();

        moduleInstall.installModules(moduleInstallRequest).addOnSuccessListener(moduleInstallResponse -> {
            isScannerInstalled = true;

        }).addOnFailureListener(e -> {
            isScannerInstalled = false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}