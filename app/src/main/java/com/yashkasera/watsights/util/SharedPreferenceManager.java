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

import android.content.Context;
import android.content.SharedPreferences;

import com.yashkasera.watsights.R;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceManager {
    public static final String PREFERENCE_NAME = "WatSights-Preferences";
    public static final String IS_FIRST_TIME_LAUNCH = "isFirstLaunch";
    public static final String WHITELISTED_KEYWORDS = "importantKeywords";
    public static final String BLACKLISTED_KEYWORDS = "blacklistedKeywords";
    public static final String WHITELISTED_LINKS = "whitelistedLinks";
    public static final String BLACKLISTED_LINKS = "blacklistedLinks";
    public static final String ARE_KEYWORDS_ENABLED = "enabledKeywords";
    public static final String ARE_EMAIL_ENABLED = "enabledEmails";
    public static final String ARE_PHONE_ENABLED = "enabledPhone";
    public static final String IS_DATE_ENABLED = "enabledDate";
    public static final String IS_LENGTHY = "isLengthy";
    public static final String CONTAINS_RED_EMOJI = "containsEmoji";
    public static final String LINK_FILTER = "filterLinksBy";
    public static final int FILTER_ALL_LINKS = 0;
    public static final int FILTER_ALL_EXCEPT_BLACKLISTED = 1;
    public static final int FILTER_ONLY_WHITELISTED_LINKS = 2;
    public static final int FILTER_NO_LINKS = 3;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public SharedPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public Set<String> getWhitelistedKeywords() {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, context.getResources().getStringArray(R.array.important_words));
        return sharedPreferences.getStringSet(WHITELISTED_KEYWORDS, set);
    }

    public void setWhitelistedKeywords(Set<String> set) {
        editor.putStringSet(WHITELISTED_KEYWORDS, set);
        editor.apply();
    }

    public Set<String> getBlacklistedKeywords() {
        return sharedPreferences.getStringSet(BLACKLISTED_KEYWORDS, new HashSet<>());
    }

    public void setBlacklistedKeywords(Set<String> set) {
        editor.putStringSet(BLACKLISTED_KEYWORDS, set);
        editor.apply();
    }

    public Set<String> getWhitelistedLinks() {
        return sharedPreferences.getStringSet(WHITELISTED_LINKS, new HashSet<>());
    }

    public void setWhitelistedLinks(Set<String> set) {
        editor.putStringSet(WHITELISTED_LINKS, set);
        editor.apply();
    }

    public Set<String> getBlacklistedLinks() {
        return sharedPreferences.getStringSet(BLACKLISTED_LINKS, new HashSet<>());
    }

    public void setBlacklistedLinks(Set<String> set) {
        editor.putStringSet(BLACKLISTED_LINKS, set);
        editor.apply();
    }

    public boolean areKeywordsEnabled() {
        return sharedPreferences.getBoolean(ARE_KEYWORDS_ENABLED, true);
    }

    public int getFilterCode() {
        return sharedPreferences.getInt(LINK_FILTER, FILTER_ALL_LINKS);
    }

    public boolean areEmailsEnabled() {
        return sharedPreferences.getBoolean(ARE_EMAIL_ENABLED, true);
    }

    public boolean arePhoneEnabled() {
        return sharedPreferences.getBoolean(ARE_PHONE_ENABLED, true);
    }

    public void setAreKeywordsEnabled(boolean enabled) {
        editor.putBoolean(ARE_KEYWORDS_ENABLED, enabled);
        editor.commit();
    }

    public void setLinkFilterCode(int code) {
        editor.putInt(LINK_FILTER, code);
        editor.commit();
    }

    public void setArePhoneEnabled(boolean enabled) {
        editor.putBoolean(ARE_PHONE_ENABLED, enabled);
        editor.commit();
    }

    public void setAreEmailEnabled(boolean enabled) {
        editor.putBoolean(ARE_EMAIL_ENABLED, enabled);
        editor.commit();
    }

    public boolean getIsDateEnabled() {
        return sharedPreferences.getBoolean(IS_DATE_ENABLED, true);
    }

    public void setIsDateEnabled(boolean enabled) {
        editor.putBoolean(IS_DATE_ENABLED, enabled);
        editor.commit();
    }

    public boolean getIsLengthy() {
        return sharedPreferences.getBoolean(IS_LENGTHY, true);
    }

    public void setIsLengthy(boolean enabled) {
        editor.putBoolean(IS_LENGTHY, enabled);
        editor.commit();
    }

    public boolean getContainsRedEmoji() {
        return sharedPreferences.getBoolean(CONTAINS_RED_EMOJI, true);
    }

    public void setContainsRedEmoji(boolean enabled) {
        editor.putBoolean(CONTAINS_RED_EMOJI, enabled);
        editor.commit();
    }

}
