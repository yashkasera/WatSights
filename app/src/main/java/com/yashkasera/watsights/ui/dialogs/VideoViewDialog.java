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

package com.yashkasera.watsights.ui.dialogs;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;

import java.io.File;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class VideoViewDialog extends DialogFragment {

    String path;
    MediaController mediaController;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    FloatingActionButton fab1, fab2, fab3;
    TextView hint2, hint3;
    boolean isOpen = false;
    Uri uri;

    public VideoViewDialog() {
    }

    public static VideoViewDialog newInstance(String path) {
        VideoViewDialog frag = new VideoViewDialog();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_video_view, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab(view);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_DayNight);
        path = getArguments().getString("path");
        if (path != null) {
            VideoView videoview = view.findViewById(R.id.videoView);
            uri = Uri.parse(path);
            videoview.setVideoURI(uri);
            videoview.start();
            mediaController = new MediaController(getContext());
            videoview.setMediaController(mediaController);
            videoview.setOnPreparedListener(mp -> mp.setLooping(true));
            view.findViewById(R.id.fab3).setOnClickListener(v -> {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share video using"));
            });
            view.findViewById(R.id.fab2).setOnClickListener(v -> {
                File file = new File(path);
                if (file.exists()) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to delete this file? This action cannot be undone")
                            .setPositiveButton("Yes", (dialog, which) -> deleteFile(v, file))
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .create().show();
                }
            });
            view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        } else {
            Snackbar.make(getContext(), view, "Unable to play video", LENGTH_SHORT)
                    .show();
            dismiss();
        }
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_anticlock);
    }

    private void deleteFile(View v, File file) {
        if (file.delete()) {
            Snackbar.make(v, "File Deleted Successfully", LENGTH_SHORT).show();
            dismiss();
        } else {
            Snackbar.make(v, "File could not be deleted", LENGTH_SHORT)
                    .setAction("Retry", v1 -> deleteFile(v1, file))
                    .show();
        }
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

        fab1.setOnClickListener(v -> {
            if (isOpen) {
                show_all();
                fab1.startAnimation(fab_anticlock);
                isOpen = false;
            } else {
                hide_all();
                fab1.startAnimation(fab_clock);
                isOpen = true;
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
