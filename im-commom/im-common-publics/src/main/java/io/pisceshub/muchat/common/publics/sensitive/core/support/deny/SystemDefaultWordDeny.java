package io.pisceshub.muchat.common.publics.sensitive.core.support.deny;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.FileUtils;
import io.pisceshub.muchat.common.publics.sensitive.core.api.IWordDeny;

import java.util.List;

/**
 * 系统默认的信息 xiaochangbai 3
 */
@ThreadSafe
public class SystemDefaultWordDeny implements IWordDeny {

  @Override
  public List<String> deny() {
    return FileUtils.readAllLinesForZip(
        SystemDefaultWordDeny.class.getResourceAsStream("/deny.zip"));
  }

}
