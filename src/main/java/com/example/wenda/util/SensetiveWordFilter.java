package com.example.wenda.util;

import org.apache.commons.lang.CharUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/28  17:26
 * @Description:
 */

@Service
public class SensetiveWordFilter implements InitializingBean {

    private TrimTree root = new TrimTree();

    public static void main(String[] args) {
        SensetiveWordFilter sensetiveWordFilter = new SensetiveWordFilter();

        /*sensetiveWordFilter.addKeyWord("色");*/
        System.out.println(sensetiveWordFilter.filterSensetiveWord("你哈色情"));
    }

    private boolean isSymbol(char c) {
        int cInt = (int) c;
        //除去26个英文大小写字母和0-9十个数字,东亚文字之外的任何符号
        return !CharUtils.isAsciiAlphanumeric(c) && (cInt < 0x2E80 || cInt > 0x9FFF);
    }

    /**
     * 向前缀树中添加关键词
     *
     * @param lineText
     */
    public void addKeyWord(String lineText) {
        TrimTree tmpNode = root;
        for (int i = 0; i < lineText.length(); ++i) {
            Character character = lineText.charAt(i);
            if (isSymbol(character)) {
                continue;
            }
            TrimTree node = tmpNode.getNextTrimTree(character);
            if (node == null) {
                node = new TrimTree();
                tmpNode.addTrimTreeNode(character, node);
            }
            tmpNode = node;
            if (i == lineText.length() - 1) {
                tmpNode.setEnd(true);
            }
        }
    }

    /**
     * 对输入的文本进行敏感词过滤，将敏感词替和谐掉，返回和谐后的字符串
     *
     * @param text
     * @return String
     */
    public String filterSensetiveWord(String text) {
        //用来记录敏感词的长度
        int length;
        //用来记录敏感词的个数
        int count = 0;
        StringBuffer stringBuffer = new StringBuffer();
        TrimTree tmpNode = new TrimTree();
        int begin = 0;
        int position = 0;
        for (int i = 0; i < text.length(); ++i) {
            length = 0;
            Character character = text.charAt(i);
            if (isSymbol(character)) {
                ++begin;
                position = begin;
                stringBuffer.append(character);
                continue;
            }
            tmpNode = root.getNextTrimTree(character);
            if (tmpNode == null) {
                ++position;
                begin = position;
                stringBuffer.append(character);
            } else if (tmpNode.isEnd()) {
                ++count;
                ++length;
                for (int j = 0; j < length; ++j) {
                    stringBuffer.append("*");
                }
                i = begin;
                position = begin + 1;
                begin = position;
            } else {
                while (true) {
                    ++length;
                    ++begin;
                    Character character1 = text.charAt(begin);
                    if (isSymbol(character1)) {
                        continue;
                    }
                    tmpNode = tmpNode.getNextTrimTree(character1);
                    if (tmpNode == null) {
                        stringBuffer.append(character1);
                        break;
                    } else if (tmpNode.isEnd()) {
                        ++length;
                        ++count;
                        for (int j = 0; j < length; j++) {
                            stringBuffer.append("*");
                        }
                        i = begin;
                        position = begin + 1;
                        begin = position;
                        break;
                    }
                }
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 通过配置文件加载敏感词文件
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sensetiveWord.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String lineText;
            while ((lineText = bufferedReader.readLine()) != null) {
                lineText.trim();
                addKeyWord(lineText);
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("读取敏感词文件失败" + e.getLocalizedMessage());
        }
    }

    /**
     * 前缀树的类
     */
    private class TrimTree {
        //用来标记某一个结点是否为关键词的结束结点，false表示不是结束结点，true表示为敏感词的结束结点
        private boolean end = false;
        //用来存储前缀树的结点，Character表示关键词，TrimTree表示该结点的下一个结点，
        //其中存储了该结点所有相邻子结点的集合
        private Map<Character, TrimTree> trimTreeNode = new HashMap<Character, TrimTree>();

        public TrimTree getNextTrimTree(Character key) {
            return trimTreeNode.get(key);
        }

        public void addTrimTreeNode(Character key, TrimTree trimTree) {
            trimTreeNode.put(key, trimTree);
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }
}
