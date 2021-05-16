/*
 * Copyright (c) 2021. Yash Kasera
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yashkasera.watsights.activity;


import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.services.MediaListenerService;
import com.yashkasera.watsights.util.NotificationBuilder;
import com.yashkasera.watsights.util.SharedPreferenceManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_CANCELED;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT;
import static androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    SharedPreferences sharedPreferences;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceManager = new SharedPreferenceManager(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mode = sharedPreferences.getString("display", "system_default");
        if (mode.equals("light_mode")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (mode.equals("dark_mode")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        new NotificationBuilder(this).createImportantNotificationChannel();
        new NotificationBuilder(this).createEliteNotificationChannel();
        new NotificationBuilder(this).createMentionNotificationChannel();
        new NotificationBuilder(this).createDeletedMediaNotificationChannel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionOrStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void biometric() {
        Executor newExecutor = Executors.newSingleThreadExecutor();
        BiometricPrompt myBiometricPrompt = new BiometricPrompt(this, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                switch (errorCode) {
                    case BIOMETRIC_ERROR_CANCELED:
                        Log.i(TAG, "Cancelled");
                        break;
                    case BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.i(TAG, "The operation was canceled because the biometric sensor is unavailable");
                        break;
                    case BIOMETRIC_ERROR_HW_NOT_PRESENT:
                        Log.i(TAG, "The device does not have a biometric sensor");
                        break;
                    default:
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "Fingerprint recognised successfully");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "Fingerprint not recognised");
            }
        });
        final BiometricPrompt.PromptInfo promptInfo;
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Verification Required")
                .setConfirmationRequired(true)
                .setDeviceCredentialAllowed(true)
                .build();
        myBiometricPrompt.authenticate(promptInfo);

    }

    void checkPermissionOrStart() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(getBaseContext(), MediaListenerService.class));
                if (sharedPreferenceManager.isFirstTimeLaunch()) {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                    return;
                } else {
                    if (sharedPreferences.getBoolean("biometrics", false)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            biometric();
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }
//                    else{
//                    new AlertDialog.Builder(SplashActivity.this, R.style.AlertDialogCustom)
//                            .setTitle("Enable Notification Access")
//                            .setMessage("Please allow " + getString(R.string.app_name) + " to read notifications.\nThese messages are stored locally and are not accessible by us")
//                            .setCancelable(false)
//                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
//                                }
//                            }).show();
//                }
//            }
            }
        }, 1000);
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}