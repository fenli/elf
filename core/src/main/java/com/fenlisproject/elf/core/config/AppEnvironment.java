/*
* Copyright (C) 2015 Steven Lewi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.fenlisproject.elf.core.config;

import android.content.Context;
import android.os.Environment;

import com.fenlisproject.elf.core.util.FileUtils;

public class AppEnvironment {

    /* Private Directory */
    private final String mPrivateFilesDirectory;
    private final String mPrivateImagesDirectory;
    private final String mPrivateDatabaseDirectory;
    private final String mPrivateCacheDirectory;
    private final String mPrivateTempDirectory;

    /* Public Directory */
    private String mPublicFilesDirectory;
    private String mPublicImagesDirectory;
    private String mPublicDatabaseDirectory;
    private String mPublicCacheDirectory;
    private String mPublicTempDirectory;

    public AppEnvironment(Context context) {
        mPrivateFilesDirectory = context.getFilesDir().getAbsolutePath();
        mPrivateDatabaseDirectory = mPrivateFilesDirectory.replaceAll("files$", "databases");
        mPrivateImagesDirectory = mPrivateFilesDirectory + "/images";
        mPrivateCacheDirectory = context.getCacheDir().getAbsolutePath();
        mPrivateTempDirectory = mPrivateFilesDirectory + "/tmp";
        FileUtils.createDirsIfNotExist(mPrivateImagesDirectory);
        FileUtils.createDirsIfNotExist(mPrivateTempDirectory);

        if (isPublicDirectoryAvailable() && context.getExternalFilesDir(null) != null) {
            mPublicFilesDirectory = context.getExternalFilesDir(null).getAbsolutePath();
            mPublicDatabaseDirectory = context.getExternalFilesDir(null)
                    .getAbsolutePath().replaceAll("files$", "databases");
            mPublicImagesDirectory = context.getExternalFilesDir("images").getAbsolutePath();
            mPublicCacheDirectory = context.getExternalCacheDir().getAbsolutePath();
            mPublicTempDirectory = context.getExternalFilesDir("tmp").getAbsolutePath();
            FileUtils.createDirsIfNotExist(mPublicDatabaseDirectory);
        }
    }

    public boolean isPublicDirectoryAvailable() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public String getPrivateFilesDirectory() {
        return mPrivateFilesDirectory;
    }

    public String getPublicFilesDirectory() {
        return mPublicFilesDirectory;
    }

    public String getPrivateImagesDirectory() {
        return mPrivateImagesDirectory;
    }

    public String getPublicImagesDirectory() {
        return mPublicImagesDirectory;
    }

    public String getPrivateDatabaseDirectory() {
        return mPrivateDatabaseDirectory;
    }

    public String getPublicDatabaseDirectory() {
        return mPublicDatabaseDirectory;
    }

    public String getPrivateCacheDirectory() {
        return mPrivateCacheDirectory;
    }

    public String getPublicCacheDirectory() {
        return mPublicCacheDirectory;
    }

    public String getPrivateTempDirectory() {
        return mPrivateTempDirectory;
    }

    public String getPublicTempDirectory() {
        return mPublicTempDirectory;
    }
}