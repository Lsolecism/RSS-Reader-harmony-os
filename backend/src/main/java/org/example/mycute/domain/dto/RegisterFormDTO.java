package org.example.mycute.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFormDTO {
    @NotBlank(message = "用户名不能为空")
    @Size( min = 1,max = 50, message = "用户名长度最多为50位")
    private String userName;
    @NotBlank(message = "密码不能为空")
    @Size(min= 1, max = 50, message = "密码长度最多为50位")
    private String passWord;
    @NotBlank(message = "邮箱不能为空")
    @Size(min= 1, max = 50, message = "邮箱长度最多为50位")
    @Email(message = "邮箱格式不正确")
    private String email;
}
