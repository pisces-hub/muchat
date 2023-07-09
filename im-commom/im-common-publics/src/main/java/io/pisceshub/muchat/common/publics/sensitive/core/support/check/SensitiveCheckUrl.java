package io.pisceshub.muchat.common.publics.sensitive.core.support.check;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.pisceshub.muchat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.pisceshub.muchat.common.publics.sensitive.common.core.WordContext;
import io.pisceshub.muchat.common.publics.sensitive.common.enums.ValidModeEnum;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.CharUtil;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.RegexUtil;

/**
 * URL 正则表达式检测实现。
 *
 * 也可以严格的保留下来。
 *
 * （1）暂时先粗略的处理 web-site
 * （2）如果网址的最后为图片类型，则跳过。
 * （3）长度超过 70，直接结束。
 *
 * xiaochangbai
 *
 */
@ThreadSafe
public class SensitiveCheckUrl implements ISensitiveCheck {

    /**
     * 最长的网址长度
     *2
     */
    private static final int MAX_WEB_SITE_LEN = 70;

    @Override
    public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum, WordContext wordContext) {
        // 记录敏感词的长度
        int lengthCount = 0;
        int actualLength = 0;

        StringBuilder stringBuilder = new StringBuilder();
        // 这里偷懒直接使用 String 拼接，然后结合正则表达式。
        // DFA 本质就可以做正则表达式，这样实现不免性能会差一些。
        // 后期如果有想法，对 DFA 进一步深入学习后，将进行优化。
        for(int i = beginIndex; i < txt.length(); i++) {
            char currentChar = txt.charAt(i);
            char mappingChar = wordContext.formatChar(currentChar);;

            if(CharUtil.isWebSiteChar(mappingChar)
                && lengthCount <= MAX_WEB_SITE_LEN) {
                lengthCount++;
                stringBuilder.append(currentChar);

                if(isCondition(stringBuilder.toString())) {
                    actualLength = lengthCount;

                    // 是否遍历全部匹配的模式
                    if(ValidModeEnum.FAIL_FAST.equals(validModeEnum)) {
                        break;
                    }
                }
            } else {
                break;
            }
        }

        return SensitiveCheckResult.of(actualLength, SensitiveCheckUrl.class);
    }

    /**
     * 这里指定一个阈值条件
     * @param string 长度
     * @return 是否满足条件
     *2
     */
    private boolean isCondition(final String string) {
        return RegexUtil.isWebSite(string);
    }


}
