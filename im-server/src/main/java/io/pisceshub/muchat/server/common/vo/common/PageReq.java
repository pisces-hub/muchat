package io.pisceshub.muchat.server.common.vo.common;

import lombok.Data;

/**
 * @author xiaochangbai
 * @date 2023-06-17 16:40
 */
@Data
public class PageReq {

    /**
     * 页码
     */
    private Long page = 1L;

    /**
     * 每页大小
     */
    private Long size  = 15L;
}
