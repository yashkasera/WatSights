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

package com.yashkasera.watsights.ui.groupInfo;

import android.app.AlertDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.MemberAdapter;
import com.yashkasera.watsights.model.Group;
import com.yashkasera.watsights.model.People;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoFragment extends Fragment {
    private static final String TAG = "GroupInfoFragment";
    long groupId;
    private RecyclerView recyclerView;
    private SwitchCompat store_messages;
    private TextView priority, name, icon;
    private SeekBar seekBar;
    private Group group;
    private List<People> list;
    private MemberAdapter memberAdapter;
    private final String[] options = {"Elite", "Spammer", "None"};
    private GroupInfoViewModel mViewModel;

    public static GroupInfoFragment newInstance() {
        return new GroupInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(GroupInfoViewModel.class);
        assert getArguments() != null;
        groupId = getArguments().getLong("groupId");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        name = view.findViewById(R.id.name);
        icon = view.findViewById(R.id.icon);
        seekBar = view.findViewById(R.id.seekBar);
        priority = view.findViewById(R.id.priority);
        store_messages = view.findViewById(R.id.switch_store_messages);
        mViewModel.getGroup(groupId).observe(getViewLifecycleOwner(), group1 -> {
            group = group1;
            Log.d(TAG, "onViewCreated() returned: " + group);
            if (group != null) {
                name.setText(group.getName());
                store_messages.setChecked(group.isStore_messages());
                priority.setText(String.valueOf(group.getPriority()));
                seekBar.setProgress(group.getPriority());
                GradientDrawable draw = new GradientDrawable();
                draw.setShape(GradientDrawable.OVAL);
                draw.setColor(group1.getIcon());
                icon.setBackground(draw);
                icon.setText(String.valueOf(group1.getName().charAt(0)));
            }
        });
        list = new ArrayList<>();
        memberAdapter = new MemberAdapter(getActivity(), list);
        recyclerView.setAdapter(memberAdapter);
        mViewModel.getMembers(groupId).observe(getViewLifecycleOwner(), people -> {
            list = people;
            recyclerView.setAdapter(memberAdapter);
            memberAdapter.setList(list);
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                People person = list.get(position);
                int checkedItem = person.isElite() ? 0 : (person.isSpammer() ? 1 : 2);
                new AlertDialog.Builder(getContext())
                        .setTitle("Assign a role")
//                            .setMessage("Messages from members with ELITE status will be displayed separately and with SPAMMER status will be given lesser priority")
                        .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                            Log.d(TAG, "onClick() returned: " + which);
                            if (which == 0) {
                                person.setElite(true);
                                person.setSpammer(false);
                                mViewModel.updatePerson(person);
                            } else if (which == 1) {
                                person.setElite(false);
                                person.setSpammer(true);
                                mViewModel.updatePerson(person);
                            } else {
                                person.setElite(false);
                                person.setSpammer(false);
                                mViewModel.updatePerson(person);
                            }
                            dialog.dismiss();
                        }).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        store_messages.setOnCheckedChangeListener((buttonView, isChecked) -> {
            group.setStore_messages(isChecked);
            mViewModel.updateGroup(group);
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priority.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                group.setPriority(seekBar.getProgress());
                mViewModel.updateGroup(group);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Bundle bundle = new Bundle();
            bundle.putLong("groupId", groupId);
            NavHostFragment.findNavController(GroupInfoFragment.this).navigate(R.id.action_groupInfoFragment_to_chatFragment, bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}