package io.pisceshub.muchat.server.admin.dto;

import lombok.Data;



@Data
public class ServerConnectionInfoItemVo {

    private String netAddress;

    private String protocol;

    private Long connectorCount;

}
