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

package com.yashkasera.watsights.ui.pinned;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yashkasera.watsights.Repository;
import com.yashkasera.watsights.model.MessageQuery;

import java.util.List;

public class PinnedViewModel extends AndroidViewModel {
    private final Repository repository;
    private final Application application;
    public LiveData<List<MessageQuery>> list;

    public PinnedViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new Repository(application);
        list = repository.getPinnedMessages();
    }

    public LiveData<List<MessageQuery>> getList() {
        return list;
    }

    public void deletePinned(long messageId) {
        repository.deletePinned(messageId);
    }
}