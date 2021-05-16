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

package com.yashkasera.watsights.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.FileAdapter;
import com.yashkasera.watsights.model.FileDetails;
import com.yashkasera.watsights.ui.dialogs.AudioDialog;
import com.yashkasera.watsights.ui.dialogs.DocumentViewDialog;
import com.yashkasera.watsights.ui.dialogs.ImageViewDialog;
import com.yashkasera.watsights.ui.dialogs.InfoDialog;
import com.yashkasera.watsights.ui.dialogs.VideoViewDialog;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.Constants;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewFilesFragment extends Fragment {

    private static final String TAG = "ViewFilesActivity";
    RecyclerView recyclerView;
    List<FileDetails> files;
    List<FileDetails> selectedFiles;
    int type;
    String path;
    Context context;
    ProgressDialog progressDialog;
    FileAdapter fileAdapter;

    boolean selectAll = false;
    private boolean isLinear = false;

    public static ViewFilesFragment newInstance() {
        return new ViewFilesFragment();
    }

    private static long getFileSize(File file) {
        if (file != null && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        setHasOptionsMenu(true);
        type = getArguments().getInt("type");
        recyclerView = view.findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(getContext());
        files = new ArrayList<>();
        selectedFiles = new ArrayList<>();
        isLinear = true;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        fileAdapter = new FileAdapter(files, context, isLinear, type, ViewFilesFragment.this);
        recyclerView.setAdapter(fileAdapter);
        switch (type) {
            case Constants.IMAGE:
                path = Constants.IMAGES_RECEIVED_PATH;
                break;
            case Constants.VIDEO:
                path = Constants.VIDEOS_RECEIVED_PATH;
                break;
            case Constants.AUDIO:
                path = Constants.AUDIO_RECEIVED_PATH;
                break;
            case Constants.DOCUMENT:
                path = Constants.DOCUMENTS_RECEIVED_PATH;
                break;
            case Constants.VOICE:
                path = Constants.VOICE_RECEIVED_PATH;
                break;
            case Constants.STICKERS:
                path = Constants.STICKERS_PATH;
                break;
            case Constants.WALLPAPER:
                path = Constants.WALLPAPER_RECEIVED_PATH;
                break;
            case Constants.GIF:
                path = Constants.GIF_RECEIVED_PATH;
                break;
            default:
                Toast.makeText(context, "There was an error processing your request. Please try again", Toast.LENGTH_SHORT).show();
        }
        try {
            files = new GetFiles().execute(path).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private String getExtension(File file) {
        Uri uri = Uri.fromFile(file);
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_files, menu);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: " + item.getTitle());
        switch (item.getItemId()) {
            case android.R.id.home:
                NavHostFragment.findNavController(ViewFilesFragment.this).navigate(R.id.action_viewFilesFragment_to_navigation_storage);
                return true;
            case R.id.action_grid:
                if (!isLinear) {
                    item.setIcon(context.getDrawable(R.drawable.ic_baseline_list_24));
                    isLinear = true;
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                } else {
                    item.setIcon(context.getDrawable(R.drawable.ic_baseline_grid_on_24));
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    isLinear = false;
                }
                fileAdapter = new FileAdapter(files, context, isLinear, type, ViewFilesFragment.this);
                recyclerView.setAdapter(fileAdapter);
                fileAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_delete:
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete selected images?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                deleteFiles();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
                return true;
            case R.id.action_share:
                List<String> pathList = new ArrayList<>();
                for (FileDetails fileDetails : selectedFiles) {
                    Log.d(TAG, "onOptionsItemSelected() returned: " + fileDetails.getPath());
                    pathList.add(fileDetails.getPath());
                }
                shareMultiple(pathList, context);
                return true;
            case R.id.action_select_all:
                if (item.isChecked()) {
                    selectedFiles.clear();
                    for (FileDetails fileDetails : files) {
                        fileDetails.setSelected(false);
                    }
                    item.setChecked(false);
                } else {
                    for (FileDetails fileDetails : files) {
                        fileDetails.setSelected(true);
                    }
                    selectedFiles.clear();
                    selectedFiles.addAll(files);
                    item.setChecked(true);
                }
                fileAdapter.setList(files);
                fileAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_info:
                FragmentTransaction transaction = ((FragmentActivity) getContext())
                        .getSupportFragmentManager()
                        .beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("title", "View a file");
                bundle.putString("message", "Tap on a file to select it. Long press on any file to view it");
                bundle.putString("button", "DISMISS");

                InfoDialog.newInstance(bundle).show(transaction, "dialog_info");
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void deleteFiles() throws IOException {
        int j = 0;
        for (FileDetails fileDetails : selectedFiles) {
            File file = new File(fileDetails.getPath());
            if (file.delete()) {
                j++;
                Log.d(TAG, "onClick() deleted: " + fileDetails.getName());
                fileAdapter.removeItem(fileDetails);
            }
        }
        Toast.makeText(context, j + " files deleted", Toast.LENGTH_SHORT).show();
    }

    private void filesReceived() {
        if (files != null) {
            fileAdapter.setList(files);
            recyclerView.setAdapter(fileAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (!files.get(position).isSelected()) {
                        files.get(position).setSelected(true);
                        if (!selectedFiles.contains(files.get(position))) {
                            selectedFiles.add(files.get(position));
                        }
                    } else {
                        files.get(position).setSelected(false);
                        selectedFiles.remove(files.get(position));
                    }
                    fileAdapter.setList(files);
                    fileAdapter.notifyItemChanged(position);
                }

                @Override
                public void onLongClick(View view, int position) {
                    if (type == Constants.IMAGE || type == Constants.STICKERS || type == Constants.WALLPAPER) {
                        FragmentManager fm = getChildFragmentManager();
                        ImageViewDialog imageViewDialog = ImageViewDialog.newInstance(position, files.get(position).getPath());
                        imageViewDialog.show(fm, "dialog_image_view");
                    } else if (type == Constants.VIDEO || type == Constants.GIF) {
                        FragmentManager fm = getChildFragmentManager();
                        VideoViewDialog videoViewDialog = VideoViewDialog.newInstance(files.get(position).getPath());
                        videoViewDialog.show(fm, "dialog_video_view");
                    } else if (type == Constants.AUDIO || type == Constants.VOICE) {
                        FragmentManager fm = getChildFragmentManager();
                        AudioDialog audioDialog = AudioDialog.newInstance(position, files.get(position).getName(), files.get(position).getPath());
                        audioDialog.show(fm, "dialog_audio");
                    } else if (type == Constants.DOCUMENT) {
                        FragmentManager fm = getChildFragmentManager();
                        DocumentViewDialog documentViewDialog = DocumentViewDialog.newInstance(position, files.get(position).getPath());
                        documentViewDialog.show(fm, "dialog_document");
                    }
                }
            }));
        }
    }

    public void shareMultiple(List<String> path, Context context) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (String s : path) {
            uris.add(Uri.parse(s));
        }
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("application/pdf/*|image|video/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(intent, "Share Files Using"));
    }

    private class GetFiles extends AsyncTask<String, Void, ArrayList<FileDetails>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<FileDetails> doInBackground(String... strings) {
            ArrayList<FileDetails> files = new ArrayList<>();
            String path = strings[0];

            if (path != null) {
                File directory = new File(path);
                File[] results = directory.listFiles();
                if (results != null) {
                    //Still verify if the file is an image in whatsapp preferred format(jpg)
                    for (final File file : results) {
                        if (file.isFile()) {
                            if (!file.getName().endsWith(".nomedia")) {
                                FileDetails fileDetails = new FileDetails();
                                fileDetails.setName(file.getName());
                                fileDetails.setPath(file.getPath());
                                fileDetails.setMod(file.lastModified());
                                fileDetails.setSize(Formatter.formatShortFileSize(context, getFileSize(file)));
                                fileDetails.setS(getFileSize(file));
                                fileDetails.setExt(getExtension(file));
                                fileDetails.setSelected(false);
                                files.add(fileDetails);
                            }
                        } else if (file.isDirectory()) {
                            if (!file.getName().equals("Sent")) {
                                File[] res = file.listFiles();
                                if (res != null) {
                                    for (File re : res) {
                                        if (!re.getName().endsWith(".nomedia")) {
                                            FileDetails fileDetails = new FileDetails();
                                            fileDetails.setName(re.getName());
                                            fileDetails.setPath(re.getPath());
                                            fileDetails.setMod(re.lastModified());
                                            fileDetails.setSize(Formatter.formatShortFileSize(context, getFileSize(re)));
                                            fileDetails.setExt(getExtension(file));
                                            fileDetails.setS(getFileSize(re));
                                            fileDetails.setSelected(false);
                                            Log.e("size", String.valueOf(getFileSize(re)));
                                            files.add(fileDetails);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.e("Files", "No files found in " + directory.getName());
                }
            }
            return files;
        }

        @Override
        protected void onPostExecute(ArrayList<FileDetails> files) {
            super.onPostExecute(files);
            progressDialog.dismiss();
            filesReceived();
        }
    }


}