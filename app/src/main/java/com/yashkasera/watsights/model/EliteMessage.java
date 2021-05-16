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

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ELITE")
//, foreignKeys = @ForeignKey(entity = Message.class, parentColumns = "id", childColumns = "messageId")
public class EliteMessage {

    @PrimaryKey(autoGenerate = true)
    public long id;
    private long messageId;

    public EliteMessage(long messageId) {
        this.messageId = messageId;
    }

    public long getId() {
        return id;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
