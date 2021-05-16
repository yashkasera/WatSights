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

package com.yashkasera.watsights.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yashkasera.watsights.model.EliteMessage;
import com.yashkasera.watsights.model.Group;
import com.yashkasera.watsights.model.GroupMember;
import com.yashkasera.watsights.model.ImportantMessage;
import com.yashkasera.watsights.model.Media;
import com.yashkasera.watsights.model.Message;
import com.yashkasera.watsights.model.People;
import com.yashkasera.watsights.model.PinnedMessage;
import com.yashkasera.watsights.model.SpamMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Group.class,
        Message.class,
        EliteMessage.class,
        ImportantMessage.class,
        GroupMember.class,
        People.class,
        PinnedMessage.class,
        SpamMessage.class,
        Media.class
}, version = 1, exportSchema = false)

public abstract class WatSightsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "WatSightsDatabase";
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);
    public static volatile WatSightsDatabase INSTANCE = null;
    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                DatabaseDao databaseDao = INSTANCE.databaseDao();
                databaseDao.deleteElite();
                databaseDao.deleteGroups();
                databaseDao.deleteGroupMembers();
                databaseDao.deleteImportantMessages();
                databaseDao.deleteMessages();
                databaseDao.deletePeople();
                databaseDao.deletePinnedMessages();
                databaseDao.deleteSpamMessages();
                databaseDao.deleteMedia();
            });
        }
    };

    public static WatSightsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WatSightsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, WatSightsDatabase.class, DATABASE_NAME)
//                            .addCallback(callback)
                            .build();
//                            .fallbackToDestructiveMigration()
                }
            }
        }
        return INSTANCE;
    }

    public abstract DatabaseDao databaseDao();
}
