package io.pisceshub.muchat.common.publics.sensitive.common.core;

import io.pisceshub.muchat.common.publics.sensitive.common.enums.ValidModeEnum;

/**
 * 敏感信息监测接口 （1）敏感词 （2）数字（连续8位及其以上） （3）邮箱 （4）URL 可以使用责任链的模式，循环调用。 xiaochangbai
 */
public interface ISensitiveCheck {

  /**
   * 检查敏感词数量
   * <p>
   * （1）如果未命中敏感词，直接返回 0 （2）命中敏感词，则返回敏感词的长度。
   * <p>
   * ps: 这里结果进行优化， 1. 是否包含敏感词。 2. 敏感词的长度 3. 正常走过字段的长度（便于后期替换优化，避免不必要的循环重复）
   *
   * @param txt           文本信息
   * @param beginIndex    开始下标
   * @param validModeEnum 验证模式
   * @return 敏感信息对应的长度
   */
  SensitiveCheckResult sensitiveCheck(final String txt, final int beginIndex,
      final ValidModeEnum validModeEnum,
      WordContext wordContext);

}
