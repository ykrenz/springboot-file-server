package com.github.ren.file.client.s3;

import com.amazonaws.services.s3.model.*;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/7 18:50
 */
public interface S3CommonMultipartApi {
//    public static void main(String[] args) {
//        AmazonS3 amazonS3 = new AmazonS3Client();
//
//        amazonS3.initiateMultipartUpload()
//        amazonS3.uploadPart()
//        amazonS3.listMultipartUploads();
//
//        amazonS3.completeMultipartUpload()
//        amazonS3.abortMultipartUpload();
//    }

    InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest request);

    UploadPartResult uploadPart(UploadPartRequest request);

    MultipartUploadListing listMultipartUploads(ListMultipartUploadsRequest request);

    CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest request);

    CopyPartResult copyPart(CopyPartRequest copyPartRequest);

    void abortMultipartUpload(AbortMultipartUploadRequest request);
}
