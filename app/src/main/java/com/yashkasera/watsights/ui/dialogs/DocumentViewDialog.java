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

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.FileAdapter;
import com.yashkasera.watsights.model.FileDetails;

import java.io.File;
import java.util.List;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class DocumentViewDialog extends DialogFragment {

    public FloatingActionButton fab1, fab2, fab3;
    String path;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView hint2, hint3;
    boolean isOpen = false;
    Uri uri;
    int position;

    public DocumentViewDialog() {
    }

    public static DocumentViewDialog newInstance(int position, String path) {
        DocumentViewDialog frag = new DocumentViewDialog();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putInt("position", position);
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_web_view, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_DayNight);
        fab(view);
        path = getArguments().getString("path");
        position = getArguments().getInt("position");
        final WebView webView = view.findViewById(R.id.webView);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_anticlock);
        if (path != null) {
            uri = Uri.parse(path);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadDataWithBaseURL("",
                    "<embed src='" + path + "' width='300' height='500'>",
                    "application/pdf",
                    "utf-8",
                    "");
        } else {
            Snackbar.make(getContext(), view, "There was an error! Please try again", LENGTH_SHORT)
                    .show();
            dismiss();
        }
        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.fab2).setOnClickListener(v -> {
            File file = new File(path);
            if (file != null && file.exists()) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this file? This action cannot be undone")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFile(v, file);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });
        view.findViewById(R.id.fab3).setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        });
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
                    .setAction("Retry", v1 -> deleteFile(v1, file)).show();
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
