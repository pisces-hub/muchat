package io.pisceshub.muchat.common.publics.sensitive.common.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WordContext {

    /**
     * 敏感词树
     */
    private NodeTree              rootNode;

    /**
     * 忽略重复词
     */
    private boolean               ignoreRepeat;

    /**
     * 敏感数字检测对应的长度限制
     */
    private int                   sensitiveCheckNumLen = -1;

    /**
     * 格式化链
     */
    private List<ICharFormat>     charFormats;

    /**
     * 敏感词检查链
     */
    private List<ISensitiveCheck> sensitiveChecks;

    public void addCharFormat(ICharFormat charFormat) {
        if (this.charFormats == null) {
            this.charFormats = new ArrayList<>();
        }
        this.charFormats.add(charFormat);
    }

    public void addSensitiveChecks(ISensitiveCheck sensitiveCheck) {
        if (this.sensitiveChecks == null) {
            this.sensitiveChecks = new ArrayList<>();
        }
        this.sensitiveChecks.add(sensitiveCheck);
    }

    public char formatChar(char origin) {
        if (this.charFormats == null || this.charFormats.size() < 1) {
            return origin;
        }
        for (ICharFormat iCharFormat : charFormats) {
            origin = iCharFormat.format(origin);
        }
        return origin;
    }

}
