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

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.services.MediaListenerService;
import com.yashkasera.watsights.util.Constants;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_MANAGE_STORAGE_CODE = 1001;
    private static final int REQUEST_NOTIFICATION_ACCESS_CODE = 1002;
    private static final int PERMISSION_REQUEST_CODE = 1003;
    Toolbar toolbar;
    Snackbar snackbarStorage, snackbarNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_direct, R.id.navigation_groups, R.id.navigation_more_options, R.id.navigation_storage)
                .build();
        checkPermissions();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    void checkPermissions() {
        if (!isNotificationServiceEnabled()) {
            View view = findViewById(R.id.main_activity);
            snackbarNotifications = Snackbar.make(view, "Some app features might not work. Please allow Notification access to proceed!",
                    BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("ALLOW", v -> startActivityForResult(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUEST_NOTIFICATION_ACCESS_CODE));
            snackbarNotifications.show();
        } else if (!hasStoragePermission()) {
            View view = findViewById(R.id.main_activity);
            snackbarStorage = Snackbar.make(view, "Some app features might not work. Please allow Storage access to proceed!",
                    BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("ALLOW", v -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            startActivityForResult(new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), REQUEST_MANAGE_STORAGE_CODE);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    });
            snackbarStorage.show();
        } else {
            if (snackbarNotifications != null) snackbarNotifications.dismiss();
            if (snackbarStorage != null) snackbarStorage.dismiss();
            File deletedMediaDir = Constants.DELETED_MEDIA_DIR;
            if (!deletedMediaDir.exists()) deletedMediaDir.mkdirs();
            File statusVideos = new File(Environment.getExternalStorageDirectory(), "WatSights" + File.separator + "Whatsapp Status Videos");
            if (!statusVideos.exists()) statusVideos.mkdirs();
            File temp = new File(Environment.getExternalStorageDirectory(), "WatSights" + File.separator + "Whatsapp Deleted Images" + File.separator + "temp");
            if (!temp.exists()) temp.mkdirs();
        }
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

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NOTIFICATION_ACCESS_CODE) {
            startService(new Intent(getBaseContext(), NotificationListenerService.class));
            checkPermissions();
        }
        if (requestCode == REQUEST_MANAGE_STORAGE_CODE) {
            startService(new Intent(getBaseContext(), MediaListenerService.class));
            checkPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                startService(new Intent(getBaseContext(), NotificationListenerService.class));
                checkPermissions();
            }
        }
    }
}