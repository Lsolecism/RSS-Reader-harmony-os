package org.example.mycute.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private Integer userId;
    private String userName;
    private String email;
    private String newPhoto;
}
