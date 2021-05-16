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

package com.yashkasera.watsights.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.SimpleListItemAdapter;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.RecyclerTouchListener;
import com.yashkasera.watsights.util.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KeywordsFragment extends Fragment {


    RecyclerView recyclerView_white, recyclerView_black;
    SimpleListItemAdapter simpleListItemAdapter1, simpleListItemAdapter2;
    ArrayList<String> listBlack, listWhite;
    SharedPreferenceManager sharedPreferenceManager;
    SwitchCompat enable;
    String temp;

    public KeywordsFragment() {
    }

    public static KeywordsFragment newInstance() {

        return new KeywordsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keywords, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        listWhite = new ArrayList<>();
        listBlack = new ArrayList<>();
        whitelist(view);
        blacklist(view);
        enable = view.findViewById(R.id.enable);
        enable.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        enable.setChecked(sharedPreferenceManager.areKeywordsEnabled());
        if (sharedPreferenceManager.areKeywordsEnabled()) linearLayout.setVisibility(View.VISIBLE);
        else linearLayout.setVisibility(View.GONE);
        enable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayout.setVisibility(View.VISIBLE);
                sharedPreferenceManager.setAreKeywordsEnabled(true);
            } else {
                linearLayout.setVisibility(View.GONE);
                sharedPreferenceManager.setAreKeywordsEnabled(false);
            }
        });
        view.findViewById(R.id.add).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add new keyword");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_keyword, (ViewGroup) getView(), false);
            final TextInputEditText textInputEditText = viewInflated.findViewById(R.id.textInputEditText);
            final TextInputLayout textInputLayout = viewInflated.findViewById(R.id.textInputLayout);
            ChipGroup chipGroup = viewInflated.findViewById(R.id.chipGroup);
            builder.setView(viewInflated);
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                if (textInputEditText.getText().length() > 0) {
                    textInputLayout.setError(null);
                    if (chipGroup.getCheckedChipId() == R.id.white) {
                        listWhite.add(0, textInputEditText.getText().toString().toLowerCase());
                        simpleListItemAdapter1.notifyItemInserted(0);
                    } else {
                        listBlack.add(0, textInputEditText.getText().toString().toLowerCase());
                        simpleListItemAdapter2.notifyItemInserted(0);
                    }
                    dialog.dismiss();
                } else {
                    textInputLayout.setError("Cannot be empty!");
                }
            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }

    void whitelist(View view) {
        recyclerView_white = view.findViewById(R.id.whitelist);
        simpleListItemAdapter1 = new SimpleListItemAdapter(getContext(), listWhite);
        if (sharedPreferenceManager.getWhitelistedKeywords() != null)
            listWhite.addAll(sharedPreferenceManager.getWhitelistedKeywords());
        recyclerView_white.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView_white.setAdapter(simpleListItemAdapter1);
        recyclerView_white.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView_white, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(listWhite.get(position))
                        .setPositiveButton("Remove", (dialog, which) -> {
                            temp = listWhite.get(position);
                            listWhite.remove(position);
                            simpleListItemAdapter1.notifyItemRemoved(position);
                            Snackbar.make(view, temp + " removed successfully",
                                    BaseTransientBottomBar.LENGTH_SHORT)
                                    .setAction("Undo", v -> {
                                        listWhite.add(temp);
                                        simpleListItemAdapter1.notifyItemInserted(position);
                                    }).show();
                        })
                        .setNegativeButton("Blacklist", (dialog, which) -> {
                            String str = listWhite.get(position);
                            listWhite.remove(position);
                            listBlack.add(0, str);
                            simpleListItemAdapter1.notifyItemRemoved(position);
                            simpleListItemAdapter2.notifyItemInserted(0);
                        })
                        .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    void blacklist(View view) {
        recyclerView_black = view.findViewById(R.id.blacklist);
        simpleListItemAdapter2 = new SimpleListItemAdapter(getContext(), listBlack);
        if (sharedPreferenceManager.getBlacklistedKeywords() != null)
            listBlack.addAll(sharedPreferenceManager.getBlacklistedKeywords());
        recyclerView_black.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView_black.setAdapter(simpleListItemAdapter2);
        recyclerView_black.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView_black, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(listBlack.get(position))
                        .setPositiveButton("Remove", (dialog, which) -> {
                            temp = listBlack.get(position);
                            listBlack.remove(position);
                            simpleListItemAdapter2.notifyItemRemoved(position);
                            Snackbar.make(view, temp + " removed successfully",
                                    BaseTransientBottomBar.LENGTH_SHORT)
                                    .setAction("Undo", v -> {
                                        listBlack.add(temp);
                                        simpleListItemAdapter2.notifyItemInserted(position);
                                    }).show();

                        })
                        .setNegativeButton("Whitelist", (dialog, which) -> {
                            String str = listBlack.get(position);
                            listBlack.remove(position);
                            listWhite.add(0, str);
                            simpleListItemAdapter2.notifyItemRemoved(position);
                            simpleListItemAdapter1.notifyItemInserted(0);
                        })
                        .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onDestroyView() {
        updateBlacklisted();
        updateWhitelisted();
        super.onDestroyView();
    }

    private void updateWhitelisted() {
        Set set = new HashSet<>();
        set.addAll(listWhite);
        sharedPreferenceManager.setWhitelistedKeywords(set);
    }

    private void updateBlacklisted() {
        Set set = new HashSet<>();
        set.addAll(listBlack);
        sharedPreferenceManager.setBlacklistedKeywords(set);
    }
}