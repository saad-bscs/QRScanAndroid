package com.example.barcodescanapp.viewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.barcodescanapp.MainActivity;
import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.List;

public class ScannerViewModel extends ViewModel {

    private final MutableLiveData<String> successLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> failureLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> cancelLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> IsScannerInstalledData = new MutableLiveData<>();
    boolean isScannerInstalled = false;
    GmsBarcodeScanner scanner;

    public LiveData<Boolean> getIsScannerInstalled() {
        return IsScannerInstalledData;
    }

    public LiveData<String> getIsSuccess() {
        return successLiveData;
    }

    public LiveData<String> getIsFailure() {
        return failureLiveData;
    }

    public LiveData<String> getIsCancelled() {
        return cancelLiveData;
    }

    public void initializeScannerObjects(Context context) {
        initializeGoogleScanner(context);
        installGoogleScanner(context);
    }

    private void initializeGoogleScanner(Context context) {
        GmsBarcodeScannerOptions options = initializeGoogleScanner();
        scanner = GmsBarcodeScanning.getClient(context, options);
    }

    private GmsBarcodeScannerOptions initializeGoogleScanner() {

        return new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .enableAutoZoom().build();
    }

    private void installGoogleScanner(Context context) {

        ModuleInstallClient moduleInstall = ModuleInstall.getClient(context);
        ModuleInstallRequest moduleInstallRequest = ModuleInstallRequest
                .newBuilder()
                .addApi(GmsBarcodeScanning.getClient(context)).build();

        moduleInstall.installModules(moduleInstallRequest).addOnSuccessListener(moduleInstallResponse -> {

            IsScannerInstalledData.setValue(true);

        }).addOnFailureListener(e -> {

            IsScannerInstalledData.setValue(false);

        });
    }

    public void startScanning() {
        scanner.startScan().addOnSuccessListener(barcode -> {
                    String result = barcode.getRawValue();
                    if (result != null) {
                        String scannedValue = "Scanned Value:" + result;
                        successLiveData.setValue(scannedValue);
                    }
                })
                .addOnFailureListener(e -> {
                    failureLiveData.setValue(e.getMessage());
                })
                .addOnCanceledListener(() ->
                {
                    cancelLiveData.setValue("Cancelled");
                });
    }
}
