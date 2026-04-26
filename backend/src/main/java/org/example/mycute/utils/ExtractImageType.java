package org.example.mycute.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractImageType {
    @NotNull
    @Contract("null -> fail")
    public String extractImageType(String base64) {
        if (base64 == null || !base64.startsWith("data:image/")) {
            throw new IllegalArgumentException("非法的 Base64 图片格式");
        }

        // 示例：data:image/png;base64,iVBORw0...
        String[] parts = base64.split(",")[0].split("/");
        String type = parts[1].split(";")[0]; // 得到 "png" 或 "jpeg"

        // 检查是否支持的格式
        if (!type.matches("^(jpeg|png|gif)$")) {
            throw new IllegalArgumentException("不支持的图片类型: " + type);
        }

        return type;
    }
    public String extractBase64Data(String base64) {
        if (base64 == null || !base64.startsWith("data:image/")) {
            throw new IllegalArgumentException("非法的 Base64 格式");
        }
        return base64.split(",")[1];
    }
}
