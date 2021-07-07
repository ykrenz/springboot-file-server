package com.github.ren.file.client.s3;

import com.amazonaws.services.s3.model.*;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/7 18:52
 */
public class FastDfsCommonApi implements S3CommonMultipartApi {

    @Override
    public InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest request) {
        return null;
    }

    @Override
    public UploadPartResult uploadPart(UploadPartRequest request) {
        return null;
    }

    @Override
    public MultipartUploadListing listMultipartUploads(ListMultipartUploadsRequest request) {
        return null;
    }

    @Override
    public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest request) {
        return null;
    }

    @Override
    public CopyPartResult copyPart(CopyPartRequest copyPartRequest) {
        throw new UnsupportedOperationException("Extend AbstractAmazonS3 to provide an implementation");
    }

    @Override
    public void abortMultipartUpload(AbortMultipartUploadRequest request) {

    }
}
