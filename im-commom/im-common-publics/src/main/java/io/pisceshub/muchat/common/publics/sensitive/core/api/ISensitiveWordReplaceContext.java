package io.pisceshub.muchat.common.publics.sensitive.core.api;

/**
 * 敏感词替换策略上下文 xiaochangbai
 */
public interface ISensitiveWordReplaceContext {

    /**
     * 敏感词
     * 
     * @return 敏感词
     */
    String sensitiveWord();

    /**
     * 单词长度
     * 
     * @return 单词长度
     */
    int wordLength();

}
