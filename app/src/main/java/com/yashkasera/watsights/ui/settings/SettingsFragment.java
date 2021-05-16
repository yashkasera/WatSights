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

package com.yashkasera.watsights.ui.settings;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.util.Constants;

import java.io.File;
import java.io.IOException;

import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT;
import static androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;
import static androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED;
import static androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";
    SwitchPreference biometrics;
    boolean enableBiometrics = false;
    private SettingsViewModel mViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        Preference important_filters = findPreference("important_filters");
        assert important_filters != null;
        important_filters.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NavHostFragment.findNavController(SettingsFragment.this).navigate(R.id.action_settingsFragment_to_importantFiltersFragment);
                return true;
            }
        });
        ListPreference displayMode = findPreference("display");
        assert displayMode != null;
        displayMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i(TAG, "onPreferenceChange: " + newValue);
                if (newValue.equals("light_mode")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (newValue.equals("dark_mode")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                return true;
            }
        });
        biometrics = findPreference("biometrics");
        biometrics.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        return biometric();
                    } else {
                        Toast.makeText(getContext(), "This device does not support biometrics", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Biometrics disabled Successfully", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;

            }
        });

        Preference elite = findPreference("elite");
        assert elite != null;
        elite.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Confirm Reset Elite Members")
                        .setMessage("Removes all Elite Members. Their messages will not be deleted" +
                                "\nThis action cannot be undone. Do you still want to proceed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mViewModel.resetElite();
                                    Toast.makeText(getContext(), "All Elite Members removed", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e(TAG, "onClick: ", e);
                                } finally {
                                    dialog.dismiss();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
            }
        });
        Preference spammers = findPreference("spammers");
        assert spammers != null;
        spammers.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Confirm Reset Spammers")
                        .setMessage("Removes all Spammers. Their messages will not be deleted" +
                                "\nThis action cannot be undone. Do you still want to proceed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mViewModel.resetSpammers();
                                    Toast.makeText(getContext(), "All Spammers removed", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e(TAG, "onClick: ", e);
                                } finally {
                                    dialog.dismiss();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
            }
        });
        Preference clearApp = findPreference("clearApp");
        assert clearApp != null;
        clearApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage(R.string.clear_data)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mViewModel.deleteAllMessages();
                                    Toast.makeText(getContext(), "All Messages deleted Successfully", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e(TAG, "onClick: ", e);
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
            }
        });
        Preference clearAll = findPreference("clearAll");
        assert clearAll != null;
        clearAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage(R.string.delete_app_data)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                mViewModel.deleteAllMessages();
                                File file = new File(Constants.APP_DIR);
                                if (file.exists())
                                    deleteAllFiles(file);
                                Toast.makeText(getContext(), "All data deleted Successfully", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "onClick: ", e);
                            }
                        }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
                return true;
            }
        });
    }

    void deleteAllFiles(File parentDir) throws IOException {
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteAllFiles(file);
            } else {
                file.delete();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    boolean biometric() {
        switch (BiometricManager.from(getContext()).canAuthenticate()) {
            case BIOMETRIC_SUCCESS:
                Toast.makeText(getContext(), "Biometrics enabled successfully", Toast.LENGTH_SHORT).show();
                return true;
            case BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getContext(), "The operation was canceled because the biometric sensor is unavailable", Toast.LENGTH_SHORT).show();
                return false;
            case BIOMETRIC_ERROR_HW_NOT_PRESENT:
                Toast.makeText(getContext(), "The device does not have a biometric sensor", Toast.LENGTH_SHORT).show();
                return false;
            case BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getContext(), "Please enable system wide biometrics to enable this feature", Toast.LENGTH_SHORT).show();
                return false;
        }
        return false;
    }
}