package io.pisceshub.muchat.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"io.pisceshub.muchat.server.mapper"})
@SpringBootApplication(exclude= {SecurityAutoConfiguration.class })
public class ImServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImServerApplication.class,args);

    }
}
