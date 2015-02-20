package org.elf.framework.core.config;

import android.content.Context;
import android.os.Environment;

import org.elf.framework.core.util.FileUtils;

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