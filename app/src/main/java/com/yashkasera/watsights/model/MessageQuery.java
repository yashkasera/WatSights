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

package com.yashkasera.watsights.model;

public class MessageQuery {
    long id;
    String message;
    String personName;
    String groupName;
    boolean elite;
    boolean spammer;
    String timestamp;

    public MessageQuery(long id, String message, String personName, String groupName, boolean elite, boolean spammer, String timestamp) {
        this.id = id;
        this.message = message;
        this.personName = personName;
        this.groupName = groupName;
        this.elite = elite;
        this.spammer = spammer;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getPersonName() {
        return personName;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isElite() {
        return elite;
    }

    public boolean isSpammer() {
        return spammer;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
