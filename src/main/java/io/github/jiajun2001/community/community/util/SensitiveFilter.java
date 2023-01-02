package io.github.jiajun2001.community.community.util;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.tree.TreeNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private TrieNode root = new TrieNode();

    private static String FILTEREDWORD = "***";

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyWord;
            while ((keyWord = reader.readLine()) != null) {
                this.addKeyWord(keyWord.toLowerCase());
            }
        } catch (IOException e) {
            logger.error("Loading sensitive words fails!" + e.getMessage());
        }

    }

    private void addKeyWord(String keyWord) {
        TrieNode tempNode = root;
        for (int i = 0; i < keyWord.length(); i++) {
            char c = keyWord.charAt(i);
            TrieNode subNode = tempNode.getChildren(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addNode(c, subNode);
            }
            tempNode = subNode;
            if (i == keyWord.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    // Filter sensitive word
    // text is original text
    // It returns processed text
    public String filter(String text) {
        if (StringUtils.isBlank(text)) return null;

        // Pointer 1
        TrieNode tempNode = root;
        // Pointer 2
        int begin = 0;
        // Pointer 3
        int end = 0;

        // Outcome
        StringBuilder sb = new StringBuilder();

        while (end < text.length()) {
            char c = Character.toLowerCase(text.charAt(end));

            if (isSymbol(c)) {
                // If pointer points to root, retain the symbol
                if (tempNode == root) {
                    sb.append(text.charAt(end));
                    begin++;
                }
                end++;
                continue;
            }

            // Check child nodes
            tempNode = tempNode.getChildren(c);
            if (tempNode == null) {
                // String starts with begin is not a sensitive word
                sb.append(text.charAt(begin));
                // Next position
                end = ++begin;
                tempNode = root;
            } else if (tempNode.isKeywordEnd()) {
                // Find a sensitive word starts with begin and ends with end
                sb.append(FILTEREDWORD);
                // Next position
                begin = ++end;
                tempNode = root;
            } else {
                // Check the next character
                end++;
            }
        }
        // Handle the ending characters
        sb.append(text.substring(begin));
        return new String(sb);
    }

    // Determine if it is a sign
    private boolean isSymbol(Character c) {
        // 0x2E80 --- c > 0x9FFF: East Asian Character
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode {

        private boolean isKeywordEnd = false;

        private Map<Character, TrieNode> children = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addNode(Character c, TrieNode node) {
            children.put(c, node);
        }

        public TrieNode getChildren(Character c) {
            return children.get(c);
        }
    }
}
