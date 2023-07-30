package io.pisceshub.muchat.common.publics.sensitive.core.api;

/**
 * 敏感词的结果处理 xiaochangbai
 */
public interface IWordResultHandler<R> {

    /**
     * 对于结果的处理
     * 
     * @param wordResult 结果
     * @return 处理结果
     */
    R handle(final IWordResult wordResult);

}
