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

package com.yashkasera.watsights.ui.status;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.yashkasera.watsights.model.Status;
import com.yashkasera.watsights.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusViewModel extends AndroidViewModel {
    private static final String TAG = "StatusViewModel";
    private List<Status> list;

    public StatusViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Status> getStatus() {
        Log.d(TAG, "*****getStatus*****");
        list = new ArrayList<>();
        if (Constants.STATUS_DIRECTORY.exists()) {
            File[] files = Constants.STATUS_DIRECTORY.listFiles();
            if (files != null) {
                Log.d(TAG, "getStatus: " + files.length);
                Arrays.sort(files);
                for (File file : files) {
                    Log.d(TAG, "getStatus: " + file.getName());
                    if (!file.getName().equalsIgnoreCase(".nomedia")) {
                        Status status = new Status(file, file.getName(), file.getAbsolutePath());
                        status.setThumbnail(getThumbnail(status));
                        list.add(status);
                    }
                }
            } else {
                Toast.makeText(getApplication().getApplicationContext(), "Files is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication().getApplicationContext(), "Directory does not exist!", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    private Bitmap getThumbnail(Status status) {
        if (status.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(Status.getFile().getAbsolutePath(),
                    MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Status.getFile().getAbsolutePath()),
                    Constants.THUMB_SIZE,
                    Constants.THUMB_SIZE);
        }
    }

}
