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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.model.CallLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yashkasera.watsights.util.Functions.isSameDay;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private final List<CallLog> list;
    private final Context context;

    public CallLogAdapter(Context context, List<CallLog> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(list.get(position).getName() == null) holder.number.setText(list.get(position).getNumber());
        else holder.number.setText(list.get(position).getName());
        Date date = new Date(list.get(position).getDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        holder.date.setText(simpleDateFormat.format(date));
        if(position == 0) {
            holder.date.setVisibility(View.VISIBLE);
        } else if (isSameDay(new Date(list.get(position).getDate()), new Date(list.get(position - 1).getDate()))) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setVisibility(View.VISIBLE);
        }

        long seconds = list.get(position).getDuration();
        long minutes = TimeUnit.SECONDS
                .toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        holder.time.setText(sdf.format(date));
        holder.duration.setText(String.format("%d mins %d secs", minutes, seconds));

        switch (list.get(position).getType()) {
            case android.provider.CallLog.Calls.INCOMING_TYPE:
                holder.type.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_call_incoming));
                break;
            case android.provider.CallLog.Calls.OUTGOING_TYPE:
                holder.type.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_call_outgoing));
                break;
            case android.provider.CallLog.Calls.MISSED_TYPE:
                holder.type.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_call_missed));
                break;
            case android.provider.CallLog.Calls.VOICEMAIL_TYPE:
                holder.type.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_call_voicemail));
                break;
            case android.provider.CallLog.Calls.REJECTED_TYPE:
            case android.provider.CallLog.Calls.BLOCKED_TYPE:
            default:
                holder.type.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_call_cancelled));
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView type;
        public final TextView number;
        public final TextView date;
        public final TextView duration;
        public final TextView time;
        public final RelativeLayout log;

        public ViewHolder(View view) {
            super(view);
            type = view.findViewById(R.id.type);
            number = view.findViewById(R.id.number);
            date = view.findViewById(R.id.date);
            duration = view.findViewById(R.id.duration);
            time = view.findViewById(R.id.time);
            log = view.findViewById(R.id.log);
        }
    }
}