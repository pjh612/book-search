package com.example.trevaribooksearch.infrastructure.search.parser;

import com.example.trevaribooksearch.infrastructure.search.exception.InvalidSearchQueryException;
import com.example.trevaribooksearch.infrastructure.search.model.KeywordToken;
import com.example.trevaribooksearch.infrastructure.search.model.SearchCriteria;
import com.example.trevaribooksearch.infrastructure.search.model.SearchOperator;
import com.example.trevaribooksearch.infrastructure.search.model.SearchOperatorType;

import java.util.LinkedList;
import java.util.Stack;

public class DefaultQueryParser implements QueryParser<SearchCriteria> {

    public SearchCriteria parse(String query) {
        if (query == null || query.isBlank()) {
            throw new InvalidSearchQueryException("query cannot be null or blank");
        }

        Stack<String> tokens = tokenize(query);
        LinkedList<KeywordToken> keywordTokens = buildKeywordTokens(tokens);
        SearchOperator operatorType = keywordTokens.isEmpty()
                ? SearchOperatorType.NO_OPERATOR
                : keywordTokens.getLast().operator();

        return new SearchCriteria(keywordTokens, operatorType);
    }

    private Stack<String> tokenize(String query) {
        Stack<String> result = new Stack<>();
        StringBuilder sb = new StringBuilder();

        for (char c : query.toCharArray()) {
            if (c == '|' || c == '-') {
                if (!sb.isEmpty()) {
                    result.add(sb.toString().trim());
                    sb.setLength(0);
                }
                result.add(String.valueOf(c));
            } else {
                sb.append(c);
            }
        }
        if (!sb.isEmpty()) {
            result.add(sb.toString().trim());
        }
        return result;
    }

    private LinkedList<KeywordToken> buildKeywordTokens(Stack<String> tokens) {
        LinkedList<KeywordToken> keywordTokens = new LinkedList<>();
        String lastKeyword = null;
        while (!tokens.isEmpty()) {
            String current = tokens.pop();
            if (current.equals("-") && lastKeyword != null) {
                keywordTokens.addFirst(new KeywordToken(lastKeyword, SearchOperatorType.NOT_OPERATOR));
            } else if (current.equals("|") && lastKeyword != null) {
                keywordTokens.addFirst(new KeywordToken(lastKeyword, SearchOperatorType.OR_OPERATOR));
            }
            lastKeyword = current;
        }
        keywordTokens.addFirst(new KeywordToken(lastKeyword, SearchOperatorType.NO_OPERATOR));

        return keywordTokens;
    }
}
