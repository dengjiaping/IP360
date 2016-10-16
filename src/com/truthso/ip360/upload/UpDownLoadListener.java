package com.truthso.ip360.upload;

public interface UpDownLoadListener {
        void onProgress(long currentSize, long totalSize);
        void onSuccess();
        void onFailure();
}
