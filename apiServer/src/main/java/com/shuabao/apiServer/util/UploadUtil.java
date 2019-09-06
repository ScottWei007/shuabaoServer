package com.shuabao.apiServer.util;

import com.shuabao.core.manager.EnvironmentManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Scott Wei on 8/18/2018.
 */
public final class UploadUtil {
    //上傳目錄
    public static final String UPLOADDIR = EnvironmentManager.getEnvironment().getProperty("spring.upload.dir");

    public static final String uploadFile(MultipartFile file) throws Exception{
        if(Objects.isNull(file) || file.isEmpty()) {
            return null;
        }
        //創建目錄路徑
        File dir = new File(UPLOADDIR);
        if(!dir.exists()) {
            dir.mkdir();
        }
        String fileName =  file.getOriginalFilename();
        //uuid + 文件名後綴名
        fileName = UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
        String realPath = UPLOADDIR + fileName;
        Path path = Paths.get(realPath);
        Files.write(path,  file.getBytes());
        return realPath;
    }
}
