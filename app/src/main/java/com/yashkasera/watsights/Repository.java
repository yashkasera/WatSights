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

package com.yashkasera.watsights;

import android.app.Application;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.yashkasera.watsights.database.DatabaseDao;
import com.yashkasera.watsights.database.WatSightsDatabase;
import com.yashkasera.watsights.model.EliteMessage;
import com.yashkasera.watsights.model.Group;
import com.yashkasera.watsights.model.GroupMember;
import com.yashkasera.watsights.model.ImportantMessage;
import com.yashkasera.watsights.model.Media;
import com.yashkasera.watsights.model.Message;
import com.yashkasera.watsights.model.MessageQuery;
import com.yashkasera.watsights.model.People;
import com.yashkasera.watsights.model.PinnedMessage;
import com.yashkasera.watsights.model.SpamMessage;
import com.yashkasera.watsights.util.Constants;
import com.yashkasera.watsights.util.NotificationBuilder;

import java.io.File;
import java.util.Date;
import java.util.List;


public class Repository {
    private static final String TAG = "Repository";
    public DatabaseDao databaseDao;
    Application application;

    public Repository(Application application) {
        this.application = application;
        databaseDao = WatSightsDatabase.getInstance(application).databaseDao();
    }

    public LiveData<List<Group>> getGroups() {
        return databaseDao.getGroups();
    }

    public LiveData<Group> getGroup(long id) {
        return databaseDao.getGroup(id);
    }

    public LiveData<List<Media>> getDeletedMedia() {
        return databaseDao.getDeletedMedia();
    }

    public void updateGroup(Group group) {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> databaseDao.updateGroup(group));
    }

    public void updatePerson(People person) {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> databaseDao.updatePerson(person));
    }

    public void deleteAllMessages() {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> {
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

    public LiveData<List<Message>> getMessages() {
        return databaseDao.getMessages();
    }

    public LiveData<List<MessageQuery>> getGroupMessages(long groupId) {
        return databaseDao.getGroupMessages(groupId);
    }

    public LiveData<List<MessageQuery>> getPersonMessages(long personId) {
        return databaseDao.getPersonMessages(personId);
    }

    public LiveData<List<MessageQuery>> getEliteMessages() {
        return databaseDao.getEliteMessages();
    }

    public LiveData<List<People>> getElitePeople() {
        return databaseDao.getElitePeople();
    }

    public LiveData<List<People>> getSpammers() {
        return databaseDao.getElitePeople();
    }

    public LiveData<List<People>> getMembers(long groupId) {
        return databaseDao.getMembers(groupId);
    }

    public Message getMessage(long messageId) {
        final Message[] message = new Message[1];
        WatSightsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                message[0] = databaseDao.getMessage(messageId);
            }
        });
        return message[0];
    }

    public People getPerson(long personId) {
        return databaseDao.getPerson(personId);
    }

    public LiveData<List<MessageQuery>> getSpamMessages() {
        return databaseDao.getSpamMessages();
    }

    public LiveData<List<MessageQuery>> getPinnedMessages() {
        return databaseDao.getPinnedMessages();
    }

    public void deletePinned(long id) {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> Log.d(TAG, "run: message unpinned" + databaseDao.deletePinned(id)));
    }

    public void resetElitePeople() {
        WatSightsDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                databaseDao.resetElitePeople();
            }
        });
    }

    public void resetSpammers() {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> databaseDao.resetSpammers());
    }

    public LiveData<List<MessageQuery>> getImportantMessages() {
        return databaseDao.getImportantMessages();
    }

    public void insertPinned(PinnedMessage pinnedMessage) {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> databaseDao.insertPinned(pinnedMessage));
    }

    public void addGroupMessage(String from, String message, boolean isImportant, boolean containsMention) {

        String groupName, personName;
        if (from.contains("(") && from.contains(" messages)")) {
            Log.e(TAG, "addGroupMessage: 1");
            groupName = from.substring(0, from.lastIndexOf('(')).trim();
        } else {
            Log.e(TAG, "addGroupMessage: 2");
            groupName = from.substring(0, from.lastIndexOf(':')).trim();
        }
        personName = from.substring(from.lastIndexOf(':') + 1).trim();
        Log.e(TAG, "addGroupMessage: " + groupName);
        WatSightsDatabase.databaseWriteExecutor.execute(() -> {
            long groupId, personId;
            if (databaseDao.getGroupByName(groupName) != null) {
                Group group = databaseDao.getGroupByName(groupName);
                groupId = group.getId();
                if (!group.isStore_messages()) return;
                if (message.equals(databaseDao.getGroupLastMessage(groupId))) return;
                group.setLast_viewed(new Date().toString());
                databaseDao.updateGroup(group);
            } else {
                Log.d(TAG, "run: adding Group");
                groupId = databaseDao.insertGroup(new Group(groupName, new Date().toString(), getColor(), true, 0));
                if (groupId != -1)
                    Log.d(TAG, "run: group added =>" + groupId);
            }
            if (databaseDao.getPersonByName(personName) != null) {
                personId = databaseDao.getPersonByName(personName).getId();
            } else {
                Log.d(TAG, "run: adding person");
                personId = databaseDao.insertPerson(new People(personName, false, false, getColor()));
                if (personId != -1)
                    Log.d(TAG, "run: person added =>" + personId);
            }
            if (message.equalsIgnoreCase("\uD83D\uDCF7 Photo")) {
                Log.i(TAG, "run: photo received");
                File file = getLatestFilefromDir(Environment.getExternalStorageDirectory() + File.separator + "WatSights" + File.separator + "Whatsapp Deleted Images" + File.separator + "temp");
                if (file != null && file.exists()) {
                    Log.d(TAG, "run() returned: " + file.getAbsolutePath());
                    databaseDao.insertMedia(new Media(file.getName(), Constants.IMAGE, file.getAbsolutePath(), personId, groupId, false));
                }
            }
            Log.d(TAG, "run: databaseDao.getGroupLastMessage() =>" + databaseDao.getGroupLastMessage(groupId));
            if ((groupId != -1) && (personId != -1) && !message.equals(databaseDao.getGroupLastMessage(groupId))) {
                if (databaseDao.isMember(groupId, personId) == null) {
                    Log.d(TAG, "run: adding group member");
                    databaseDao.insertMember(new GroupMember(groupId, personId));
                }
                long messageId = databaseDao.insertMessage(new Message(message, groupId, personId, new Date().toString()));
                if (messageId != -1) {
                    Log.d(TAG, "run: message added at :id => " + messageId);
                }

                if (containsMention) {
                    if (PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext()).getBoolean("notification_mention", true)) {
                        new NotificationBuilder(application.getApplicationContext(), groupName + " : " + personName, message, messageId).showMentionNotification();
                    }
                }
                if ((databaseDao.getPerson(personId) != null && databaseDao.getPerson(personId).isSpammer())) {
                    databaseDao.insertSpam(new SpamMessage(messageId));
                    return;
                }
                if (isImportant) {
                    databaseDao.insertImportant(new ImportantMessage(messageId));
                    if (PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext()).getBoolean("notification_important", true)) {
                        new NotificationBuilder(application.getApplicationContext(), groupName + " : " + personName, message, messageId).showImportantNotification();
                    }
                }

                if ((databaseDao.getPerson(personId) != null && databaseDao.getPerson(personId).isElite())) {
                    databaseDao.insertElite(new EliteMessage(messageId));
                    new NotificationBuilder(application.getApplicationContext(), groupName + " : " + personName, message, messageId).showEliteNotification();
                }

            }
        });
    }

    private int getColor(){
        TypedArray colors = application.getApplicationContext().getResources().obtainTypedArray(R.array.loading_colors);
        int index = (int) (Math.random() * colors .length());
        int color = colors.getColor(index, Color.BLACK);
        Log.d(TAG, "getColor() returned: " + color);
        return color;
    }

    public void insertMedia(Media media) {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> databaseDao.insertMedia(media));
    }

    public void addPersonalMessage(String from, String message, boolean isImportant) {
        Log.d(TAG, "****addPersonalMessage****");
        WatSightsDatabase.databaseWriteExecutor.execute(() -> {
            long personId;
            if (databaseDao.getPersonByName(from) != null) {
                personId = databaseDao.getPersonByName(from).getId();
            } else {
                Log.d(TAG, "run: adding person");
                personId = databaseDao.insertPerson(new People(from, false, false, getColor()));
                if (personId != -1)
                    Log.d(TAG, "run: person added =>" + personId);
            }
            if (personId != -1) {
                Log.d(TAG, "run: person id =>" + personId);
                long id = databaseDao.insertMessage(new Message(message, -1, personId, new Date().toString()));
                Log.d(TAG, "run() returned: messageId=>" + id);
            }
        });

    }

    private File getLatestFilefromDir(String dirPath) {
        Log.d(TAG, "getLatestFilefromDir() returned: " + dirPath);
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            Log.i(TAG, "getLatestFilefromDir: files null");
            return null;
        }
        File lastMod = files[0];
        for (File file : files) {
            Log.d(TAG, "getLatestFilefromDir() returned: " + file.getName());
            if (lastMod.lastModified() < file.lastModified()) {
                lastMod = file;
            }
        }
        return lastMod;
    }

    public void updateMedia(String prevPath, String newPath) {
        WatSightsDatabase.databaseWriteExecutor.execute(() -> databaseDao.updateMedia(prevPath, newPath));
    }

    public LiveData<List<People>> getPeople() {
        return databaseDao.getPeople();
    }
}
