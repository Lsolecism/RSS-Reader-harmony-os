package org.example.mycute.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mycute.domain.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private User user;
    private String token;
} 