package io.pisceshub.muchat.common.publics.sensitive.core.support.format;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.core.ICharFormat;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.CharUtils;

/**
 * 忽略英文的各种格式
 * xiaochangbai
 *
 */
@ThreadSafe
public class IgnoreEnglishStyleFormat implements ICharFormat {

    @Override
    public char format(char original) {
        return CharUtils.getMappingChar(original);
    }

}
