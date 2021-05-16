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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.MemberAdapter;
import com.yashkasera.watsights.model.People;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class EliteMembersFragment extends BottomSheetDialogFragment {

    private static final String TAG = "EliteMembersFragment";
    RecyclerView recyclerView;
    List<People> list;
    MemberAdapter memberAdapter;
    private EliteMembersViewModel mViewModel;

    public static EliteMembersFragment newInstance() {
        return new EliteMembersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliteMembersViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        memberAdapter = new MemberAdapter(getContext(), list);
        mViewModel.getList().observe(this, messages -> {
            list = messages;
            if (list.size() == 0) {
                view.findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.no_results).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(memberAdapter);
                memberAdapter.setList(list);
            }
        });
        recyclerView.setAdapter(memberAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent();
                i.putExtra("personId", list.get(position).getId());
                i.putExtra("personName", list.get(position).getName());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}