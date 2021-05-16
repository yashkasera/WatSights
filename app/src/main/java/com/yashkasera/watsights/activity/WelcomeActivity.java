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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.util.Constants;
import com.yashkasera.watsights.util.SharedPreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private static final int REQUEST_MANAGE_STORAGE_CODE = 1001;
    private static final int REQUEST_NOTIFICATION_ACCESS_CODE = 1002;
    private static final int PERMISSION_REQUEST_CODE = 1003;
    private final int NoOfSlides = 6;
    private final Context context = this;
    SharedPreferenceManager sharedPreferenceManager;
    Button allow;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private FloatingActionButton next, prev;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == 0) {
                prev.setVisibility(View.GONE);
            } else {
                prev.setVisibility(View.VISIBLE);
            }
            if (position == 4 || position == 5) allow.setVisibility(View.VISIBLE);
            else allow.setVisibility(View.INVISIBLE);
            if (position == 4) {
                allow.setEnabled(!isNotificationServiceEnabled());
            }
            if (position == 5) {
                allow.setEnabled(!hasStoragePermission());
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        sharedPreferenceManager = new SharedPreferenceManager(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        editor.putString("display", "system_default");
        editor.apply();
        setContentView(R.layout.activity_welcome);
        allow = findViewById(R.id.allow);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        prev.setVisibility(View.GONE);

        addBottomDots(0);

        changeStatusBarColor();
        allow.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == 4) {
                startActivityForResult(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUEST_NOTIFICATION_ACCESS_CODE);
            } else if (viewPager.getCurrentItem() == 5) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    startActivityForResult(new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), REQUEST_MANAGE_STORAGE_CODE);
                } else {
                    ActivityCompat.requestPermissions(WelcomeActivity.this,
                            new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        });
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        prev.setOnClickListener(v -> {
            int current = getItem(-1);
            if (current >= 0) viewPager.setCurrentItem(current);
        });

        next.setOnClickListener(v -> {
            int current = getItem(+1);
            Log.d(TAG, "onClick() returned: " + current);
            if (current < NoOfSlides)
                viewPager.setCurrentItem(current);
            else {
                launchHomeScreen();
            }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[NoOfSlides];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        next.setBackgroundTintList(ColorStateList.valueOf(colorsActive[currentPage]));
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        if (!isNotificationServiceEnabled()) {
            View view = findViewById(R.id.welcome_activity);
            Snackbar.make(view, "Please allow Notification access to proceed!",
                    BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("ALLOW", v -> startActivityForResult(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUEST_NOTIFICATION_ACCESS_CODE)).show();
        } else if (!hasStoragePermission()) {
            View view = findViewById(R.id.welcome_activity);
            Snackbar.make(view, "Please allow Storage access to proceed!",
                    BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("ALLOW", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                startActivityForResult(new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), REQUEST_MANAGE_STORAGE_CODE);
                            } else {
                                ActivityCompat.requestPermissions(WelcomeActivity.this,
                                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            }
                        }
                    }).show();
        } else {
            File deletedMediaDir = Constants.DELETED_MEDIA_DIR;
            if (!deletedMediaDir.exists()) deletedMediaDir.mkdirs();
            File statusVideos = new File(Environment.getExternalStorageDirectory(), "WatSights" + File.separator + "Whatsapp Status Videos");
            if (!statusVideos.exists()) statusVideos.mkdirs();
            File temp = new File(Environment.getExternalStorageDirectory(), "WatSights" + File.separator + "Whatsapp Deleted Images" + File.separator + "temp");
            if (!temp.exists()) temp.mkdirs();
            Log.i(TAG, "launchHomeScreen: start");
//            sharedPreferenceManager.setFirstTimeLaunch(false);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
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

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NOTIFICATION_ACCESS_CODE) {
            allow.setEnabled(!isNotificationServiceEnabled());
        }
        if (requestCode == REQUEST_MANAGE_STORAGE_CODE) {
            allow.setEnabled(!hasStoragePermission());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                allow.setEnabled(!hasStoragePermission());
                if (hasStoragePermission()) {
                    launchHomeScreen();
                }
            }
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        TextView title, text;
        View background;
        ImageView icon;
        int[] icons = {R.drawable.ic_slide_sticker_1,
                R.drawable.ic_slide_sticker_2,
                R.drawable.ic_slide_sticker_3,
                R.drawable.ic_slide_sticker_4,
                R.drawable.ic_slide_sticker_5};
        int[] backgrounds = {R.drawable.slide_background_1,
                R.drawable.slide_background_2,
                R.drawable.slide_background_3,
                R.drawable.slide_background_4,
                R.drawable.slide_background_5,
        };
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            if (position > 0) {
                view = layoutInflater.inflate(R.layout.welcome_slide1, container, false);
                title = view.findViewById(R.id.title);
                text = view.findViewById(R.id.text);
                icon = view.findViewById(R.id.icon);
                background = view.findViewById(R.id.background);
                title.setText(getResources().getStringArray(R.array.welcome_title)[position - 1]);
                text.setText(getResources().getStringArray(R.array.welcome_text)[position - 1]);
                icon.setImageDrawable(ContextCompat.getDrawable(context, icons[position - 1]));
                background.setBackground(ContextCompat.getDrawable(context, backgrounds[position - 1]));

            } else {
                view = layoutInflater.inflate(R.layout.welcome_slide0, container, false);
                TextInputEditText name = view.findViewById(R.id.name);
                TextInputLayout name1 = view.findViewById(R.id.name1);
                view.findViewById(R.id.done).setOnClickListener(v -> {
                    if (name != null && name.getText().toString().length() != 0) {
                        editor.putString("user_name", name.getText().toString());
                        if (editor.commit()) {
                            Log.i(TAG, "onClick: username added");
                            hideSoftKeyboard(WelcomeActivity.this);
                            viewPager.setCurrentItem(position + 1);
                        }
                        name1.setError(null);
                    } else {
                        name1.setError("Invalid Name");
                    }
                });

            }

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return NoOfSlides;
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}