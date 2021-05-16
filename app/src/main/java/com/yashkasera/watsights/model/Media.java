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

@Entity(tableName = "MEDIA")
public class Media {
    private final String name;
    private final int type;
    private final String path;
    private final long personId;
    private final long groupId;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private boolean isDeleted;

    public Media(String name, int type, String path, long personId, long groupId, boolean isDeleted) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.personId = personId;
        this.groupId = groupId;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public long getPersonId() {
        return personId;
    }

    public long getGroupId() {
        return groupId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
