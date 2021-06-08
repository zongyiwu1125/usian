package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class ImageController {

    @Autowired
    private FastFileStorageClient storageClient;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        String hou = filename.substring(filename.lastIndexOf(".")+1);
        try {
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), hou, null);
            return Result.ok("http://image.usian.com/"+storePath.getFullPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error("文件上传失败");
    }
}
