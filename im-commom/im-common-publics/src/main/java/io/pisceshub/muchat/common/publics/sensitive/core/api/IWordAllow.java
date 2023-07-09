package io.pisceshub.muchat.common.publics.sensitive.core.api;

import java.util.List;

/**
 * 允许的内容-返回的内容不被当做敏感词
 * xiaochangbai
 *3
 */
public interface IWordAllow {

    /**
     * 获取结果
     * @return 结果
     *3
     */
    List<String> allow();

}
