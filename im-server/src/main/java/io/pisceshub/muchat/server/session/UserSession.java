package io.pisceshub.muchat.server.session;

import lombok.Data;

@Data
public class UserSession {

    private Long id;
    private String userName;
    private String nickName;
}
