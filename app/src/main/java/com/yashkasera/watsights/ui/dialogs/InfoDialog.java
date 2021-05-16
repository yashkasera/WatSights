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

package com.yashkasera.watsights.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yashkasera.watsights.R;

public class InfoDialog extends BottomSheetDialogFragment {
    static String title1;
    static String message1;
    static String positiveButtonText;
    Button positiveButton;
    TextView title, message;

    public static InfoDialog newInstance(Bundle bundle) {
        title1 = bundle.getString("title");
        message1 = bundle.getString("message");
        positiveButtonText = bundle.getString("button");
        return new InfoDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        positiveButton = view.findViewById(R.id.positive);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        title.setText(title1);
        message.setText(message1);
        if (positiveButtonText != null) {
            positiveButton.setText(positiveButtonText);
        }
        positiveButton.setOnClickListener(v -> dismiss());
    }


}
