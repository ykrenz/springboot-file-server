package com.github.ren.file.client.s3;

import com.amazonaws.services.s3.model.*;

/**
 * @Description s3 bucket spi
 * @Author ren
 * @Since 1.0
 */
public interface S3BucketSpi {

    /**
     * 创建 bucket
     *
     * @param createBucketRequest
     * @return
     */
    Bucket createBucket(CreateBucketRequest createBucketRequest);

    /**
     * 删除bucket
     *
     * @param deleteBucketRequest
     */
    void deleteBucket(DeleteBucketRequest deleteBucketRequest);

    /**
     * 获取 bucket acl
     *
     * @param bucketName
     * @return
     */
    AccessControlList getBucketAcl(String bucketName);

    /**
     * 查询bucket Objects
     *
     * @param listObjectsRequest
     * @return
     */
    ObjectListing listObjects(ListObjectsRequest listObjectsRequest);

    /**
     * 查询bucket Objects v2
     *
     * @param listObjectsV2Request
     * @return
     */
    ListObjectsV2Result listObjectsV2(ListObjectsV2Request listObjectsV2Request);

    /**
     * 获取bucket生命周期配置
     *
     * @param getBucketLifecycleConfigurationRequest
     * @return
     */
    BucketLifecycleConfiguration getBucketLifecycleConfiguration(
            GetBucketLifecycleConfigurationRequest getBucketLifecycleConfigurationRequest);
}
