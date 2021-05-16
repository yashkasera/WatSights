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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.yashkasera.watsights.R;

public class MoreOptionsFragment extends Fragment {

    public static MoreOptionsFragment newInstance() {
        return new MoreOptionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.status_downloader).setOnClickListener(v -> NavHostFragment.findNavController(MoreOptionsFragment.this).navigate(R.id.action_navigation_more_options_to_statusFragment));
        view.findViewById(R.id.deleted_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MoreOptionsFragment.this).navigate(R.id.action_navigation_more_options_to_navigation_deleted);
            }
        });
        view.findViewById(R.id.faqs).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("page", 0);
            NavHostFragment.findNavController(MoreOptionsFragment.this).navigate(R.id.action_navigation_more_options_to_faqFragment, bundle);
        });
        view.findViewById(R.id.privacy_policy).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("page", 1);
            NavHostFragment.findNavController(MoreOptionsFragment.this).navigate(R.id.action_navigation_more_options_to_faqFragment, bundle);
        });
        view.findViewById(R.id.terms_of_use).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("page", 2);
            NavHostFragment.findNavController(MoreOptionsFragment.this).navigate(R.id.action_navigation_more_options_to_faqFragment, bundle);
        });
        view.findViewById(R.id.contact_us).setOnClickListener(v -> NavHostFragment.findNavController(MoreOptionsFragment.this).navigate(R.id.action_navigation_more_options_to_contactUsFragment));
    }
}