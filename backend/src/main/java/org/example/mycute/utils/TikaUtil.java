package org.example.mycute.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class TikaUtil {
    public String detectMimeType(@NotNull MultipartFile file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file.getInputStream());
    }

    // 获取文件扩展名
    @NotNull
    public String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDotIndex = filename.lastIndexOf(".");
        return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex + 1);
    }
}
