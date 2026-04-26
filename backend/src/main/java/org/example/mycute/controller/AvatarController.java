package org.example.mycute.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.domain.entity.Avatar;
import org.example.mycute.service.AvatarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/avatar")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable int userId) {
        Avatar avatar = avatarService.getAvatarByUserId(userId);

        // 根据类型设置 Content-Type
        MediaType mediaType = switch (avatar.getType()) {
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.IMAGE_JPEG;
        };

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(avatar.getImage());
    }
}
