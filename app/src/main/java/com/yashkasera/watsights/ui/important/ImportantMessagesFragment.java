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

package com.yashkasera.watsights.ui.important;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.Repository;
import com.yashkasera.watsights.adapter.MessageAdapter;
import com.yashkasera.watsights.model.MessageQuery;
import com.yashkasera.watsights.model.PinnedMessage;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ImportantMessagesFragment extends Fragment {
    private static final String TAG = "ImportantMessagesFragme";
    RecyclerView recyclerView;
    Repository repository;
    private ImportantMessagesViewModel mViewModel;
    private List<MessageQuery> list;
    private MessageAdapter messageAdapter;

    public static ImportantMessagesFragment newInstance() {
        return new ImportantMessagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ImportantMessagesViewModel.class);
        repository = mViewModel.getRepository();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity(), list, true);
        recyclerView.setAdapter(messageAdapter);
        mViewModel.getImportantMessages().observe(requireActivity(), importantMessages -> {
            list = importantMessages;
            if (list.size() == 0) {
                view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.no_results).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                messageAdapter.setList(list);
                messageAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Snackbar.make(requireContext(), view, "Long press on a message to pin it", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onLongClick(View view, int position) {
                long messageId = list.get(position).getId();
                Log.d(TAG, "onLongClick() returned: " + messageId);
                Executors.newSingleThreadExecutor().execute(() -> {
                    if (repository.databaseDao.isPinned(messageId) != null) {
                        Snackbar.make(view, "Message already pinned", BaseTransientBottomBar.LENGTH_SHORT).show();
                        return;
                    }
                    PinnedMessage pinnedMessage = new PinnedMessage(messageId);
                    long id = repository.databaseDao.insertPinned(pinnedMessage);
                    if (id != -1) {
                        Snackbar.make(view, "Message Pinned", BaseTransientBottomBar.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }));

    }
}