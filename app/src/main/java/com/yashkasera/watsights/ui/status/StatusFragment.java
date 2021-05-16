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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.StatusAdapter;
import com.yashkasera.watsights.model.Status;
import com.yashkasera.watsights.ui.dialogs.InfoDialog;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.Constants;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusFragment extends Fragment {
    private static final String TAG = "StatusFragment";
    StatusAdapter statusAdapter;
    RecyclerView recyclerView;
    private StatusViewModel statusViewModel;
    private List<Status> list;

    public StatusFragment() {
        // Required empty public constructor
    }

    public static StatusFragment newInstance(String param1, String param2) {
        return new StatusFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list = new ArrayList<>();
        statusAdapter = new StatusAdapter(getContext(), list);
        view.findViewById(R.id.info).setOnClickListener(v -> {
            FragmentTransaction transaction = ((FragmentActivity) getContext())
                    .getSupportFragmentManager()
                    .beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("title", "Missing something?");
            bundle.putString("message", "Sometimes you need to view a status for it to appear here.");
            bundle.putString("button", "DISMISS");

            InfoDialog.newInstance(bundle).show(transaction, "dialog_info");
        });
        if (Constants.STATUS_DIRECTORY.exists()) {
            File[] files = Constants.STATUS_DIRECTORY.listFiles();
            if (files != null) {
                Log.d(TAG, "getStatus: " + files.length);
                Arrays.sort(files);
                for (File file : files) {
                    Log.d(TAG, "getStatus: " + file.getName());
                    if (!file.getName().endsWith(".nomedia")) {
                        Status status = new Status(file, file.getName(), file.getAbsolutePath());
                        status.setThumbnail(getThumbnail(status));
                        statusAdapter.addItem(status);
                    }
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Files is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Directory does not exist!", Toast.LENGTH_SHORT).show();
        }
        recyclerView.setAdapter(statusAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (list.get(position).isVideo()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("status", list.get(position));
                    FragmentTransaction transaction = ((FragmentActivity) getContext())
                            .getSupportFragmentManager()
                            .beginTransaction();
                    ViewVideoDialog.newInstance(bundle).show(transaction, "dialog_playback");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", list.get(position).getPath());
                    FragmentTransaction transaction = ((FragmentActivity) getContext())
                            .getSupportFragmentManager()
                            .beginTransaction();
                    ViewImageDialog.newInstance(bundle).show(transaction, "dialog_playback");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    void downloadFile(Status status) throws IOException {
        File file = new File(Constants.APP_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dest = new File(file + File.separator + status.getTitle());
        if (dest.exists()) {
            dest.delete();
        }
        copyFile(Status.getFile(), dest);
    }

    private void copyFile(File file, File dest) throws IOException {
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;

        source = new FileInputStream(file).getChannel();
        destination = new FileOutputStream(dest).getChannel();
        destination.transferFrom(source, 0, source.size());
        source.close();
        destination.close();
    }

    private Bitmap getThumbnail(Status status) {
        if (status.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(Status.getFile().getAbsolutePath(),
                    MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Status.getFile().getAbsolutePath()),
                    Constants.THUMB_SIZE,
                    Constants.THUMB_SIZE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            NavHostFragment.findNavController(StatusFragment.this).navigate(R.id.action_statusFragment_to_navigation_more_options);
        return super.onOptionsItemSelected(item);
    }
}