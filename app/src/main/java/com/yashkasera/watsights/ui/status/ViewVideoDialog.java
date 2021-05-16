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

package com.yashkasera.watsights.ui.status;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.model.Status;

import java.io.File;

import kotlin.io.FilesKt;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class ViewVideoDialog extends DialogFragment {
    private static final String TAG = "ItemListDialogFragment";
    static Status status;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    FloatingActionButton fab1, fab2, fab3;
    TextView hint2, hint3;
    VideoView videoView;
    boolean isOpen = false;
    MediaController mediaController;

    public static ViewVideoDialog newInstance(Bundle bundle) {
        final ViewVideoDialog fragment = new ViewVideoDialog();
        status = (Status) bundle.getSerializable("status");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_dialog, container, false);
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fab(view);
        Log.d(TAG, "onViewCreated() returned: " + status.getPath());
        videoView = view.findViewById(R.id.videoView);
        Uri uri = Uri.parse(status.getPath());
        videoView.setVideoURI(uri);
        videoView.start();
        mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));
        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.fab3).setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Share video using"));
        });
        view.findViewById(R.id.fab2).setOnClickListener(v -> {
            if (addVideo(status.getPath())) {
                Snackbar.make(getContext(), v, "Video saved to WatSights/Whatsapp Status Videos", LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(getContext(), v, "There was an error! Please try again", LENGTH_SHORT)
                        .show();
            }
        });

        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_anticlock);
    }

    public boolean addVideo(String path) {
        File srcFile = new File(path);
        if (srcFile.exists()) {
            File destFile = new File(Environment.getExternalStorageDirectory(), "WatSights" + File.separator + "Whatsapp Status Videos" + File.separator + srcFile.getName());
            Log.i(TAG, "onEvent: copied to temp");
            return FilesKt.copyTo(srcFile, destFile, true, 16384) != null;
        }
        return false;
    }

    public void fab(View view) {
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        fab3 = view.findViewById(R.id.fab3);
        hint2 = view.findViewById(R.id.hint2);
        hint3 = view.findViewById(R.id.hint3);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        hint2.setVisibility(View.INVISIBLE);
        hint3.setVisibility(View.INVISIBLE);
        fab1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    show_all();
                    fab1.startAnimation(fab_anticlock);
                    isOpen = false;
                } else {
                    hide_all();
                    fab1.startAnimation(fab_clock);
                    isOpen = true;
                }
            }
        });
    }

    private void show_all() {
        fab2.setVisibility(View.VISIBLE);
        fab3.setVisibility(View.VISIBLE);
        hint2.setVisibility(View.VISIBLE);
        hint3.setVisibility(View.VISIBLE);
        fab2.startAnimation(fab_close);
        fab3.startAnimation(fab_close);
        hint2.startAnimation(fab_close);
        hint3.startAnimation(fab_close);
        fab1.setVisibility(View.INVISIBLE);
    }

    private void hide_all() {
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        hint2.setVisibility(View.INVISIBLE);
        hint3.setVisibility(View.INVISIBLE);
        fab2.startAnimation(fab_open);
        fab3.startAnimation(fab_open);
        hint2.startAnimation(fab_open);
        hint3.startAnimation(fab_open);
    }
}