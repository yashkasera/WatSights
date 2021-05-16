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

package com.yashkasera.watsights.ui.elite;

import android.app.Activity;
import android.content.Intent;
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

import com.google.android.material.chip.Chip;
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

public class EliteMessagesFragment extends Fragment {

    private static final String TAG = "PinnedFragment";
    RecyclerView recyclerView;
    List<MessageQuery> list;
    MessageAdapter messageAdapter;
    Repository repository;
    Chip sender;
    boolean isAll = true;
    View view;
    private EliteMessagesViewModel mViewModel;

    public static EliteMessagesFragment newInstance() {
        return new EliteMessagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_elite, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = new ViewModelProvider(this).get(EliteMessagesViewModel.class);
        repository = mViewModel.getRepository();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        sender = view.findViewById(R.id.sender);
        list = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(), list, true);
        recyclerView.setAdapter(messageAdapter);
        showAll();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Snackbar.make(getContext(), view, "Long press on a message to pin it", BaseTransientBottomBar.LENGTH_SHORT)
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
                        Snackbar snackbar = Snackbar.make(getContext(), view, "Message Pinned", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
            }
        }));
        sender.setOnClickListener(v -> {
            if (isAll) {
                EliteMembersFragment eliteMembersFragment = new EliteMembersFragment();
                eliteMembersFragment.setTargetFragment(EliteMessagesFragment.this, 1);
                eliteMembersFragment.show(getFragmentManager().beginTransaction(), "get person id");
            } else showAll();
        });
    }

    private void showAll() {
        sender.setCloseIconVisible(false);
        sender.setText("Sent By : All");
        mViewModel.getEliteMessages().observe(getViewLifecycleOwner(), messages -> {
            list = messages;
            if (list.size() == 0) {
                view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.no_results).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                messageAdapter.setList(list);
                recyclerView.swapAdapter(messageAdapter, true);
                recyclerView.scrollToPosition(list.size() - 1);
            }

        });
        isAll = true;
    }

    private void showElitePerson(long personId, String personName) {
        sender.setCloseIconVisible(true);
        Log.d(TAG, "showElitePerson() returned: " + personName);
        sender.setText("Sent By : " + personName);
        mViewModel.getList(personId).observe(getViewLifecycleOwner(), messages -> {
            for (MessageQuery messageQuery : messages) {
                Log.d(TAG, "onChanged() returned: " + messageQuery.getMessage());
            }
            list = messages;
            if (list.size() == 0) {
                view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.no_results).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                messageAdapter.setList(list);
                recyclerView.swapAdapter(messageAdapter, true);
            }

        });
        isAll = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    long personId = data.getLongExtra("personId", -1);
                    String personName = data.getStringExtra("personName");
                    Log.d(TAG, "onActivityResult() returned: " + data.getLongExtra("personId", -1));
                    if (personId != -1) {
                        showElitePerson(personId, personName);
                    }
                }
                break;
        }
    }


}