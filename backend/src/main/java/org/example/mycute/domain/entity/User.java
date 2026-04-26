package org.example.mycute.domain.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    @TableField(value = "user_name")
    private String userName;
    @TableField(value = "pass_word")
    private String passWord;
    @TableField(value = "email")
    private String email;
}
