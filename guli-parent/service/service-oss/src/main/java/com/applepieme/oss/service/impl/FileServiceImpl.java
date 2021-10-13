package com.applepieme.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.applepieme.base.exception.GuliException;
import com.applepieme.oss.service.FileService;
import com.applepieme.oss.util.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author applepieme
 * @date 2021/10/1 21:05
 */
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile multipartFile, String host) {
        String endpoint = ConstantPropertiesUtils.ENDPOINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String uploadUrl = null;

        // 创建PutObjectRequest对象。
        // 依次填写Bucket名称（例如examplebucket）、Object完整路径（例如exampledir/exampleobject.txt）和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        PutObjectRequest putObjectRequest;
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filePath = host + "/" + new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileUrl = filePath + "/" + fileName + fileType;
        try {
            putObjectRequest = new PutObjectRequest(bucketName, fileUrl, multipartFile.getInputStream());
            // 上传文件。
            ossClient.putObject(putObjectRequest);
            uploadUrl = "https://" + bucketName + "." + endpoint + "/" + fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "上传文件出现异常");
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return uploadUrl;
    }
}
