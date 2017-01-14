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
    private ReverseCharSequence myText;
    private Matcher myMatcher;

    /**
     * Constructor of reversed matcher that takes a ReversedRegEx instance and an input character sequence.
     * if input is not an instance of ReverseCharSequence then it is wrapped in ReversedCharSequence class.
     *
     * @param reversedRegEx reversed regex instance
     * @param input char sequence to use as input for matching
     */
    public ReverseMatcher(final ReversedRegEx reversedRegEx, final CharSequence input) {
        myReversedRegEx = reversedRegEx;
        myText = input instanceof ReverseCharSequence ? (ReverseCharSequence) input : ReversedCharSequence.of(input);
        myMatcher = Pattern.compile(myReversedRegEx.pattern(), myReversedRegEx.getFlags()).matcher(myText);
    }

    public ReversedRegEx reversedRegEx() {
        return myReversedRegEx;
    }

    public ReverseCharSequence reversedText() {
        return myText;
    }

    public CharSequence text() {
        return myText.getReversedChars();
    }

    public Pattern pattern() {
        return myMatcher.pattern();
    }

    public MatchResult toMatchResult() {
        return this;
    }

    /**
     * Does not reverse the string buffer, only the replacement param
     * when all appending is done the buffer result should be reversed
     *
     * @param sb          target string buffer
     * @param replacement replacement string
     * @return this for chaining
     */
    public ReverseMatcher appendReplacement(final StringBuffer sb, final String replacement) {
        myMatcher.appendReplacement(sb, reversedString(replacement));
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
        myMatcher.usePattern(Pattern.compile(myReversedRegEx.pattern(), myReversedRegEx.getFlags()));
        return this;
    }

    public ReverseMatcher reset() {
        myMatcher.reset();
        return this;
    }

    public ReverseMatcher reset(final CharSequence input) {
        myText = input instanceof ReversedCharSequence ? (ReversedCharSequence) input : ReversedCharSequence.of(input);
        myMatcher.reset(myText);
        return this;
    }

    @Override
    public int start() {
        return myText.mapBoundary(myMatcher.end());
    }

    @Override
    public int start(final int group) {
        return myText.mapBoundary(myMatcher.end(myReversedRegEx.getReversedGroupIndex(group)));
    }

    @Override
    public int end() {
        return myText.mapBoundary(myMatcher.start());
    }

    @Override
    public int end(final int group) {
        return myText.mapBoundary(myMatcher.start(myReversedRegEx.getReversedGroupIndex(group)));
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
        myMatcher.region(myMatcher.regionStart(), myText.mapBoundary(start));
        return myMatcher.find();
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

    public ReverseMatcher region(final int start, final int end) {
        myMatcher.region(myText.mapBoundary(end), myText.mapBoundary(start));
        return this;
    }

    public int regionStart() {
        return myText.mapBoundary(myMatcher.regionEnd());
    }

    public int regionEnd() {
        return myText.mapBoundary(myMatcher.regionStart());
    }

    public boolean hasTransparentBounds() {
        return myMatcher.hasTransparentBounds();
    }

    public ReverseMatcher useTransparentBounds(final boolean b) {
        myMatcher.useTransparentBounds(b);
        return this;
    }

    public boolean hasAnchoringBounds() {
        return myMatcher.hasAnchoringBounds();
    }

    public ReverseMatcher useAnchoringBounds(final boolean b) {
        myMatcher.useAnchoringBounds(b);
        return this;
    }

    public boolean hitEnd() {
        return myMatcher.hitEnd();
    }

    public boolean requireEnd() {
        return myMatcher.requireEnd();
    }
}
