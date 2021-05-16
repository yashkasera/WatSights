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

package com.yashkasera.watsights.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.Repository;
import com.yashkasera.watsights.activity.MainActivity;
import com.yashkasera.watsights.adapter.MessageAdapter;
import com.yashkasera.watsights.model.Group;
import com.yashkasera.watsights.model.MessageQuery;
import com.yashkasera.watsights.model.PinnedMessage;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    Repository repository;
    long groupId;
    Group group;
    private List<MessageQuery> list;
    private ChatViewModel mViewModel;
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        groupId = getArguments().getLong("groupId");
        mViewModel.getGroup(groupId).observe(getViewLifecycleOwner(), group1 -> {
            group = group1;
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(group.getName());
        });
        repository = mViewModel.getRepository();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity(), list, false);
        recyclerView.setAdapter(messageAdapter);
        mViewModel.getList(groupId).observe(getViewLifecycleOwner(), messages -> {
            list = messages;
            if (list.size() == 0) {
                view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.no_results).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                messageAdapter.setList(list);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(list.size() - 1);
            }
        });
        view.findViewById(R.id.reply).setOnClickListener(v -> {
            ReplyFragment replyFragment = new ReplyFragment();
            replyFragment.setTargetFragment(ChatFragment.this, 1005);
            replyFragment.show(getFragmentManager().beginTransaction(), "dialog_call_log");
        });
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
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
            }
        }));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavHostFragment.findNavController(ChatFragment.this).navigate(R.id.action_chatFragment_to_navigation_groups);
                return true;
            case R.id.action_info:
                Log.i(TAG, "onOptionsItemSelected: action_info clicked");
                Bundle bundle = new Bundle();
                bundle.putLong("groupId", groupId);
                NavHostFragment.findNavController(ChatFragment.this).navigate(R.id.action_chatFragment_to_groupInfoFragment, bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}