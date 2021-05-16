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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.model.FileDetails;
import com.yashkasera.watsights.ui.ViewFilesFragment;
import com.yashkasera.watsights.util.Constants;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    public List<FileDetails> fileList;
    Context context;
    boolean isLinear;
    int type;
    ViewFilesFragment viewFilesFragment;

    public FileAdapter() {
    }

    public FileAdapter(List<FileDetails> fileList, Context context, boolean isLinear, int type, ViewFilesFragment viewFilesFragment) {
        this.type = type;
        this.isLinear = isLinear;
        this.fileList = fileList;
        this.context = context;
        this.viewFilesFragment = viewFilesFragment;
    }

    public void removeItem(FileDetails fileDetails) {
        int position;
        position = fileList.indexOf(fileDetails);
        fileList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isLinear)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.checkBox.setChecked(fileList.get(position).isSelected());
        if (type == Constants.IMAGE || type == Constants.GIF || type == Constants.STICKERS || type == Constants.WALLPAPER || type == Constants.VIDEO) {
            if (type == Constants.VIDEO || type == Constants.GIF)
                holder.play.setVisibility(View.VISIBLE);
            else holder.play.setVisibility(View.GONE);
            holder.title.setText(fileList.get(position).getName());
            holder.size.setText(fileList.get(position).getSize());
            Glide.with(context)
                    .load(fileList.get(position).getPath())
                    .transition(withCrossFade())
                    .into(holder.imageView);
        } else if (type == Constants.DOCUMENT) {
            holder.title.setText(fileList.get(position).getName());
            holder.title.setVisibility(View.VISIBLE);
            holder.size.setText(fileList.get(position).getSize());
            holder.size.setVisibility(View.VISIBLE);
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_picture_as_pdf_24));
            holder.imageView.setScaleX(0.5f);
            holder.imageView.setScaleY(0.5f);
        } else if (type == Constants.AUDIO || type == Constants.VOICE) {
            holder.play.setVisibility(View.VISIBLE);
            holder.title.setText(fileList.get(position).getName());
            holder.title.setVisibility(View.VISIBLE);
            holder.size.setText(fileList.get(position).getSize());
            holder.size.setVisibility(View.VISIBLE);
            holder.imageView.setImageDrawable(null);
            holder.imageView.setScaleX(0.5f);
            holder.imageView.setScaleY(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public List<FileDetails> getList() {
        return fileList;
    }

    public void setList(List<FileDetails> files) {
        fileList = files;
        notifyDataSetChanged();
    }


    public class FileViewHolder extends RecyclerView.ViewHolder {
        TextView title, size;
        ImageView imageView, play;
        CheckBox checkBox;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            size = itemView.findViewById(R.id.size);
            imageView = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);
            play = itemView.findViewById(R.id.play);
        }
    }

}
