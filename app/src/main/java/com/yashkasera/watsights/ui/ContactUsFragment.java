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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.yashkasera.watsights.R;

import java.util.Objects;

public class ContactUsFragment extends Fragment {
    TextInputEditText name, email, subject, message;

    public ContactUsFragment() {
    }

    public static ContactUsFragment newInstance(String param1, String param2) {
        return new ContactUsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        subject = view.findViewById(R.id.subject);
        message = view.findViewById(R.id.message);

        view.findViewById(R.id.send).setOnClickListener(v -> {
            if (check()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"yashkasera@icloud.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                String msg = message.getText().toString() + "\n\n" + name.getText().toString() + "\n" + email.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                try {
                    startActivity(Intent.createChooser(intent, "Send email using :"));
                    NavHostFragment.findNavController(ContactUsFragment.this).navigate(R.id.action_contactUsFragment_to_navigation_more_options);
                } catch (android.content.ActivityNotFoundException e) {
                    Snackbar.make(v, "No Email Client Found!!",
                            BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
            } else {
                Snackbar.make(v, "Please fill out all the fields before proceeding!",
                        BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private boolean check() {
        if (Objects.requireNonNull(name.getText()).length() == 0) return false;
        if (Objects.requireNonNull(email.getText()).length() == 0) return false;
        if (Objects.requireNonNull(subject.getText()).length() == 0) return false;
        return Objects.requireNonNull(message.getText()).length() != 0;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            NavHostFragment.findNavController(ContactUsFragment.this).navigate(R.id.action_contactUsFragment_to_navigation_more_options);
        return super.onOptionsItemSelected(item);
    }
}