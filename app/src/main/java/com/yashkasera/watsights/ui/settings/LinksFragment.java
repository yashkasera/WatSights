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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class LinksFragment extends Fragment {


    RecyclerView recyclerView_white, recyclerView_black;
    SimpleListItemAdapter simpleListItemAdapter1, simpleListItemAdapter2;
    ArrayList<String> listBlack, listWhite, tempList1, tempList2;
    SharedPreferenceManager sharedPreferenceManager;
    RadioGroup radioGroup;
    private String temp;

    public LinksFragment() {
        // Required empty public constructor
    }

    public static LinksFragment newInstance() {
        return new LinksFragment();
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
        tempList1 = new ArrayList<>();
        tempList2 = new ArrayList<>();
        recyclerView_white = view.findViewById(R.id.whitelist);
        recyclerView_black = view.findViewById(R.id.blacklist);
        whitelist(view);
        blacklist(view);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        if (sharedPreferenceManager.getFilterCode() == SharedPreferenceManager.FILTER_ALL_LINKS) {
            linearLayout.setVisibility(View.VISIBLE);
            view.findViewById(R.id.all1).setVisibility(View.VISIBLE);
            view.findViewById(R.id.all2).setVisibility(View.GONE);
            recyclerView_white.setVisibility(View.GONE);
            recyclerView_black.setVisibility(View.GONE);
            RadioButton radioButton = view.findViewById(R.id.FILTER_ALL_LINKS);
            radioButton.setChecked(true);
        } else if (sharedPreferenceManager.getFilterCode() == SharedPreferenceManager.FILTER_ONLY_WHITELISTED_LINKS) {
            linearLayout.setVisibility(View.VISIBLE);
            view.findViewById(R.id.all1).setVisibility(View.GONE);
            view.findViewById(R.id.all2).setVisibility(View.VISIBLE);
            recyclerView_white.setVisibility(View.VISIBLE);
            recyclerView_black.setVisibility(View.GONE);
            RadioButton radioButton = view.findViewById(R.id.FILTER_ONLY_WHITELISTED_LINKS);
            radioButton.setChecked(true);
        } else if (sharedPreferenceManager.getFilterCode() == SharedPreferenceManager.FILTER_ALL_EXCEPT_BLACKLISTED) {
            linearLayout.setVisibility(View.VISIBLE);
            view.findViewById(R.id.all1).setVisibility(View.VISIBLE);
            view.findViewById(R.id.all2).setVisibility(View.GONE);
            recyclerView_white.setVisibility(View.GONE);
            recyclerView_black.setVisibility(View.VISIBLE);
            RadioButton radioButton = view.findViewById(R.id.FILTER_ALL_EXCEPT_BLACKLISTED);
            radioButton.setChecked(true);
        } else {
            linearLayout.setVisibility(View.GONE);
            RadioButton radioButton = view.findViewById(R.id.FILTER_NO_LINKS);
            radioButton.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.FILTER_ALL_LINKS:
                    linearLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.all1).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.all2).setVisibility(View.GONE);
                    recyclerView_white.setVisibility(View.GONE);
                    recyclerView_black.setVisibility(View.GONE);
                    sharedPreferenceManager.setLinkFilterCode(SharedPreferenceManager.FILTER_ALL_LINKS);
                    break;
                case R.id.FILTER_ALL_EXCEPT_BLACKLISTED:
                    linearLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.all1).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.all2).setVisibility(View.GONE);
                    recyclerView_white.setVisibility(View.GONE);
                    recyclerView_black.setVisibility(View.VISIBLE);
                    sharedPreferenceManager.setLinkFilterCode(SharedPreferenceManager.FILTER_ALL_EXCEPT_BLACKLISTED);
                    break;
                case R.id.FILTER_ONLY_WHITELISTED_LINKS:
                    linearLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.all1).setVisibility(View.GONE);
                    view.findViewById(R.id.all2).setVisibility(View.VISIBLE);
                    recyclerView_white.setVisibility(View.VISIBLE);
                    recyclerView_black.setVisibility(View.GONE);
                    sharedPreferenceManager.setLinkFilterCode(SharedPreferenceManager.FILTER_ONLY_WHITELISTED_LINKS);

                    break;
                case R.id.FILTER_NO_LINKS:
                    linearLayout.setVisibility(View.GONE);
                    sharedPreferenceManager.setLinkFilterCode(SharedPreferenceManager.FILTER_NO_LINKS);
                    break;
            }
        });
        view.findViewById(R.id.add).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add new Link");
            builder.setMessage("Only the domain of the website you want to add");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_keyword, (ViewGroup) getView(), false);
            final TextInputEditText textInputEditText = viewInflated.findViewById(R.id.textInputEditText);
            final TextInputLayout textInputLayout = viewInflated.findViewById(R.id.textInputLayout);
            textInputLayout.setHint("example.com");
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
        simpleListItemAdapter1 = new SimpleListItemAdapter(getContext(), listWhite);
        if (sharedPreferenceManager.getWhitelistedLinks() != null)
            listWhite.addAll(sharedPreferenceManager.getWhitelistedLinks());
        recyclerView_white.setLayoutManager(new LinearLayoutManager(getContext()));
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
        simpleListItemAdapter2 = new SimpleListItemAdapter(getContext(), listBlack);
        if (sharedPreferenceManager.getBlacklistedLinks() != null)
            listBlack.addAll(sharedPreferenceManager.getBlacklistedLinks());
        recyclerView_black.setLayoutManager(new LinearLayoutManager(getContext()));
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
        Set set = new HashSet<>(listWhite);
        sharedPreferenceManager.setWhitelistedLinks(set);
    }

    private void updateBlacklisted() {
        Set set = new HashSet<>(listBlack);
        sharedPreferenceManager.setBlacklistedLinks(set);
    }
}