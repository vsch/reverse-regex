package com.vladsch.ReverseRegEx.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a wrapper class for Matcher that allows using an interface for both forward and reverse searches
 */
public class ForwardMatcher implements RegExMatcher {
    private Matcher myMatcher;

    /**
     * Constructor of reversed matcher that takes a ReversedRegEx instance and an input character sequence.
     * if input is not an instance of ReverseCharSequence then it is wrapped in ReversedCharSequence class.
     *
     * @param matcher matcher to wrap
     */
    public ForwardMatcher(Matcher matcher) {
        myMatcher = matcher;
    }

    @Override
    public Pattern pattern() {
        return myMatcher.pattern();
    }

    @Override
    public MatchResult toMatchResult() {
        return this;
    }

    @Override
    public RegExMatcher appendReplacement(final StringBuffer sb, final String replacement) {
        myMatcher.appendReplacement(sb, replacement);
        return this;
    }

    @Override
    public StringBuffer appendTail(final StringBuffer sb) {
        return myMatcher.appendTail(sb);
    }

    public RegExMatcher usePattern(final Pattern pattern) {
        myMatcher.usePattern(pattern);
        return this;
    }

    @Override
    public RegExMatcher reset() {
        myMatcher.reset();
        return this;
    }

    @Override
    public RegExMatcher reset(final CharSequence input) {
        myMatcher.reset(input);
        return this;
    }

    @Override
    public int start() {
        return myMatcher.start();
    }

    @Override
    public int start(final int group) {
        return myMatcher.start(group);
    }

    @Override
    public int end() {
        return myMatcher.end();
    }

    @Override
    public int end(final int group) {
        return myMatcher.end(group);
    }

    @Override
    public String group() {
        return myMatcher.group();
    }

    @Override
    public String group(final int group) {
        return myMatcher.group(group);
    }

    @Override
    public String group(final String name) {
        return myMatcher.group(name);
    }

    @Override
    public int groupCount() {
        return myMatcher.groupCount();
    }

    @Override
    public boolean matches() {
        return myMatcher.matches();
    }

    @Override
    public boolean find() {
        return myMatcher.find();
    }

    @Override
    public boolean find(final int start) {
        return myMatcher.find(start);
    }

    @Override
    public boolean lookingAt() {
        return myMatcher.lookingAt();
    }

    public static String quoteReplacement(final String s) {
        return Matcher.quoteReplacement(s);
    }

    @Override
    public String replaceAll(final String replacement) {
        return myMatcher.replaceAll(replacement);
    }

    @Override
    public String replaceFirst(final String replacement) {
        return myMatcher.replaceFirst(replacement);
    }

    @Override
    public RegExMatcher region(final int start, final int end) {
        myMatcher.region(start, end);
        return this;
    }

    @Override
    public int regionStart() {
        return myMatcher.regionStart();
    }

    @Override
    public int regionEnd() {
        return myMatcher.regionEnd();
    }

    @Override
    public boolean hasTransparentBounds() {
        return myMatcher.hasTransparentBounds();
    }

    @Override
    public RegExMatcher useTransparentBounds(final boolean b) {
        myMatcher.useTransparentBounds(b);
        return this;
    }

    @Override
    public boolean hasAnchoringBounds() {
        return myMatcher.hasAnchoringBounds();
    }

    @Override
    public RegExMatcher useAnchoringBounds(final boolean b) {
        myMatcher.useAnchoringBounds(b);
        return this;
    }

    @Override
    public boolean hitEnd() {
        return myMatcher.hitEnd();
    }

    @Override
    public boolean requireEnd() {
        return myMatcher.requireEnd();
    }
}
