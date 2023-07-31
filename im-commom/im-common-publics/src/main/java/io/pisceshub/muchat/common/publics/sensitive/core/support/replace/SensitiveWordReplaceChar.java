package io.pisceshub.muchat.common.publics.sensitive.core.support.replace;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.CharUtil;
import io.pisceshub.muchat.common.publics.sensitive.core.api.ISensitiveWordReplace;
import io.pisceshub.muchat.common.publics.sensitive.core.api.ISensitiveWordReplaceContext;

/**
 * 指定字符的替换策略 xiaochangbai
 */
@ThreadSafe
public class SensitiveWordReplaceChar implements ISensitiveWordReplace {

  private final char replaceChar;

  public SensitiveWordReplaceChar(char replaceChar) {
    this.replaceChar = replaceChar;
  }

  @Override
  public String replace(ISensitiveWordReplaceContext context) {
    int wordLength = context.wordLength();

    return CharUtil.repeat(replaceChar, wordLength);
  }

}
