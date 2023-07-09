package io.pisceshub.muchat.common.publics.sensitive.common.core;


/**
 * 单词格式化
 * （1）忽略大小写
 * （2）忽略全角半角
 * （3）忽略停顿词
 * （4）忽略数字转换。
 *
 * xiaochangbai
 *
 */
public interface ICharFormat {

    /**
     * 针对 char 格式化
     * @param original 原始 char
     * @return 格式化后的 char
     *
     */
    char format(final char original);

}
