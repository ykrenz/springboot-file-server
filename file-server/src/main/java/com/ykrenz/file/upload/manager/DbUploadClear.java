package com.ykrenz.file.upload.manager;

import com.ykrenz.file.model.CommonUtils;
import com.ykrenz.file.upload.storage.FileServerClient;
import com.ykrenz.file.lock.FileLock;
import com.ykrenz.file.upload.storage.model.UploadTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DbUploadClear implements FileUploadClear {

    private static final String CLEAR_LOCK_KEY = "FastDfsClearUploadTask";

    private final FileServerClient fileServerClient;

    private final FileLock fileLock;

    public DbUploadClear(FileServerClient fileServerClient, FileLock fileLock) {
        this.fileServerClient = fileServerClient;
        this.fileLock = fileLock;
    }

    @Override
    public void clear(int expireDays) {
        try {
            if (expireDays <= 0) {
                return;
            }
            if (fileLock.tryLock(CLEAR_LOCK_KEY)) {
                clearExpireUpload(expireDays);
            }
        } catch (Exception e) {
            log.error("clear part error", e);
        } finally {
            fileLock.unlock(CLEAR_LOCK_KEY);
        }
    }

    private void clearExpireUpload(int expireDays) {
        List<UploadTask> expireUploadModels = getExpireUploads(expireDays);
        while (!expireUploadModels.isEmpty()) {
            expireUploadModels.forEach(u -> {
                fileServerClient.abortMultipart(u.getUploadId());
            });
            expireUploadModels = getExpireUploads(expireDays);
        }
    }

    private List<UploadTask> getExpireUploads(int expireDays) {
        ListUploadParam request = new ListUploadParam();
        request.setPage(1);
        request.setLimit(100);
        List<UploadTask> uploadTasks = fileServerClient.listUpload();
        if (CollectionUtils.isEmpty(uploadTasks)) {
            return Collections.emptyList();
        }
        return uploadTasks.stream()
                .filter(o -> isExpire(expireDays, o))
                .collect(Collectors.toList());
    }

    private boolean isExpire(int expireDays, UploadTask task) {
        return CommonUtils.plusDays(task.getCreateTime(), expireDays) > Instant.now().toEpochMilli();
    }
}
