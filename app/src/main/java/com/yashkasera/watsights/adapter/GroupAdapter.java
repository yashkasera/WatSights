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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.model.Group;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.yashkasera.watsights.util.Functions.isSameDay;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private static final String TAG = "GroupAdapter";
    Context context;
    List<Group> arrayList;

    public GroupAdapter(Context context, List<Group> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    public void setList(List<Group> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = arrayList.get(position);

        Log.d(TAG, "onBindViewHolder: date=>" + group.getLast_viewed());

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(group.getIcon());

        holder.icon.setBackground(draw);
        holder.icon.setText(String.valueOf(group.getName().charAt(0)));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        holder.name.setText(group.getName());
        Date date = new Date(group.getLast_viewed());
        holder.date.setText(isSameDay(date, new Date()) ? time.format(date) : dateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView icon;
        TextView name, summary, date;

        //        Chip number;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
//            summary = itemView.findViewById(R.id.summary);
            date = itemView.findViewById(R.id.date);
//            number = itemView.findViewById(R.id.number);
        }
    }

}
