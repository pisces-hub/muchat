package io.pisceshub.muchat.common.publics.sensitive.core.support.result;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.core.api.IWordResult;
import io.pisceshub.muchat.common.publics.sensitive.core.api.IWordResultHandler;

/**
 * 不做任何处理 xiaochangbai
 */
@ThreadSafe
public class WordResultHandlerRaw implements IWordResultHandler<IWordResult> {

    @Override
    public IWordResult handle(IWordResult wordResult) {
        return wordResult;
    }

}
