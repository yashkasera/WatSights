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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.FileAdapter;
import com.yashkasera.watsights.model.FileDetails;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class AudioDialog extends BottomSheetDialogFragment {

    private final Handler myHandler = new Handler();
    String path;
    TextView title, duration;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    ImageButton pause;
    Uri uri;
    private double startTime = 0;
    private final Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                startTime = mediaPlayer.getCurrentPosition();
                duration.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekBar.setProgress((int) startTime);
                myHandler.postDelayed(this, 100);
            }
        }
    };
    private double finalTime = 0;
    private int position;

    public AudioDialog() {
    }

    public static AudioDialog newInstance(int position, String title, String path) {
        AudioDialog frag = new AudioDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("path", path);
        bundle.putString("title", title);
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_audio, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_DayNight);
        path = getArguments().getString("path");
        position = getArguments().getInt("position");
        if (path != null) {
            title = view.findViewById(R.id.title);
            title.setText(getArguments().getString("title"));
            duration = view.findViewById(R.id.duration);
            seekBar = view.findViewById(R.id.seekBar);
            uri = Uri.parse(path);
            mediaPlayer = MediaPlayer.create(getContext(), uri);
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();
            seekBar.setMax((int) finalTime);
            pause = view.findViewById(R.id.pause);
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_motion_photos_on_24));
                    } else {
                        mediaPlayer.start();
                        pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_pause_circle_outline_24));
                    }
                }
            });

            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(UpdateSongTime, 100);
            mediaPlayer.start();
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.pause();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                }
            });

        }

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                dismiss();
            }
        });
        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_motion_photos_on_24));
                }
                File file = new File(path);
                if (file != null && file.exists()) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to delete this file? This action cannot be undone")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                }
                                deleteFile(v, file);
                            }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
                }
            }
        });
        view.findViewById(R.id.share).setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("audio/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        });
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

    private void deleteFile(View v, File file) {
        if (file.delete()) {
            Snackbar.make(v, "File Deleted Successfully", LENGTH_SHORT).show();
            dismiss();
            FileAdapter fileAdapter = new FileAdapter();
            List<FileDetails> list = fileAdapter.getList();
            list.remove(position);
            fileAdapter.setList(list);
        } else {
            Snackbar.make(v, "File could not be deleted", LENGTH_SHORT)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteFile(v, file);
                        }
                    }).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
