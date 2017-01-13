package com.vladsch.ReverseRegEx.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class that reverses coordinates for reverse direction matching
 * also changes the group numbers from original pattern to reversed pattern groups
 */
public class ReverseMatcher implements MatchResult {
    private ReversedRegEx myReversedRegEx;
    private ReversedCharSequence myText;
    private Matcher myMatcher;

    private int mapOffset(int index) {
        return myText.reversedIndex(index);
    }

    public ReverseMatcher(final ReversedRegEx reversedRegEx, final CharSequence text) {
        myReversedRegEx = reversedRegEx;
        myText = ReversedCharSequence.of(text);
        myMatcher = Pattern.compile(myReversedRegEx.getReversed(), myReversedRegEx.getFlags()).matcher(myText);
    }

    public ReversedRegEx getReversedRegEx() {
        return myReversedRegEx;
    }

    public ReversedCharSequence getReversedText() {
        return myText;
    }

    public CharSequence getText() {
        return myText.getChars();
    }

    public Pattern pattern() {
        return myMatcher.pattern();
    }

    public MatchResult toMatchResult() {
        return this;
    }

    /**
     * Does not reverse the string buffer, only the replacement param
     *
     * @param sb target string buffer
     * @param replacement replacement string
     * @return this for chaining
     */
    public ReverseMatcher appendReplacement(final StringBuffer sb, final String replacement) {
        myMatcher = myMatcher.appendReplacement(sb, reversedString(replacement));
        return this;
    }

    /**
     * Does not reverse the string buffer, only the replacement param
     *
     * @param sb target string buffer
     * @return target string buffer
     */
    public StringBuffer appendTail(final StringBuffer sb) {
        return myMatcher.appendTail(sb);
    }

    public ReverseMatcher usePattern(final ReversedRegEx reversedRegEx) {
        myReversedRegEx = reversedRegEx;
        myMatcher = myMatcher.usePattern(Pattern.compile(myReversedRegEx.getReversed(), myReversedRegEx.getFlags()));
        return this;
    }

    public ReverseMatcher reset() {
        myMatcher = myMatcher.reset();
        return this;
    }

    public ReverseMatcher reset(final CharSequence input) {
        myText = ReversedCharSequence.of(input);
        myMatcher = myMatcher.reset(myText);
        return this;
    }

    @Override
    public int start() {
        return mapOffset(myMatcher.end());
    }

    @Override
    public int start(final int group) {
        return mapOffset(myMatcher.end(myReversedRegEx.getReversedGroupIndex(group)));
    }

    @Override
    public int end() {
        return mapOffset(myMatcher.start());
    }

    @Override
    public int end(final int group) {
        return mapOffset(myMatcher.start(myReversedRegEx.getReversedGroupIndex(group)));
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
        return reversedString(myMatcher.group(myReversedRegEx.getReversedGroupIndex(group)));
    }

    public String group(final String name) {
        return reversedString(myMatcher.group(name));
    }

    @Override
    public int groupCount() {
        return myMatcher.groupCount();
    }

    public boolean matches() {
        return myMatcher.matches();
    }

    public boolean find() {
        return myMatcher.find();
    }

    public boolean find(final int start) {
        return myMatcher.find(mapOffset(start));
    }

    public boolean lookingAt() {
        return myMatcher.lookingAt();
    }

    public static String quoteReplacement(final String s) {
        return Matcher.quoteReplacement(s);
    }

    public String replaceAll(final String replacement) {
        return reversedString(myMatcher.replaceAll(reversedString(replacement)));
    }

    public String replaceFirst(final String replacement) {
        return reversedString(myMatcher.replaceFirst(reversedString(replacement)));
    }

    public Matcher region(final int start, final int end) {
        myMatcher = myMatcher.region(mapOffset(end), mapOffset(start));
        return myMatcher;
    }

    public int regionStart() {
        return mapOffset(myMatcher.regionEnd());
    }

    public int regionEnd() {
        return mapOffset(myMatcher.regionStart());
    }

    public boolean hasTransparentBounds() {
        return myMatcher.hasTransparentBounds();
    }

    public ReverseMatcher useTransparentBounds(final boolean b) {
        myMatcher = myMatcher.useTransparentBounds(b);
        return this;
    }

    public boolean hasAnchoringBounds() {
        return myMatcher.hasAnchoringBounds();
    }

    public ReverseMatcher useAnchoringBounds(final boolean b) {
        myMatcher = myMatcher.useAnchoringBounds(b);
        return this;
    }

    public boolean hitEnd() {
        return myMatcher.hitEnd();
    }

    public boolean requireEnd() {
        return myMatcher.requireEnd();
    }
}
