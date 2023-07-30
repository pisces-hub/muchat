package io.pisceshub.muchat.server.common.vo.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author xiaochangbai
 * @date 2023-06-18 15:40
 */
@Data
public class AnonymousLoginReq implements Serializable {

    @NotNull
    @Size(min = 10, max = 100)
    private String anonymouId;
}
