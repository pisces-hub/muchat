package io.pisceshub.muchat.common.publics.sensitive.core.api;

import java.util.List;

/**
 * 拒绝出现的数据-返回的内容被当做是敏感词 xiaochangbai 3
 */
public interface IWordDeny {

    /**
     * 获取结果
     * 
     * @return 结果 3
     */
    List<String> deny();

}
