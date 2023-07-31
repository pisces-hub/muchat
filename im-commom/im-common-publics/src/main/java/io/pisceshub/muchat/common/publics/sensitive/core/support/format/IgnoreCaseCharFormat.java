package io.pisceshub.muchat.common.publics.sensitive.core.support.format;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.core.ICharFormat;

/**
 * 忽略大小写 xiaochangbai
 */
@ThreadSafe

public class IgnoreCaseCharFormat implements ICharFormat {

  @Override
  public char format(char original) {
    return Character.toLowerCase(original);
  }

}
