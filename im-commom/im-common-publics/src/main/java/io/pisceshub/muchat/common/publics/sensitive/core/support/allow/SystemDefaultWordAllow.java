package io.pisceshub.muchat.common.publics.sensitive.core.support.allow;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.FileUtils;
import io.pisceshub.muchat.common.publics.sensitive.core.api.IWordAllow;

import java.util.List;

/**
 * 系统默认的信息 xiaochangbai 3
 */
@ThreadSafe
public class SystemDefaultWordAllow implements IWordAllow {

  @Override
  public List<String> allow() {
    return FileUtils.readAllLinesForZip(
        SystemDefaultWordAllow.class.getResourceAsStream("/allow.zip"));
  }

}
