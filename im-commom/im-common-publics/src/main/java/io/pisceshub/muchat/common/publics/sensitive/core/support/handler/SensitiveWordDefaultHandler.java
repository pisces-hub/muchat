package io.pisceshub.muchat.common.publics.sensitive.core.support.handler;


import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.pisceshub.muchat.common.publics.sensitive.common.core.NodeTree;
import io.pisceshub.muchat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.pisceshub.muchat.common.publics.sensitive.common.core.WordContext;
import io.pisceshub.muchat.common.publics.sensitive.common.enums.ValidModeEnum;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.CollectionUtil;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.FileUtils;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.StringUtil;
import io.pisceshub.muchat.common.publics.sensitive.core.api.ISensitiveWordReplace;
import io.pisceshub.muchat.common.publics.sensitive.core.api.ISensitiveWordReplaceContext;
import io.pisceshub.muchat.common.publics.sensitive.core.api.IWordHandler;
import io.pisceshub.muchat.common.publics.sensitive.core.api.IWordResult;
import io.pisceshub.muchat.common.publics.sensitive.core.support.check.SensitiveCheckUrl;
import io.pisceshub.muchat.common.publics.sensitive.core.support.replace.SensitiveWordReplaceContext;
import io.pisceshub.muchat.common.publics.sensitive.core.support.result.WordResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 敏感词处理器
 *
 * xiaochangbai
 *
 */
@Slf4j
@ThreadSafe
public class SensitiveWordDefaultHandler implements IWordHandler {

    private WordContext wordContext;

    @Override
    public synchronized void initWord(Collection<String> collection, WordContext wordContext) {
        this.wordContext = wordContext;
        long startTime = System.currentTimeMillis();
        NodeTree rootNode = new NodeTree();
        for (String key : collection) {
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            NodeTree tempNode = rootNode;
            for (int i = 0; i < key.length(); i++) {
                char c = key.charAt(i);
                NodeTree subNode = tempNode.getSubNode(c);
                if (subNode == null) {
                    // 初始化子节点
                    subNode = new NodeTree();
                    tempNode.addSubNode(c, subNode);
                }
                // 指向子节点,进入下一轮循环
                tempNode = subNode;
                // 设置结束标识
                if (i == key.length() - 1) {
                    tempNode.setKeywordEnd(true);
                }
            }
        }
        wordContext.setRootNode(rootNode);
        log.info("敏感词初始化完成，共{}个词,加载耗时:{}/s",collection.size(),(System.currentTimeMillis()-startTime)/1000.0);
    }

    /**
     * 是否包含
     * （1）直接遍历所有
     * （2）如果遇到，则直接返回 true
     *
     * @param string 字符串
     * @return 是否包含
     *
     */
    @Override
    public boolean contains(String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        for (int i = 0; i < string.length(); i++) {
            SensitiveCheckResult checkResult = sensitiveCheck(string, i, ValidModeEnum.FAIL_FAST,wordContext);
            // 快速返回
            if (checkResult.index() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回所有对应的敏感词
     * （1）结果是有序的
     * （2）为了保留所有的下标，结果从 v0.1.0 之后不再去重。
     *
     * @param string 原始字符串
     * @return 结果
     *
     */
    @Override
    public List<IWordResult> findAll(String string) {
        return getSensitiveWords(string, ValidModeEnum.FAIL_OVER);
    }

    @Override
    public IWordResult findFirst(String string) {
        List<IWordResult> stringList = getSensitiveWords(string, ValidModeEnum.FAIL_FAST);

        if (CollectionUtil.isEmpty(stringList)) {
            return null;
        }

        return stringList.get(0);
    }

    @Override
    public String replace(String target, final ISensitiveWordReplace replace) {
        if(StringUtil.isEmpty(target)) {
            return target;
        }

        return this.replaceSensitiveWord(target, replace);
    }

    /**
     * 获取敏感词列表
     *
     * @param text     文本
     * @param modeEnum 模式
     * @return 结果列表
     *
     */
    private List<IWordResult> getSensitiveWords(final String text, final ValidModeEnum modeEnum) {
        //1. 是否存在敏感词，如果比存在，直接返回空列表
        if (StringUtil.isEmpty(text)) {
            return new ArrayList<>();
        }

        List<IWordResult> resultList = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            SensitiveCheckResult checkResult = sensitiveCheck(text, i, ValidModeEnum.FAIL_OVER,wordContext);
            // 命中
            int wordLength = checkResult.index();
            if (wordLength > 0) {
                // 保存敏感词
                String sensitiveWord = text.substring(i, i + wordLength);

                // 添加去重
                WordResult wordResult = WordResult.newInstance()
                        .startIndex(i)
                        .endIndex(i+wordLength)
                        .word(sensitiveWord);
                resultList.add(wordResult);

                // 快速返回
                if (ValidModeEnum.FAIL_FAST.equals(modeEnum)) {
                    break;
                }

                // 增加 i 的步长
                // 为什么要-1，因为默认就会自增1
                // TODO: 这里可以根据字符串匹配算法优化。
                i += wordLength - 1;
            }
        }

        return resultList;
    }

    /**
     * 直接替换敏感词，返回替换后的结果
     * @param target           文本信息
     * @param replace 替换策略
     * @return 脱敏后的字符串
     *
     */
    private String replaceSensitiveWord(final String target,
                                        final ISensitiveWordReplace replace) {
        if(StringUtil.isEmpty(target)) {
            return target;
        }
        // 用于结果构建
        StringBuilder resultBuilder = new StringBuilder(target.length());

        for (int i = 0; i < target.length(); i++) {
            char currentChar = target.charAt(i);
            // 内层直接从 i 开始往后遍历，这个算法的，获取第一个匹配的单词
            SensitiveCheckResult checkResult = sensitiveCheck(target, i, ValidModeEnum.FAIL_OVER,wordContext);

            // 敏感词
            int wordLength = checkResult.index();
            if(wordLength > 0) {
                // 是否执行替换
                Class checkClass = checkResult.checkClass();
                String string = target.substring(i, i+wordLength);
                if(SensitiveCheckUrl.class.equals(checkClass)
                    && FileUtils.isImage(string)) {
                    // 直接使用原始内容，避免 markdown 图片转换失败
                    resultBuilder.append(string);
                } else {
                    // 创建上下文
                    ISensitiveWordReplaceContext replaceContext = SensitiveWordReplaceContext.newInstance()
                            .sensitiveWord(string)
                            .wordLength(wordLength);
                    String replaceStr = replace.replace(replaceContext);

                    resultBuilder.append(replaceStr);
                }

                // 直接跳过敏感词的长度
                i += wordLength-1;
            } else {
                // 普通词
                resultBuilder.append(currentChar);
            }
        }

        return resultBuilder.toString();
    }

    public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum,
                                               WordContext wordContext) {

        for(ISensitiveCheck sensitiveCheck : wordContext.getSensitiveChecks()) {
            SensitiveCheckResult result = sensitiveCheck.sensitiveCheck(txt, beginIndex, validModeEnum,wordContext);
            if(result.index() > 0) {
                return result;
            }
        }
        return SensitiveCheckResult.of(0,null);

    }


}
