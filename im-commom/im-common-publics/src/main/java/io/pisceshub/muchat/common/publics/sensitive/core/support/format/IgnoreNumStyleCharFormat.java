package io.pisceshub.muchat.common.publics.sensitive.core.support.format;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.core.ICharFormat;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.NumUtils;

/**
 * 忽略数字的样式 xiaochangbai
 */
@ThreadSafe
public class IgnoreNumStyleCharFormat implements ICharFormat {

    @Override
    public char format(char original) {
        return NumUtils.getMappingChar(original);
    }

}
