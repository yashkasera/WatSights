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

package com.yashkasera.watsights.ui.deleted;

import android.os.Bundle;
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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.DeletedMediaAdapter;
import com.yashkasera.watsights.model.FileDetails;
import com.yashkasera.watsights.ui.status.ViewImageDialog;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.Constants;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeletedFragment extends Fragment {
    private static final String TAG = "DeletedFragment";
    DeletedMediaAdapter deletedMediaAdapter;
    RecyclerView recyclerView;
    private List<FileDetails> list;
    int count = 0;
    public DeletedFragment() {
        // Required empty public constructor
    }

    public static DeletedFragment newInstance(String param1, String param2) {
        return new DeletedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list = new ArrayList<>();
        deletedMediaAdapter = new DeletedMediaAdapter(getContext(), list);
        if (Constants.DELETED_MEDIA_DIR.exists()) {
            File[] files = Constants.DELETED_MEDIA_DIR.listFiles();
            if (files != null) {
                Log.d(TAG, "getStatus: " + files.length);
                Arrays.sort(files);
                for (File file : files) {
                    Log.d(TAG, "getStatus: " + file.getName());
                    if (!file.getName().endsWith(".nomedia") && file.isFile()) {
                        FileDetails fileDetails = new FileDetails();
                        fileDetails.setName(file.getName());
                        fileDetails.setPath(file.getPath());
                        fileDetails.setSize(getFormattedSize(file.length()));
                        count++;
                        deletedMediaAdapter.addItem(fileDetails);
                    }
                }
                if (files.length == 1 && files[0].getName().equalsIgnoreCase("temp")) {
                    view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    view.findViewById(R.id.no_results).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Files is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Directory does not exist!", Toast.LENGTH_SHORT).show();
        }
        recyclerView.setAdapter(deletedMediaAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("path", list.get(position).getPath());
                FragmentTransaction transaction = ((FragmentActivity) getContext())
                        .getSupportFragmentManager()
                        .beginTransaction();
                ViewImageDialog.newInstance(bundle).show(transaction, "dialog_image_view");
//                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private String getFormattedSize(long sizeBytes) {
        return android.text.format.Formatter.formatShortFileSize(getContext(), sizeBytes);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            NavHostFragment.findNavController(DeletedFragment.this).navigate(R.id.action_navigation_deleted_to_navigation_more_options);
        return super.onOptionsItemSelected(item);
    }
}