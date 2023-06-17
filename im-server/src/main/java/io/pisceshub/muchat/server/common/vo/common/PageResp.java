package io.pisceshub.muchat.server.common.vo.common;

import lombok.Data;

import java.util.List;

/**
 * @author xiaochangbai
 * @date 2023-06-17 16:40
 */
@Data
public class PageResp<T> {

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 总数量
     */
    private Long total;


    /**
     * 数据集
     */
    private List<T> list;
}
