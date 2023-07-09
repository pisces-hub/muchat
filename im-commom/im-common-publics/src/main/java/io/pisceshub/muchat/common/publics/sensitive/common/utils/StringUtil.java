package io.pisceshub.muchat.common.publics.sensitive.common.utils;

/**
 * @author xiaochangbai
 */
public class StringUtil {
    public static boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }

    public static boolean isEmptyTrim(String ip) {
        return isEmpty(ip);
    }

    public static boolean isNotBlank(String keyword) {
        return !isBlank(keyword);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }
}
