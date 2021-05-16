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

package com.yashkasera.watsights.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yashkasera.watsights.Repository;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MessageReceived {
    private static final String TAG = "MessageReceived";
    private final Application application;
    private final String from;
    private final boolean isGroupMessage;
    private final String message;
    private final String temp;
    Repository repository;
    SharedPreferenceManager sharedPreferenceManager;
    boolean isBlackListed = false;

    public MessageReceived(Application application, String from, String message, boolean isGroupMessage) {
        this.application = application;
        this.from = from;
        this.message = message;
        temp = message.toLowerCase();
        this.isGroupMessage = isGroupMessage;
        sharedPreferenceManager = new SharedPreferenceManager(application);
    }

    boolean isValidMsg() {
        if (from.equals("WhatsApp"))
            return false;
        if (from.equals("WhatsApp Web"))
            return false;
        if (message.equals("Incoming voice call"))
            return false;
        if (message.equals("Missed voice call"))
            return false;
        if (message.equals("\uD83D\uDCF9 Incoming video call"))
            return false;
        if (message.contains("Incoming video call"))
            return false;
        if (message.matches("^[0-9]+ missed\\scalls$"))
            return false;
        if (from.matches("^Backup\\sin\\sprogress$"))
            return false;
        if (message.matches("^Preparing\\sbackup\\s\\([0-9]+%\\)$"))
            return false;
        if (message.equals("This message was deleted"))
            return false;
        if (message.matches("^Uploading:\\s[0-9]*\\.[0-9]+\\s[a-zA-Z]B\\sof\\s[0-9]*\\.[0-9]+ [a-zA-Z]B \\([0-9]+%\\)$"))
            return false;
        if (message.equals("\uD83D\uDC9F Sticker"))
            return false;
        return !from.matches("^Finished\\sbackup$") || !message.matches("^Tap\\sfor\\smore\\sinfo$");
    }

    boolean containsWord() {
        ArrayList<String> blacklist = new ArrayList<>(sharedPreferenceManager.getBlacklistedKeywords());
        for (String word : blacklist) {
            if (temp.contains(word.toLowerCase())) {
                isBlackListed = true;
                return false;
            }
        }
        ArrayList<String> whitelist = new ArrayList<>(sharedPreferenceManager.getWhitelistedKeywords());
        for (String word : whitelist) {
            if (temp.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    boolean containsLink() {
        if (temp.matches("^.*(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,}).*$")) {
            int code = sharedPreferenceManager.getFilterCode();
            if (code == SharedPreferenceManager.FILTER_ALL_LINKS) {
                return true;
            } else if (code == SharedPreferenceManager.FILTER_ALL_EXCEPT_BLACKLISTED) {
                ArrayList<String> blacklist = new ArrayList<>(sharedPreferenceManager.getBlacklistedLinks());
                for (String link : blacklist) {
                    if (temp.contains(link.toLowerCase())) {
                        isBlackListed = true;
                        return false;
                    }
                }
            } else if (code == SharedPreferenceManager.FILTER_ONLY_WHITELISTED_LINKS) {
                ArrayList<String> whitelist = new ArrayList<>(sharedPreferenceManager.getWhitelistedLinks());
                for (String link : whitelist) {
                    if (temp.contains(link.toLowerCase())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean containsNumber() {
        if (sharedPreferenceManager.arePhoneEnabled())
            return message.matches("^.*([0-9]{10})+.*$") || message.matches("^.*\\+([0-9]{12})+.*$") || message.matches("^.*\\+([0-9]{12})+.*[a-zA-Z]+.*$");
        return false;
    }

    boolean containsEmail() {
        if (sharedPreferenceManager.areEmailsEnabled())
            return temp.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
        return false;
    }

    boolean isLengthy() {
        if (sharedPreferenceManager.getIsLengthy())
            return message.length() >= 500;
        return false;
    }

    boolean containsRedDots() {
        if (sharedPreferenceManager.getContainsRedEmoji())
            return (message.contains("\u200B") || message.contains("\uD83D\uDED1") || message.contains("\uD83D\uDD34") || message.contains("\uD83C\uDD98"));
        return false;
    }

    boolean containsMention() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        String name = sharedPreferences.getString("user_name", null);
        if (name != null) {
            return temp.contains("@" + name.toLowerCase());
        }
        return false;
    }

    boolean containsDate() {
        if (sharedPreferenceManager.getIsDateEnabled()) {
            if (message.matches("^.*[0-9]{4}(/|-)(0[1-9]|1[0-2])(/|-)(0[1-9]|[1-2][0-9]|3[0-1]).*$"))
                return true;
            if (message.matches("^.*(0[1-9]|[1-2][0-9]|3[0-1])(/|-)(0[1-9]|1[0-2])(/|-)[0-9]{4}.*$"))
                return true;
            return message.matches("^.*[0-2][0-9]:[0-6][0-9]\\s?(A|P).*$");
        }
        return false;
    }

    public boolean isImportant() {
        return containsWord() || containsNumber() || containsLink() || containsEmail() || containsNumber() || isLengthy() || containsRedDots() || containsDate() || containsMention();
    }

    public void addMessage() throws ExecutionException {
        repository = new Repository(application);
        if (isValidMsg()) {
            boolean isImportant = isImportant();
            if (isBlackListed)
                isImportant = false;
            if (isGroupMessage) {
                repository.addGroupMessage(from, message, isImportant, containsMention());
            } else {
                repository.addPersonalMessage(from, message, isImportant);
            }
        }
    }
}
