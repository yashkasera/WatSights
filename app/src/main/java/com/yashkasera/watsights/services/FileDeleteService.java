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

package com.yashkasera.watsights.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.util.Log;

import java.io.File;

public class FileDeleteService extends JobService {

    private static final String TAG = "FileDeleteService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: Job Started");
        PersistableBundle bundle = params.getExtras();
        String path = bundle.getString("path");
        Log.i(TAG, "doInBackground: PATH=>" + path);
        File file = new File(path);
        if (file.exists()) {
            Log.i(TAG, "doInBackground: file exists");
            if (file.delete()) {
                Log.i(TAG, "run: file deleted");
                jobFinished(params, false);
            } else {
                Log.i(TAG, "run: File deletion failed! PATH=>" + path);
                jobFinished(params, true);
            }
        } else {
            Log.i(TAG, "doInBackground: File does not exist. PATH = >" + path);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: Job Cancelled before cancellation");
        return true;
    }
}
