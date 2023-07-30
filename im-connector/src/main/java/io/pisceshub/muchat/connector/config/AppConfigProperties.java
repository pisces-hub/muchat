package io.pisceshub.muchat.connector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/6/12 15:42
 */
@Data
@Configuration
@ConfigurationProperties(prefix = AppConfigProperties.PRE)
public class AppConfigProperties {

    public final static String PRE = "app";

    /**
     * 公网ip
     */
    private String             ip;

    private TcpNode            ws;

    private TcpNode            tcp;

    private ZkNode             zk;

    @Data
    public static class TcpNode {

        private Boolean enable;

        /**
         * -1随机
         */
        private Integer port;

    }

    @Data
    public static class ZkNode {

        private String path;

        private String address;

    }

}
