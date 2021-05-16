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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.model.Status;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    Context context;
    List<Status> list;

    public StatusAdapter(Context context, List<Status> list) {
        this.context = context;
        this.list = list;
    }

    public void addItem(Status status) {
        list.add(list.size(), status);
        notifyItemInserted(list.size());
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        if (list.get(position).isVideo()) {
            holder.imageView.setImageBitmap(list.get(position).getThumbnail());
            holder.play.setVisibility(View.VISIBLE);
        } else {
            Glide.with(context)
                    .load(list.get(position).getPath())
                    .transition(withCrossFade())
                    .centerCrop()
                    .thumbnail(Glide.with(context)
                            .load(R.drawable.ic_baseline_photo_24))
                    .into(holder.imageView);
            holder.play.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView play;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            play = itemView.findViewById(R.id.play);

        }
    }
}
