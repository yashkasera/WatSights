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

package com.yashkasera.watsights.ui.other;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.yashkasera.watsights.R;

import org.jetbrains.annotations.NotNull;

public class OtherFragments extends Fragment {
    int page;

    public OtherFragments() {
    }

    public static OtherFragments newInstance() {
        return new OtherFragments();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        page = getArguments().getInt("page");
        if (page == 0)
            return inflater.inflate(R.layout.fragment_faq, container, false);
        else if (page == 1)
            return inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        else
            return inflater.inflate(R.layout.fragment_terms_of_use, container, false);
    }

    private void viewSite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        view.findViewById(R.id.read).setOnClickListener(v -> {
            if (page == 0) {
                viewSite("https://yashkasera.github.io/WatSights");
            } else if (page == 1) {
                viewSite("https://yashkasera.github.io/WatSights/privacy_policy.html");
            } else {
                viewSite("https://yashkasera.github.io/WatSights/terms_of_use.html");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment.findNavController(OtherFragments.this).navigate(R.id.action_faqFragment_to_navigation_more_options);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}