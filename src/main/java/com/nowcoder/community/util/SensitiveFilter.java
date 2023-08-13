package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: SensitiveFilter
 * Package: com.nowcoder.community.util
 * Description:
 *
 * @Author CC
 * @Create 2023/8/13 14:32
 * @Version 1.0
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String REPLACEMENT = "***";
    private TrieNode rootNode = new TrieNode();

    @PostConstruct//使用@PostConstruct注解修饰的init方法就会在Spring容器的启动时自动的执行
    public void init() {
        //类路径加载资源
        try (
                //获得缓冲字节流，使用类加载器读取文件（这个方式默认文件在class路径下）
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ( (keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyWord(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词失败" + e.getMessage());
        }
    }

    //前缀树
    private class TrieNode {
        //关键词标识
        private boolean isKeywordEnd = false;
        //子节点(key是下级字符，value是下级节点)
        private Map<Character, TrieNode> children = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addChild(Character c, TrieNode node) {
            children.put(c, node);
        }

        //获取子节点
        public TrieNode getChild(Character c) {
            return children.get(c);
        }
    }

    //将一个敏感词添加到前缀树中
    private void addKeyWord(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode child = tempNode.getChild(c);
            if (child == null) {
                child = new TrieNode();
                tempNode.addChild(c, child);
            }
            //当前指针指向子节点
            tempNode = child;
        }
        tempNode.setKeywordEnd(true);
    }

    //过滤敏感词,被外面调用，所以public

    /**
     *过滤敏感词
     *
     * @param text 原始文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        int len = text.length();
        //结果
        StringBuilder ans = new StringBuilder();
        //指针2，3
        int start = 0, end = 0;
        while (end < len) {
            char c = text.charAt(end);
            //跳过符号
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    ans.append(c);
                    start++;
                }
                end++;//end一定走，start不一定走
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getChild(c);
            if (tempNode == null) {
                ans.append(text.charAt(start));
                start++;
                end = start;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                //发现了敏感词，将begin~end的字符串替换掉
                ans.append(REPLACEMENT);
                start = end + 1;
                end = start;
                tempNode = rootNode;
            } else {
                //检查下一个字符
                end++;
            }
        }

        //将最后一批字符加入
        ans.append(text.substring(start));
        return ans.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c) {
        //0x2E80 ~ 0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
}
