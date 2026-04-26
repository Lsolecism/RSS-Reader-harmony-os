package org.example.mycute;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.mycute.mapper")
public class MycuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycuteApplication.class, args);
        System.out.println("启动成功！");
    }

}
