package com.vladsch.ReverseRegEx.util;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public interface RegExMatcher extends MatchResult {
    Pattern pattern();

    MatchResult toMatchResult();

    RegExMatcher appendReplacement(StringBuffer sb, String replacement);

    StringBuffer appendTail(StringBuffer sb);

    RegExMatcher reset();

    RegExMatcher reset(CharSequence input);

    @Override
    int start();

    @Override
    int start(int group);

    @Override
    int end();

    @Override
    int end(int group);

    @Override
    String group();

    @Override
    String group(int group);

    String group(String name);

    @Override
    int groupCount();

    boolean matches();

    boolean find();

    boolean find(int start);

    boolean lookingAt();

    String replaceAll(String replacement);

    String replaceFirst(String replacement);

    RegExMatcher region(int start, int end);

    int regionStart();

    int regionEnd();

    boolean hasTransparentBounds();

    RegExMatcher useTransparentBounds(boolean b);

    boolean hasAnchoringBounds();

    RegExMatcher useAnchoringBounds(boolean b);

    boolean hitEnd();

    boolean requireEnd();
}
