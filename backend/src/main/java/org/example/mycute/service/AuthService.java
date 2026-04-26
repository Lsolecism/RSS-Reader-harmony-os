package org.example.mycute.service;

import org.example.mycute.domain.dto.*;
import org.example.mycute.domain.entity.User;

public interface AuthService {
    ResultDTO<AuthResponseDTO> login(LoginFormDTO loginFormDTO);
    ResultDTO<AuthResponseDTO> register(RegisterFormDTO registerFormDTO);
    String generateAndStoreCode(String email);
    boolean verifyCode(String email, String verificationCode);
}
