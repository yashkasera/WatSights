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

package com.yashkasera.watsights.ui.directMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yashkasera.watsights.R;
import com.yashkasera.watsights.adapter.CallLogAdapter;
import com.yashkasera.watsights.model.CallLog;
import com.yashkasera.watsights.util.ClickListener;
import com.yashkasera.watsights.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import static com.yashkasera.watsights.util.Functions.openWhatsApp;

public class CallLogFragment extends BottomSheetDialogFragment {
    private static final String TAG = "CallLogFragment";
    List<CallLog> logs;

    public CallLogFragment() {
    }

    public static CallLogFragment newInstance(String phone) {
        CallLogFragment fragment = new CallLogFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_call_log, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            logs = new ArrayList<>();
            readCallLogs();
            recyclerView.setAdapter(new CallLogAdapter(context, logs));
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent i = new Intent();
                    i.putExtra("phone", logs.get(position).getNumber());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                private final ColorDrawable bg_message = new ColorDrawable(ContextCompat.getColor(context, R.color.color_3));
                private final ColorDrawable bg_call = new ColorDrawable(ContextCompat.getColor(context, R.color.color_6));


                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    if (direction == ItemTouchHelper.LEFT) {
                        openWhatsApp(getContext(), logs.get(position).getNumber(), "");
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + logs.get(position).getNumber()));
                        startActivity(callIntent);
                    }
                    dismiss();
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    View itemView = viewHolder.itemView;
                    int itemHeight = itemView.getHeight();
                    int backgroundCornerOffset = 20;
                    if (dX > 0) {       //RIGHT SWIPE
                        bg_call.setBounds(itemView.getLeft(), itemView.getTop(),
                                itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                                itemView.getBottom());
                        bg_call.draw(c);
                        Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_call_outgoing);
                        icon.setTint(ContextCompat.getColor(context, R.color.white));
                        int intrinsicHeight = icon.getIntrinsicHeight();
                        int intrinsicWidth = icon.getIntrinsicWidth();
                        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int iconMargin = (itemHeight - intrinsicHeight) / 2;
                        int iconRight = itemView.getLeft() + iconMargin + intrinsicWidth;
                        int iconLeft = itemView.getLeft() + iconMargin;
                        int iconBottom = iconTop + intrinsicHeight;
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);

                    } else if (dX < 0) {    //LEFT SWIPE
                        bg_message.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        bg_message.draw(c);
                        Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_whatsapp);
                        icon.setTint(ContextCompat.getColor(context, R.color.white));
                        int intrinsicHeight = icon.getIntrinsicHeight();
                        int intrinsicWidth = icon.getIntrinsicWidth();
                        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int iconMargin = (itemHeight - intrinsicHeight) / 2;
                        int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconBottom = iconTop + intrinsicHeight;
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    } else { // view is unSwiped
                        bg_call.setBounds(0, 0, 0, 0);
                        bg_call.draw(c);
                        bg_message.setBounds(0, 0, 0, 0);
                        bg_message.draw(c);
                    }
                }
            }).attachToRecyclerView(recyclerView);
        }
        return view;
    }

    private void readCallLogs() {
        Uri uriCallLogs = Uri.parse("content://call_log/calls");
        Cursor cursorCallLogs = getContext().getContentResolver().query(uriCallLogs, null, null, null);
        cursorCallLogs.moveToFirst();
        do {
            String number = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            String name = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            long duration = cursorCallLogs.getLong(cursorCallLogs.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            long date = cursorCallLogs.getLong(cursorCallLogs.getColumnIndex(android.provider.CallLog.Calls.DATE));
            String type = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (!number.startsWith("+")) number = "+91" + number;
            logs.add(new CallLog(number, name, duration, type, date));

            Log.d(TAG, "onViewCreated() returned: " + "Number: " + number
                    + "\nName: " + name
                    + "\nDuration: " + duration
                    + "\n Type: " + type
                    + "\n\n");
        } while (cursorCallLogs.moveToNext());
    }
}