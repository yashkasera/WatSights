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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.util.SharedPreferenceManager;

public class OtherFilterFragment extends Fragment {
    SwitchCompat phone, email, date, lengthy, red_emoji;
    TextView phone_helper, email_helper, date_helper, lengthy_helper, red_emoji_helper;
    SharedPreferenceManager sharedPreferenceManager;

    public OtherFilterFragment() {
        // Required empty public constructor
    }

    public static OtherFilterFragment newInstance(String param1, String param2) {
        return new OtherFilterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        date = view.findViewById(R.id.date);
        lengthy = view.findViewById(R.id.lengthy);
        red_emoji = view.findViewById(R.id.red_emoji);
        view.findViewById(R.id.phone_helper).setOnClickListener(v -> phone.setChecked(!phone.isChecked()));
        view.findViewById(R.id.email_helper).setOnClickListener(v -> email.setChecked(!email.isChecked()));
        view.findViewById(R.id.date_helper).setOnClickListener(v -> date.setChecked(!date.isChecked()));
        view.findViewById(R.id.lengthy_helper).setOnClickListener(v -> lengthy.setChecked(!lengthy.isChecked()));
        view.findViewById(R.id.red_emoji_helper).setOnClickListener(v -> red_emoji.setChecked(!red_emoji.isChecked()));
        phone.setChecked(sharedPreferenceManager.arePhoneEnabled());
        email.setChecked(sharedPreferenceManager.areEmailsEnabled());
        date.setChecked(sharedPreferenceManager.getIsDateEnabled());
        lengthy.setChecked(sharedPreferenceManager.getIsLengthy());
        red_emoji.setChecked(sharedPreferenceManager.getContainsRedEmoji());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferenceManager.setArePhoneEnabled(phone.isChecked());
        sharedPreferenceManager.setAreEmailEnabled(email.isChecked());
        sharedPreferenceManager.setIsDateEnabled(date.isChecked());
        sharedPreferenceManager.setIsLengthy(lengthy.isChecked());
        sharedPreferenceManager.setContainsRedEmoji(red_emoji.isChecked());
    }
}