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

package com.yashkasera.watsights.ui.storage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.yashkasera.watsights.R;
import com.yashkasera.watsights.util.Constants;

import java.io.File;

public class StorageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "StorageFragment";
    long images, videos, documents, audio, gifs, voice, wallpaper, sticker, total;
    View view;
    Context context;
    private CardView c1, c2, c3, c4, c5, c6, c7, c8;
    private ProgressBar progressPhotos, progressVideos, progressAudio, progressGIF, progressDocuments, progressVoice, progressWallpapers, progressStickers;
    private TextView textViewPhotos, textViewVideos, textViewAudio, textViewGIF, textViewDocuments, textViewVoice, textViewWallpapers, textViewStickers;

    public static StorageFragment newInstance() {
        return new StorageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_storage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        this.view = view;
        findViewByIds(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetSizeAsyncTask().execute();
    }

    private void updateChart() {
        TextView textView = view.findViewById(R.id.pie_text);
        ProgressBar pie_total_used = view.findViewById(R.id.progress_total_used);
        ProgressBar pie_whatsapp_used = view.findViewById(R.id.progress_other_used);

        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        long totalStorage = totalBlocks * blockSize;
        long availableStorage = availableBlocks * blockSize;
        double total_used = (double) (totalStorage - availableStorage) / (double) totalStorage;
        double other_used = (double) (totalStorage - availableStorage - total) / (double) totalStorage;
        int total_used1 = (int) (total_used * 100);
        int other_used1 = (int) (other_used * 100);
        Log.d(TAG, "updateChart: total,whatsapp =>" + total_used1 + ", " + other_used1);
        pie_total_used.setProgress(total_used1);
        pie_whatsapp_used.setProgress(other_used1);
        textView.setText(String.format("%s/%s\n USED BY WHATSAPP", getFormattedSize(total), getFormattedSize(totalStorage)));
    }

    private String getFormattedSize(long sizeBytes) {
        return android.text.format.Formatter.formatShortFileSize(getContext(), sizeBytes);
    }

    private void findViewByIds(View view) {
        c1 = view.findViewById(R.id.c1);
        c2 = view.findViewById(R.id.c2);
        c3 = view.findViewById(R.id.c3);
        c4 = view.findViewById(R.id.c4);
        c5 = view.findViewById(R.id.c5);
        c6 = view.findViewById(R.id.c6);
        c7 = view.findViewById(R.id.c7);
        c8 = view.findViewById(R.id.c8);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        c7.setOnClickListener(this);
        c8.setOnClickListener(this);
        progressPhotos = view.findViewById(R.id.progressPhotos);
        textViewPhotos = view.findViewById(R.id.text1);
        progressVideos = view.findViewById(R.id.progressVideos);
        textViewVideos = view.findViewById(R.id.text2);
        progressAudio = view.findViewById(R.id.progressAudio);
        textViewAudio = view.findViewById(R.id.text3);
        progressGIF = view.findViewById(R.id.progressGIF);
        textViewGIF = view.findViewById(R.id.text5);
        progressDocuments = view.findViewById(R.id.progressDocuments);
        textViewDocuments = view.findViewById(R.id.text4);
        progressVoice = view.findViewById(R.id.progressVoice);
        textViewVoice = view.findViewById(R.id.text6);
        progressWallpapers = view.findViewById(R.id.progressWallpapers);
        textViewWallpapers = view.findViewById(R.id.text7);
        progressStickers = view.findViewById(R.id.progressStickers);
        textViewStickers = view.findViewById(R.id.text8);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.c1:
                bundle.putInt("type", Constants.IMAGE);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c2:
                bundle.putInt("type", Constants.VIDEO);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c3:
                bundle.putInt("type", Constants.AUDIO);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c4:
                bundle.putInt("type", Constants.DOCUMENT);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c5:
                bundle.putInt("type", Constants.GIF);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c6:
                bundle.putInt("type", Constants.VOICE);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c7:
                bundle.putInt("type", Constants.WALLPAPER);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
            case R.id.c8:
                bundle.putInt("type", Constants.STICKERS);
                NavHostFragment.findNavController(StorageFragment.this).navigate(R.id.action_navigation_storage_to_viewFilesFragment, bundle);
                break;
        }
    }

    class GetSizeAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Calculating space occupied by Whatsapp");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            images = folderSize(new File(Constants.IMAGES_RECEIVED_PATH));
            videos = folderSize(new File(Constants.VIDEOS_RECEIVED_PATH));
            documents = folderSize(new File(Constants.DOCUMENTS_RECEIVED_PATH));
            audio = folderSize(new File(Constants.AUDIO_RECEIVED_PATH));
            gifs = folderSize(new File(Constants.GIF_RECEIVED_PATH));
            voice = folderSize(new File(Constants.VOICE_RECEIVED_PATH));
            wallpaper = folderSize(new File(Constants.WALLPAPER_RECEIVED_PATH));
            sticker = folderSize(new File(Constants.STICKERS_PATH));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            total = images + videos + documents + audio + gifs + voice + wallpaper + sticker;
            updateChart();
            textViewPhotos.setText(getFormattedSize(images));
            textViewVideos.setText(getFormattedSize(videos));
            textViewDocuments.setText(getFormattedSize(documents));
            textViewAudio.setText(getFormattedSize(audio));
            textViewGIF.setText(getFormattedSize(gifs));
            textViewVoice.setText(getFormattedSize(voice));
            textViewWallpapers.setText(getFormattedSize(wallpaper));
            textViewStickers.setText(getFormattedSize(sticker));
            progressPhotos.setMax((int) (total / (1024 * 1024)));
            progressPhotos.setProgress((int) (images / (1024 * 1024)));
            progressVideos.setMax((int) (total / (1024 * 1024)));
            progressVideos.setProgress((int) (videos / (1024 * 1024)));
            progressDocuments.setMax((int) (total / (1024 * 1024)));
            progressDocuments.setProgress((int) (documents / (1024 * 1024)));
            progressAudio.setMax((int) (total / (1024 * 1024)));
            progressAudio.setProgress((int) (audio / (1024 * 1024)));
            progressGIF.setMax((int) (total / (1024 * 1024)));
            progressGIF.setProgress((int) (gifs / (1024 * 1024)));
            progressVoice.setMax((int) (total / (1024 * 1024)));
            progressVoice.setProgress((int) (voice / (1024 * 1024)));
            progressWallpapers.setMax((int) (total / (1024 * 1024)));
            progressWallpapers.setProgress((int) (wallpaper / (1024 * 1024)));
            progressStickers.setMax((int) (total / (1024 * 1024)));
            progressStickers.setProgress((int) (sticker / (1024 * 1024)));
            progressDialog.dismiss();
        }

        public long folderSize(File directory) {
            long length = 0;
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
            return length;
        }
    }
}