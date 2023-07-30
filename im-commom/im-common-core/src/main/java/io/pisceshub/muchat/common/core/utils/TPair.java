package io.pisceshub.muchat.common.core.utils;

import lombok.Getter;

/**
 * @author xiaochangbai
 * @date 2023-07-01 11:36
 */
@Getter
public class TPair<L, R> {

    private L left;

    private R right;

    public TPair(L left, R right){
        this.left = left;
        this.right = right;
    }

}
