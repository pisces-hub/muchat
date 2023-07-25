package io.pisceshub.muchat.common.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaochangbai
 * @date 2023-07-08 11:30
 */
@Data
public class PageReq implements Serializable {

    private Long pageNo;

    private Long pageSize;

}
