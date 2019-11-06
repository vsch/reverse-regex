package com.vladsch.ReverseRegEx.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class that reverses coordinates for reverse direction matching
 * also changes the group numbers from original pattern to reversed pattern groups
 */
public class ReverseMatcher implements RegExMatcher {
    private ReversePattern myReversePattern;
    private ReverseCharSequence myText;
    private Matcher myMatcher;

    /**
     * Constructor of reversed matcher that takes a ReversedRegEx instance and an input character sequence.
     * if input is not an instance of ReverseCharSequence then it is wrapped in ReversedCharSequence class.
     *
     * @param reversePattern reversed regex instance
     * @param input          char sequence to use as input for matching
     */
    public ReverseMatcher(final ReversePattern reversePattern, final CharSequence input) {
        myReversePattern = reversePattern;
        myText = input instanceof ReverseCharSequence ? (ReverseCharSequence) input : ReversedCharSequence.of(input);
        myMatcher = Pattern.compile(myReversePattern.pattern(), myReversePattern.flags()).matcher(myText);
    }

    public ReversePattern reversedRegEx() {
        return myReversePattern;
    }

    public ReverseCharSequence reversedText() {
        return myText;
    }

    public CharSequence text() {
        return myText.getReversedChars();
    }

    @Override
    public Pattern pattern() {
        return myMatcher.pattern();
    }

    @Override
    public MatchResult toMatchResult() {
        return this;
    }

    /**
     * Does not reverse the string buffer, only the replacement param
     * when all appending is done the buffer result should be reversed
     *
     * @param sb          target string buffer
     * @param replacement replacement string
     *
     * @return this for chaining
     */
    @Override
    public ReverseMatcher appendReplacement(final StringBuffer sb, final String replacement) {
        myMatcher.appendReplacement(sb, reversedString(replacement));
        return this;
    }

    /**
     * Does not reverse the string buffer, only the replacement param
     *
     * @param sb target string buffer
     *
     * @return target string buffer
     */
    @Override
    public StringBuffer appendTail(final StringBuffer sb) {
        return myMatcher.appendTail(sb);
    }

    public ReverseMatcher usePattern(final ReversePattern reversePattern) {
        myReversePattern = reversePattern;
        myMatcher.usePattern(Pattern.compile(myReversePattern.pattern(), myReversePattern.flags()));
        return this;
    }

    @Override
    public ReverseMatcher reset() {
        myMatcher.reset();
        return this;
    }

    @Override
    public ReverseMatcher reset(final CharSequence input) {
        myText = input instanceof ReverseCharSequence ? (ReverseCharSequence) input : ReversedCharSequence.of(input);
        myMatcher.reset(myText);
        return this;
    }

    @Override
    public int start() {
        return myText.mapBoundary(myMatcher.end());
    }

    @Override
    public int start(final int group) {
        int groupIndex = myReversePattern.getReversedGroupIndex(group);
        int end = myMatcher.end(groupIndex);
        return myText.mapBoundary(end);
    }

    @Override
    public int end() {
        return myText.mapBoundary(myMatcher.start());
    }

    @Override
    public int end(final int group) {
        int groupIndex = myReversePattern.getReversedGroupIndex(group);
        int start = myMatcher.start(groupIndex);
        return myText.mapBoundary(start);
    }

    public static String reversedString(CharSequence s) {
        return s == null ? null : ReversedCharSequence.of(s).toString();
    }

    @Override
    public String group() {
        return reversedString(myMatcher.group());
    }

    @Override
    public String group(final int group) {
        return reversedString(myMatcher.group(myReversePattern.getReversedGroupIndex(group)));
    }

    @Override
    public String group(final String name) {
        return reversedString(myMatcher.group(name));
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
        myMatcher.region(myMatcher.regionStart(), myText.mapBoundary(start));
        return myMatcher.find();
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
        return reversedString(myMatcher.replaceAll(reversedString(replacement)));
    }

    @Override
    public String replaceFirst(final String replacement) {
        return reversedString(myMatcher.replaceFirst(reversedString(replacement)));
    }

    @Override
    public ReverseMatcher region(final int start, final int end) {
        myMatcher.region(myText.mapBoundary(end), myText.mapBoundary(start));
        return this;
    }

    @Override
    public int regionStart() {
        return myText.mapBoundary(myMatcher.regionEnd());
    }

    @Override
    public int regionEnd() {
        return myText.mapBoundary(myMatcher.regionStart());
    }

    @Override
    public boolean hasTransparentBounds() {
        return myMatcher.hasTransparentBounds();
    }

    @Override
    public ReverseMatcher useTransparentBounds(final boolean b) {
        myMatcher.useTransparentBounds(b);
        return this;
    }

    @Override
    public boolean hasAnchoringBounds() {
        return myMatcher.hasAnchoringBounds();
    }

    @Override
    public ReverseMatcher useAnchoringBounds(final boolean b) {
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
