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

package com.yashkasera.watsights.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleListItemAdapter extends RecyclerView.Adapter<SimpleListItemAdapter.SimpleListViewHolder> {
    Context context;
    ArrayList<String> arrayList;

    public SimpleListItemAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SimpleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimpleListViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleListViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SimpleListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SimpleListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
//
//    public void addItem(String string) {
//        list.add(0, string);
//        notifyItemInserted(0);
//    }
//
//    public void removeItem(String string) {
//        int position = list.indexOf(string);
//        list.remove(string);
//        notifyItemRemoved(position);
//    }
}
