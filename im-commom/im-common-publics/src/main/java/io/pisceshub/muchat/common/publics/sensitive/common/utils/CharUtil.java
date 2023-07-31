package io.pisceshub.muchat.common.publics.sensitive.common.utils;

/**
 * @author xiaochangbai
 */
public class CharUtil {

  private CharUtil() {
  }

  public static boolean isDigitOrLetter(char c) {
    return Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c);
  }

  public static boolean isEmilChar(char c) {
    return isDigitOrLetter(c) || '_' == c || '-' == c || c == '.' || c == '@';
  }

  public static boolean isWebSiteChar(char c) {
    return isDigitOrLetter(c) || '-' == c || '.' == c;
  }

  public static String repeat(Character replaceChar, int wordLength) {
    if (replaceChar != null && wordLength > 0) {
      StringBuilder stringBuffer = new StringBuilder();

      for (int i = 0; i < wordLength; ++i) {
        stringBuffer.append(replaceChar);
      }

      return stringBuffer.toString();
    } else {
      return "";
    }
  }
}
