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

package com.yashkasera.watsights.ui.deleted;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yashkasera.watsights.Repository;
import com.yashkasera.watsights.model.Media;
import com.yashkasera.watsights.model.People;

import java.util.List;

public class DeletedViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Media>> getMedia;
    private final LiveData<List<People>> getPeople;

    public DeletedViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        getMedia = repository.getDeletedMedia();
        getPeople = repository.getPeople();
    }

    public LiveData<List<Media>> getMedia() {
        return getMedia;
    }

    public LiveData<List<People>> getPeople() {
        return getPeople;
    }
}