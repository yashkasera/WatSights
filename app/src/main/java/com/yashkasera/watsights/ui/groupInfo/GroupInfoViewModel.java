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

package com.yashkasera.watsights.ui.groupInfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yashkasera.watsights.Repository;
import com.yashkasera.watsights.model.Group;
import com.yashkasera.watsights.model.People;

import java.util.List;

public class GroupInfoViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<People>> list;

    public GroupInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public Repository getRepository() {
        return repository;
    }

    public LiveData<Group> getGroup(long groupId) {
        return repository.getGroup(groupId);
    }

    public LiveData<List<People>> getMembers(long groupId) {
        return repository.getMembers(groupId);
    }

    public void updatePerson(People person) {
        repository.updatePerson(person);

    }

    public void updateGroup(Group group) {
        repository.updateGroup(group);

    }
}