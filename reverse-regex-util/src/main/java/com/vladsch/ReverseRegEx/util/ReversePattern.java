/*
 * Copyright (c) 2016-2017 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * RegEx Parser based on Java Open JDK Pattern Syntax Parser
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 */

package com.vladsch.ReverseRegEx.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*
 * Will convert a regular expression into an equivalent regular expression to be used on a reversed char sequence to perform backwards
 * search that will result in the same matches as the original pattern would when applied to non-reversed sequence, but the order of these
 * matches will be reversed.
 * <p>
 * Supports converting all regex syntax to reversed syntax except \Z which has no corresponding "Start" sequence
 * <p>
 * <h3>Modifier Flags:</h3>
 * (?d)                                                      - {@link java.util.regex.Pattern#UNIX_LINES}<br/>
 * (?i)                                                      - {@link java.util.regex.Pattern#CASE_INSENSITIVE}<br/>
 * (?m)                                                      - {@link java.util.regex.Pattern#MULTILINE}<br/>
 * (?s)                                                      - {@link java.util.regex.Pattern#DOTALL}<br/>
 * (?u)                                                      - {@link java.util.regex.Pattern#UNICODE_CASE}<br/>
 * (?x)                                                      - {@link java.util.regex.Pattern#COMMENTS}<br/>
 * (?U)                                                      - {@link java.util.regex.Pattern#UNICODE_CHARACTER_CLASS}<br/>
 * <p>
 * Flags without modifier pattern: {@link Pattern#CANON_EQ}, {@link Pattern#LITERAL}
 * <p>
 * x                                                      - The character x<br/>
 * \\                                                      - The backslash character<br/>
 * \0n                                                      - The character with octal value 0n (0 <= n <= 7)<br/>
 * \0nn                                                      - The character with octal value 0nn (0 <= n <= 7)<br/>
 * \0mnn                                                      - The character with octal value 0mnn (0 <= m <= 3, 0 <= n <= 7)<br/>
 * \xhh                                                      - The character with hexadecimal value 0xhh<br/>
 * \\uhhhh                                                      - The character with hexadecimal value 0xhhhh<br/>
 * \x{h...h}                                                      - The character with hexadecimal value 0xh...h (Character.MIN_CODE_POINT  <= 0xh...h <=  Character.MAX_CODE_POINT)<br/>
 * \t                                                      - The tab character ('&#92;u0009')<br/>
 * \n                                                      - The newline (line feed) character ('&#92;u000A')<br/>
 * \r                                                      - The carriage-return character ('&#92;u000D')<br/>
 * \f                                                      - The form-feed character ('&#92;u000C')<br/>
 * \a                                                      - The alert (bell) character ('&#92;u0007')<br/>
 * \e                                                      - The escape character ('&#92;u001B')<br/>
 * \cx                                                      - The control character corresponding to x<br/>
 * <h3>Character classes</h3>
 * [abc]                                                                 - a, b, or c (simple class)<br/>
 * [^abc]                                                                - Any character except a, b, or c (negation)<br/>
 * [a-zA-Z]                                                              - a through z or A through Z, inclusive (range)<br/>
 * [a-d[m-p]]                                                            - a through d, or m through p: [a-dm-p] (union)<br/>
 * [a-z&&[def]]                                                          - d, e, or f (intersection)<br/>
 * [a-z&&[^bc]]                                                          - a through z, except for b and c: [ad-z] (subtraction)<br/>
 * [a-z&&[^m-p]]                                                         -   a through z, and not m through p: [a-lq-z](subtraction)<br/>
 * <h3>Predefined character classes</h3>
 * .                                                      - Any character (may or may not match line terminators)<br/>
 * \d                                                      - A digit: [0-9]<br/>
 * \D                                                      - A non-digit: [^0-9]<br/>
 * \s                                                      - A whitespace character: [ &#92;t&#92;n&#92;x0B&#92;f&#92;r]<br/>
 * \S                                                      - A non-whitespace character: [^&#92;s]<br/>
 * \v                                                      - A vertical whitespace character: [&#92;n&#92;x0B&#92;f&#92;r&#92;x85&#92;u2028&#92;u2029]<br/>
 * \V                                                      - A non-vertical whitespace character: [^&#92;v]<br/>
 * \w                                                      - A word character: [a-zA-Z_0-9]<br/>
 * \W                                                      - A non-word character: [^\w]<br/>
 * <h3>POSIX character classes (US-ASCII only)</h3>
 * \p{Lower}                                                      - A lower-case alphabetic character: [a-z]<br/>
 * \p{Upper}                                                      - An upper-case alphabetic character:[A-Z]<br/>
 * \p{ASCII}                                                      - All ASCII:[&#92;x00-&#92;x7F]<br/>
 * \p{Alpha}                                                      - An alphabetic character:[&#92;p{Lower}&#92;p{Upper}]<br/>
 * \p{Digit}                                                      - A decimal digit: [0-9]<br/>
 * \p{Alnum}                                                      - An alphanumeric character:[&#92;p{Alpha}&#92;p{Digit}]<br/>
 * \p{Punct}                                                      - Punctuation: One of !"#$%&'()*+,-./:;<=>?@[&#92;]^_`{|}~<br/>
 * \p{Graph}                                                      - A visible character: [&#92;p{Alnum}&#92;p{Punct}]<br/>
 * \p{Print}                                                      - A printable character: [\p{Graph}\x20]<br/>
 * \p{Blank}                                                      - A space or a tab: [ &#92;t]<br/>
 * \p{Cntrl}                                                      - A control character: [&#92;x00-&#92;x1F&#92;x7F]<br/>
 * \p{XDigit}                                                      - A hexadecimal digit: [0-9a-fA-F]<br/>
 * \p{Space}                                                      - A whitespace character: [ &#92;t&#92;n&#92;x0B&#92;f&#92;r]<br/>
 * <h3>java.lang.Character classes (simple java character type)</h3>
 * \p{javaLowerCase}                                                      - Equivalent to java.lang.Character.isLowerCase()<br/>
 * \p{javaUpperCase}                                                      - Equivalent to java.lang.Character.isUpperCase()<br/>
 * \p{javaWhitespace}                                                      - Equivalent to java.lang.Character.isWhitespace()<br/>
 * \p{javaMirrored}                                                      - Equivalent to java.lang.Character.isMirrored()<br/>
 * <h3>Classes for Unicode scripts, blocks, categories and binary properties</h3>
 * \p{IsLatin}                                                      - A Latin script character (script)<br/>
 * \p{InGreek}                                                      - A character in the Greek block (block)<br/>
 * \p{Lu}                                                      - An uppercase letter (category)<br/>
 * \p{IsAlphabetic}                                                      - An alphabetic character (binary property)<br/>
 * \p{Sc}                                                      - A currency symbol<br/>
 * \P{InGreek}                                                      - Any character except one in the Greek block (negation)<br/>
 * [\p{L}&&[^\p{Lu}]]                                                      - Any letter except an uppercase letter (subtraction)<br/>
 * <h3>Boundary matchers</h3>
 * ^                                                      - The beginning of a line<br/>
 * $                                                      - The end of a line<br/>
 * \b                                                      - A word boundary<br/>
 * \B                                                      - A non-word boundary<br/>
 * \A                                                      - The beginning of the input<br/>
 * \G                                                      - The end of the previous match<br/>
 * \Z                                                      - The end of the input but for the final terminator, if any<br/>
 * \z                                                      - The end of the input<br/>
 * <h3>Greedy quantifiers</h3>
 * X?                                                      - X, once or not at all<br/>
 * X*                                                      - X, zero or more times<br/>
 * X+                                                      - X, one or more times<br/>
 * X{n}                                                      - X, exactly n times<br/>
 * X{n,}                                                      - X, at least n times<br/>
 * X{n,m}                                                      - X, at least n but not more than m times<br/>
 * <h3>Reluctant quantifiers</h3>
 * X??                                                      - X, once or not at all<br/>
 * X*?                                                      - X, zero or more times<br/>
 * X+?                                                      - X, one or more times<br/>
 * X{n}?                                                      - X, exactly n times<br/>
 * X{n,}?                                                      - X, at least n times<br/>
 * X{n,m}?                                                      - X, at least n but not more than m times<br/>
 * <h3>Possessive quantifiers</h3>
 * X?+                                                      - X, once or not at all<br/>
 * X*+                                                      - X, zero or more times<br/>
 * X++                                                      - X, one or more times<br/>
 * X{n}+                                                      - X, exactly n times<br/>
 * X{n,}+                                                      - X, at least n times<br/>
 * X{n,m}+                                                      - X, at least n but not more than m times<br/>
 * <h3>Logical operators</h3>
 * XY                                                      - X followed by Y<br/>
 * X|Y                                                      - Either X or Y<br/>
 * (X)                                                      - X, as a capturing group<br/>
 * <h3>Back references</h3>
 * \n                                                      - Whatever the nth capturing group matched<br/>
 * \k<name>                                                      - Whatever the named-capturing group "name" matched<br/>
 * <h3>Quotation</h3>
 * \                                                      - Nothing, but quotes the following character<br/>
 * \Q                                                      - Nothing, but quotes all characters until \E<br/>
 * \E                                                      - Nothing, but ends quoting started by \Q<br/>
 * <h3>Special constructs (named-capturing and non-capturing)</h3>
 * (?<name>X)                                                      - X, as a named-capturing group<br/>
 * (?:X)                                                      - X, as a non-capturing group<br/>
 * (?idmsuxU-idmsuxU)                                                      - Nothing, but turns match flags i d m s u x U on, or when after                                                      - to off<br/>
 * (?idmsux-idmsux:X)                                                      - X, as a non-capturing group with the given flags i d m s u x on, or when after                                                      - to off<br/>
 * (?=X)                                                      - X, via zero-width positive lookahead<br/>
 * (?!X)                                                      - X, via zero-width negative lookahead<br/>
 * (?&lt;=X)                                                      - X, via zero-width positive lookbehind<br/>
 * (?&lt;!X)                                                      - X, via zero-width negative lookbehind<br/>
 * (?&gt;X)                                                      - X, as an independent, non-capturing group<br/>
 * <h2>Conversion Operation:</h2>
 * X                                                      - original pattern<br/>
 * rX                                                      - reverse of X<br/>
 * <p>
 * If X consists of a single character x then X === rX for all others rX is the sequence of characters in X, reversed.
 * <h3>Logical Operators:</h3>
 * XY                                                      - converted to rYrX<br/>
 * X|Y                                                      - converted to rY|rX to ensure that order dependent matches will be reflected in reversed search<br/>
 * (X)                                                      - converted to (rX)<br/>
 * ^                                                      - converted to $<br/>
 * $                                                      - converted to ^<br/>
 * \A                                                      - converted to \z<br/>
 * \z                                                      - converted to \A<br/>
 * \Z                                                      - converted to \A<br/>
 * (?=X)                                                      - converted to (?&lt;=rX), zero-width positive lookbehind<br/>
 * (?!X)                                                      - converted to (?&lt;!rX), zero-width negative lookbehind<br/>
 * (?&lt;=X)                                                      - converted to (?=rX), zero-width positive lookahead<br/>
 * (?&lt;!X)                                                      - converted to (?!rX), zero-width negative lookahead<br/>
 * <h4>If LITERAL flag is not used then:</h4>
 * \Q                                                      - converted to \E so as to preserve quoting operations<br/>
 * \E                                                      - converted to \Q so as to preserve quoting operations<br/>
 * <h4>If COMMENTS flag is used then</h4>
 * #.*\n is left as is otherwise it is treated as a normal sequence
 * <h3>Back Reference Conversion and capturing group conversion:</h3>
 * Numbered Capturing groups have to be re-numbered because their appearance in the reversed
 * pattern will not in general correspond to the the same group number as in the original pattern.
 * <p>
 * A map is created to convert original to reversed group numbers and vice versa.
 * <p>
 * This affects back references in two ways:
 * <p>
 * 1. Back references have to be mapped from original to reversed group number and
 * <p>
 * 2. the last back reference in the original pattern must be converted to a capturing
 * group of the corresponding back reference in the original pattern,
 * and original capturing group must be converted to a reversed back reference number.
 * <p>
 * Named back references are also affected in that the last back reference in the original
 * pattern has to be changed to a named capturing group and the named capturing group to a named back
 * reference, since in reverse it will be the last back reference that will be encountered
 * first.
 */

public final class ReversePattern implements RegExPattern {
    /**
     * The original regular-expression pattern string.
     */
    private String pattern;

    /**
     * The reversed regular-expression or original pattern string.
     */
    private String reversed;

    /**
     * The original pattern flags.
     */
    private int flags;

    /**
     * The number of capturing groups in this Pattern. Updated as the sequence is parsed
     */
    private int capturingGroupCount;

    /**
     * Original to reversed capturing group numbers
     */
    private int[] originalToReversedGroups;
    /**
     * Reversed to original capturing group numbers
     */
    private int[] reversedToOriginalGroups;

    private Pattern compiled;

    private ReversePattern(String p, int f) {
        pattern = p;
        flags = f;
        compiled = null;

        // to use UNICODE_CASE if UNICODE_CHARACTER_CLASS present
        if ((flags & Pattern.UNICODE_CHARACTER_CLASS) != 0)
            flags |= Pattern.UNICODE_CASE;

        // Reset group index count
        capturingGroupCount = 1;

        if (pattern.length() > 0) {
            parse();
            reverse();
            release();
        } else {
            reversed = "";
        }
    }

    @Override
    public Pattern compiled() {
        if (compiled == null) {
            synchronized (this) {
                compiled = Pattern.compile(reversed, flags);
            }
        }
        return compiled;
    }

    public String toString() {
        return reversed;
    }

    public static ReversePattern compile(String p) {
        //noinspection UnnecessaryLocalVariable
        ReversePattern regEx = new ReversePattern(p, 0);
        return regEx;
    }

    public static ReversePattern compile(String p, int f) {
        //noinspection UnnecessaryLocalVariable
        ReversePattern regEx = new ReversePattern(p, f);
        return regEx;
    }

    @Override
    public ReverseMatcher matcher(CharSequence input) {
        return new ReverseMatcher(this, ReversedCharSequence.of(input));
    }

    public static boolean matches(String regex, CharSequence input) {
        RegExPattern p = compile(regex);
        RegExMatcher m = p.matcher(input);
        return m.matches();
    }

    @Override
    public String[] split(CharSequence input, int limit) {
        Pattern p = compiled();
        String[] result = p.split(ReversedCharSequence.of(input), limit);
        for (int i = 0; i < result.length; i++) {
            result[i] = ReversedCharSequence.of(result[i]).toString();
        }
        return result;
    }

    @Override
    public String[] split(CharSequence input) {
        return split(input, 0);
    }

    public String originalPattern() {
        return pattern;
    }

    @Override
    public String pattern() {
        return reversed;
    }

    @Override
    public int flags() {
        return flags;
    }

    public int getCapturingGroupCount() {
        return capturingGroupCount;
    }

    public int getReversedGroupIndex(int group) {
        return group <= 0 ? group : originalToReversedGroups[group - 1] + 1;
    }

    public int getOriginalGroupIndex(int group) {
        return group <= 0 ? group : reversedToOriginalGroups[group - 1] + 1;
    }

    public int getOriginalNamedGroupIndex(String groupName) {
        //noinspection UnnecessaryLocalVariable
        int group = getGroupNameIndex(groupName);
        return group <= 0 ? group : group + 1;
    }

    public int getReversedNamedGroupIndex(String groupName) {
        int group = getGroupNameIndex(groupName);
        return group <= 0 ? group : originalToReversedGroups[group] + 1;
    }

    /**
     * List of reversed expressions in original order, to create a reverse
     * pattern it is necessary to concatenate these in reverse order while replacing
     * back references and capturing groups
     */
    private CharSequence[] sequences;

    /**
     * List of indices into sequences that will assemble into a reversed pattern
     */
    private CharSequence[] reversedSequences;

    /**
     * List of indices in sequences of original capturing group numbers [startIndex, endIndex)
     */
    private int[] capturingGroupStartIndices;
    private int[] capturingGroupEndIndices;

    /**
     * if capturing group is named then here will be it's index+1
     */
    private int[] capturingGroupNameIndicesP1;

    /**
     * List of indices in sequences of original named capturing groups [startIndex, endIndex)
     */
    private CharSequence[] namedGroups;

    /**
     * Named group index to capturing group index
     */
    private int[] namedGroupNumbers;

    /**
     * List of indices to sequences of original back references to original group numbers
     */
    private int[] backReferenceIndices;
    private int[] backReferenceGroups;
    private int[] backReferenceReversedIndicesP1;
    private boolean[] namedBackReferences;

    /**
     * Index+1 of back references which should replace the group reference
     */
    private int[] groupToBackReferenceP1;

    private static final int SEQUENCE = 0;
    private static final int GROUP = 1;
    private static final int BACK_REFERENCE = 2;

    // index into indices for next copy
    private int index;

    // indices of reverse traversal of the sequence uses these to figure out
    // what is being processed
    private transient int sequenceIndex;
    private transient int capturedGroupIndex;
    private transient int namedGroupIndex;
    private transient int backReferenceIndex;
    private transient int patternLength;
    /**
     * Index into the pattern string that keeps track of how much has been
     * parsed.
     */
    private transient int cursor;

    // next index of reversed group
    private transient int reversedGroupIndex;

    private int getGroupNameIndex(String groupName) {
        for (int i = 0; i < namedGroupIndex; i++) {
            if (groupName.equals(namedGroups[i])) return i;
        }
        return -1;
    }

    private transient int groupEnd;
    private transient int backReferenceEnd;

    private int getNextType() {
        if (capturedGroupIndex >= 0 && capturingGroupEndIndices[capturedGroupIndex] == sequenceIndex + 1) {
            groupEnd = capturedGroupIndex;
            return GROUP;
        }
        if (backReferenceIndex >= 0 && backReferenceIndices[backReferenceIndex] == sequenceIndex) {
            // back reference
            backReferenceEnd = backReferenceIndex;
            return BACK_REFERENCE;
        }

        // may need to check previous capturedGroupEndIndices if right now replacing a group
        for (int i = capturedGroupIndex; i-- > 0; ) {
            if (capturingGroupEndIndices[i] > sequenceIndex + 1) continue;

            if (capturingGroupEndIndices[i] == sequenceIndex + 1) {
                groupEnd = i;
                return GROUP;
            }
            break;
        }

        // may need to check previous backReferenceIndices if right now replacing a group
        for (int i = backReferenceIndex; i-- > 0; ) {
            if (backReferenceIndices[i] > sequenceIndex) continue;

            if (backReferenceIndices[i] == sequenceIndex) {
                backReferenceEnd = i;
                return BACK_REFERENCE;
            }
            break;
        }
        return SEQUENCE;
    }

    private void release() {
        // release intermediate storage
        sequences = null;
        reversedSequences = null;
        capturingGroupStartIndices = null;
        capturingGroupEndIndices = null;
        capturingGroupNameIndicesP1 = null;
        backReferenceIndices = null;
        backReferenceGroups = null;
        backReferenceReversedIndicesP1 = null;
        namedBackReferences = null;
    }

    private void initCopy() {
        sequenceIndex = sequences.length;
        capturedGroupIndex = capturingGroupEndIndices.length;
        namedGroupIndex = namedGroups.length;
        backReferenceIndex = backReferenceIndices.length;

        groupToBackReferenceP1 = new int[capturingGroupCount - 1];
        backReferenceReversedIndicesP1 = new int[backReferenceIndices.length];

        originalToReversedGroups = new int[capturingGroupCount - 1];
        reversedToOriginalGroups = new int[capturingGroupCount - 1];
        reversedSequences = new CharSequence[sequences.length];
        index = 0;
        reversedGroupIndex = 0;

        sequenceIndex--;
        capturedGroupIndex--;
        namedGroupIndex--;
        backReferenceIndex--;
    }

    private void copyRange(final int startIndex, final int endIndex) {
        int savedIndex = sequenceIndex;
        sequenceIndex = endIndex - 1;

        // copy opening sequence
        if (reversedSequences.length <= index) {
            reversedSequences = grow(reversedSequences, reversedSequences.length);
        }

        reversedSequences[index++] = sequences[sequenceIndex--];

        while (sequenceIndex > startIndex + 1) {
            copy();
        }

        if (reversedSequences.length <= index + 2) {
            reversedSequences = grow(reversedSequences, reversedSequences.length);
        }

        // copy close sequence
        reversedSequences[index++] = sequences[sequenceIndex--];

        // copy closure
        reversedSequences[index++] = sequences[sequenceIndex--];

        sequenceIndex = savedIndex;
    }

    private void copy() {
        int type = getNextType();
        switch (type) {
            case GROUP: {
                int group = groupEnd;
                if (groupEnd == capturedGroupIndex) capturedGroupIndex--;

                if (groupToBackReferenceP1[group] != 0) {
                    // replace by back reference to this group
                    sequenceIndex = capturingGroupStartIndices[group];
                    sequenceIndex--;

                    if (reversedSequences.length <= index + 3) reversedSequences = grow(reversedSequences, reversedSequences.length);
                    reversedSequences[index++] = "";
                    reversedSequences[index++] = sequences[backReferenceIndices[groupToBackReferenceP1[group] - 1]];
                    backReferenceReversedIndicesP1[groupToBackReferenceP1[group] - 1] = index;
                    reversedSequences[index++] = "";
                } else {
                    // copy the group
                    groupToBackReferenceP1[group] = -1;
                    originalToReversedGroups[group] = reversedGroupIndex++;
                    sequenceIndex = capturingGroupStartIndices[group];
                    sequenceIndex--;
                    copyRange(capturingGroupStartIndices[group], capturingGroupEndIndices[group]);
                }
            }
            break;

            case BACK_REFERENCE: {
                int backReference = backReferenceEnd;
                int group = backReferenceGroups[backReference];

                if (backReferenceEnd == backReferenceIndex) backReferenceIndex--;

                if (groupToBackReferenceP1[group] == 0) {
                    // need to copy the group and change group to back reference
                    groupToBackReferenceP1[group] = backReference + 1;
                    sequenceIndex--;
                    originalToReversedGroups[group] = reversedGroupIndex++;
                    copyRange(capturingGroupStartIndices[group], capturingGroupEndIndices[group]);
                } else {
                    // just copy the back reference
                    if (reversedSequences.length <= index + 3) reversedSequences = grow(reversedSequences, reversedSequences.length);
                    reversedSequences[index++] = "";
                    reversedSequences[index++] = sequences[sequenceIndex--];
                    backReferenceReversedIndicesP1[backReference] = index;
                    reversedSequences[index++] = "";
                }
            }
            break;

            case SEQUENCE:
                reversedSequences[index++] = sequences[sequenceIndex--];
                break;
        }
    }

    private void reverse() {
        initCopy();

        while (sequenceIndex >= 0) {
            copy();
        }

        // we now need to map back references to the new group numbers
        int capturedGroups = capturingGroupEndIndices.length;
        if (capturedGroups > 0) {
            String[] reversedBackReferences = new String[capturedGroups];
            for (int i = 0; i < capturedGroups; i++) {
                reversedBackReferences[i] = "\\" + (originalToReversedGroups[i] + 1);
            }

            for (int i = 0; i < backReferenceReversedIndicesP1.length; i++) {
                if (namedBackReferences[i]) continue;

                int group = backReferenceGroups[i];
                int reversedIndex = backReferenceReversedIndicesP1[i];
                if (reversedIndex == 0) continue;

                // change back reference to reversed group number
                reversedSequences[reversedIndex - 1] = reversedBackReferences[group];

                // see if need to wrap in (?: ) if followed by digit, after spacer
                int nextIndex = reversedIndex;
                CharSequence chars = null;
                while (++nextIndex < reversedSequences.length && (chars = reversedSequences[nextIndex]) != null && chars.length() == 0) { }
                if (chars != null && chars.length() > 0 && Character.isDigit(chars.charAt(0))) {
                    // add wrapper
                    reversedSequences[reversedIndex - 2] = "(?:";
                    reversedSequences[reversedIndex] = ")";
                }
            }

            // create a reverse group to original index
            for (int i = 0; i < originalToReversedGroups.length; i++) {
                reversedToOriginalGroups[originalToReversedGroups[i]] = i;
            }
        }

        StringBuilder sb = new StringBuilder(pattern.length());
        for (CharSequence reversedSequence : reversedSequences) {
            if (reversedSequence == null) break;
            sb.append(reversedSequence);
        }

        reversed = sb.toString();
    }

    private void initParse() {
        patternLength = pattern.length();

        sequenceIndex = 0;
        capturedGroupIndex = 0;
        namedGroupIndex = 0;
        backReferenceIndex = 0;

        index = 0;

        // allocate some initial values
        sequences = new CharSequence[200];

        capturingGroupStartIndices = new int[10];
        capturingGroupEndIndices = new int[10];
        capturingGroupNameIndicesP1 = new int[10];

        namedGroups = new CharSequence[10];
        namedGroupNumbers = new int[10];

        backReferenceGroups = new int[10];
        backReferenceIndices = new int[10];
        namedBackReferences = new boolean[10];
    }

    private CharSequence[] grow(CharSequence[] array, int step) {
        CharSequence[] newArray = new CharSequence[array.length + step];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    private int[] grow(int[] array, int step) {
        int[] newArray = new int[array.length + step];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    private boolean[] grow(boolean[] array, int step) {
        boolean[] newArray = new boolean[array.length + step];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    private CharSequence[] truncate(CharSequence[] array, int size) {
        if (array.length > size) {
            CharSequence[] newArray = new CharSequence[size];
            System.arraycopy(array, 0, newArray, 0, size);
            return newArray;
        }
        return array;
    }

    private int[] truncate(int[] array, int size) {
        if (array.length > size) {
            int[] newArray = new int[size];
            System.arraycopy(array, 0, newArray, 0, size);
            return newArray;
        }
        return array;
    }

    private boolean[] truncate(boolean[] array, int size) {
        if (array.length > size) {
            boolean[] newArray = new boolean[size];
            System.arraycopy(array, 0, newArray, 0, size);
            return newArray;
        }
        return array;
    }

    private void finalizeParse() {
        sequences = truncate(sequences, sequenceIndex);

        capturingGroupStartIndices = truncate(capturingGroupStartIndices, capturedGroupIndex);
        capturingGroupEndIndices = truncate(capturingGroupEndIndices, capturedGroupIndex);
        capturingGroupNameIndicesP1 = truncate(capturingGroupNameIndicesP1, capturedGroupIndex);

        namedGroups = truncate(namedGroups, namedGroupIndex);
        namedGroupNumbers = truncate(namedGroupNumbers, namedGroupIndex);

        backReferenceGroups = truncate(backReferenceGroups, backReferenceIndex);
        backReferenceIndices = truncate(backReferenceIndices, backReferenceIndex);
        namedBackReferences = truncate(namedBackReferences, backReferenceIndex);
    }

    private void addSequence(CharSequence charSequence) {
        if (sequences.length <= sequenceIndex) {
            sequences = grow(sequences, sequences.length);
        }
        sequences[sequenceIndex] = charSequence;
        sequenceIndex++;
    }

    private void addSequenceReversed(String s) {
        // add the code points in reverse order
        if (s.length() == 1) {
            addSequence(s);
        } else {
            int length = s.length();
            int[] buffer = new int[length];
            int ci = 0;
            int i = 0;
            for (; i < length; i++) {
                int cp = s.codePointAt(ci);
                buffer[length - i - 1] = cp;
                ci += Character.charCount(cp);
            }

            addSequence(new String(buffer, length - i, buffer.length));
        }
    }

    private void addGroup(int startIndex, int endIndex) {
        if (capturingGroupStartIndices.length <= capturedGroupIndex) {
            capturingGroupStartIndices = grow(capturingGroupStartIndices, 10);
            capturingGroupEndIndices = grow(capturingGroupEndIndices, 10);
            capturingGroupNameIndicesP1 = grow(capturingGroupNameIndicesP1, 10);
        }
        capturingGroupStartIndices[capturedGroupIndex] = startIndex;
        capturingGroupEndIndices[capturedGroupIndex] = endIndex;
        //capturingGroupNameIndicesP1[capturedGroupIndex] = 0;
        capturedGroupIndex++;
        capturingGroupCount++;
    }

    private void addNamedGroup(CharSequence groupName, int startIndex, int endIndex) {
        int group = capturedGroupIndex;

        if (namedGroups.length <= namedGroupIndex) {
            namedGroups = grow(namedGroups, 10);
            namedGroupNumbers = grow(namedGroupNumbers, 10);
        }

        namedGroups[namedGroupIndex] = groupName;
        namedGroupNumbers[namedGroupIndex] = group;
        namedGroupIndex++;
        addGroup(startIndex, endIndex);

        capturingGroupNameIndicesP1[group] = capturedGroupIndex;
    }

    private void addBackReference(int group, int index) {
        if (backReferenceGroups.length <= backReferenceIndex) {
            backReferenceGroups = grow(backReferenceGroups, 10);
            backReferenceIndices = grow(backReferenceIndices, 10);
        }
        backReferenceGroups[backReferenceIndex] = group;
        backReferenceIndices[backReferenceIndex] = index;
        backReferenceIndex++;
    }

    private void addNamedBackReference(int namedGroup, int index) {
        int group = namedGroupNumbers[namedGroup];
        int backReference = backReferenceIndex;
        addBackReference(group, index);
        namedBackReferences[backReference] = true;
    }

    private int codePointAt(int index) {
        return index < patternLength ? pattern.codePointAt(index) : -1;
    }

    private int codePointStep(int index) {
        return index < patternLength ? Character.charCount(pattern.codePointAt(index)) : 0;
    }

    private int codePointBackStep(int index) {
        if (index > patternLength) return 0;

        if (index >= 2 && Character.charCount(pattern.codePointAt(index - 2)) == 2) {
            return 2;
        } else {
            return 1;
        }
    }

    private int codePointAt(CharSequence charSequence, int index) {
        return Character.codePointAt(charSequence, index);
    }

    private int codePointStep(CharSequence charSequence, int index) {
        return Character.charCount(Character.codePointAt(charSequence, index));
    }

    private int codePointBackStep(CharSequence charSequence, int index) {
        if (index >= 2 && Character.charCount(Character.codePointAt(charSequence, index - 2)) == 2) {
            return 2;
        } else {
            return 1;
        }
    }

    private static final int UNIX_LINES = Pattern.UNIX_LINES;
    private static final int CASE_INSENSITIVE = Pattern.CASE_INSENSITIVE;
    private static final int MULTILINE = Pattern.MULTILINE;
    private static final int DOTALL = Pattern.DOTALL;
    private static final int UNICODE_CASE = Pattern.UNICODE_CASE;
    private static final int COMMENTS = Pattern.COMMENTS;
    private static final int UNICODE_CHARACTER_CLASS = Pattern.UNICODE_CHARACTER_CLASS;
    private static final int CANON_EQ = Pattern.CANON_EQ;
    private static final int LITERAL = Pattern.LITERAL;

    /**
     * parse the regex to be reversed
     */
    private void parse() {
        initParse();

        if (has(LITERAL)) {
            addSequenceReversed(pattern);
        } else {
            expr();

            // Check extra pattern characters
            if (patternLength != cursor) {
                if (peek() == ')') {
                    throw error("Unmatched closing ')'");
                } else {
                    throw error("Unexpected internal error");
                }
            }
        }

        finalizeParse();
    }

    /**
     * Indicates whether a particular flag is set or not.
     */
    private boolean has(int f) {
        return (flags & f) != 0;
    }

    /**
     * Peek the next character, and do not advance the cursor.
     */
    private int peek() {
        int ch = codePointAt(cursor);
        if (has(COMMENTS))
            ch = peekPastWhitespace(ch);
        return ch;
    }

    /**
     * Read the next character, and advance the cursor by one.
     */
    private int read() {
        int ch = codePointAt(cursor);
        cursor += codePointStep(cursor);

        if (has(COMMENTS))
            ch = parsePastWhitespace(ch);
        return ch;
    }

    /**
     * Read the next character, and advance the cursor by one,
     * ignoring the COMMENTS setting
     */
    private int readEscaped() {
        int ch = codePointAt(cursor);
        cursor += codePointStep(cursor);
        return ch;
    }

    /**
     * Advance the cursor by one, and peek the next character.
     */
    private int next() {
        cursor += codePointStep(cursor);
        int ch = codePointAt(cursor);
        if (has(COMMENTS))
            ch = peekPastWhitespace(ch);
        return ch;
    }

    /**
     * Advance the cursor by one, and peek the next character,
     * ignoring the COMMENTS setting
     */
    private int nextEscaped() {
        cursor += codePointStep(cursor);
        int ch = codePointAt(cursor);
        return ch;
    }

    /**
     * If in xmode peek past whitespace and comments.
     */
    private int peekPastWhitespace(int ch) {
        while (ch == ' ' || ch == '#') {
            while (ch == ' ') {
                cursor += codePointStep(cursor);
                ch = codePointAt(cursor);
            }

            if (ch == '#') {
                ch = peekPastLine();
            }
        }
        return ch;
    }

    /**
     * If in xmode parse past whitespace and comments.
     */
    private int parsePastWhitespace(int ch) {
        while (ch == ' ' || ch == '#') {
            while (ch == ' ') {
                ch = codePointAt(cursor);
                cursor += codePointStep(cursor);
            }

            if (ch == '#')
                ch = parsePastLine();
        }
        return ch;
    }

    /**
     * xmode parse past comment to end of line.
     */
    private int parsePastLine() {
        int ch = codePointAt(cursor);
        cursor += codePointStep(cursor);
        while (ch != 0 && !isLineSeparator(ch)) {
            ch = codePointAt(cursor);
            cursor += codePointStep(cursor);
        }
        return ch;
    }

    /**
     * xmode peek past comment to end of line.
     */
    private int peekPastLine() {
        int ch = codePointAt(cursor);
        cursor += codePointStep(cursor);
        while (ch != 0 && !isLineSeparator(ch)) {
            cursor += codePointStep(cursor);
            ch = codePointAt(cursor);
        }
        return ch;
    }

    /**
     * Determines if character is a line separator in the current mode
     */
    private boolean isLineSeparator(int ch) {
        if (has(UNIX_LINES)) {
            return ch == '\n';
        } else {
            return (ch == '\n' ||
                    ch == '\r' ||
                    (ch | 1) == '\u2029' ||
                    ch == '\u0085');
        }
    }

    /**
     * Read the character after the next one, and advance the cursor by two.
     */
    private int skip() {
        cursor += codePointStep(cursor);
        int ch = codePointAt(cursor);
        cursor += codePointStep(cursor);
        return ch;
    }

    /**
     * Unread one next character, and retreat cursor by one.
     */
    private void unread() {
        cursor -= codePointBackStep(cursor);
    }

    private PatternSyntaxException error(String s) {
        return new PatternSyntaxException(s, pattern, cursor - codePointBackStep(cursor));
    }

    /**
     * This may be called recursively to parse sub expressions that may
     * contain alternations.
     */
    private void expr() {
        if (cursor >= patternLength) return;

        for (; ; ) {
            sequence();
            if (peek() != '|') {
                return;
            }
            addSequence("|");
            next();

            if (cursor >= patternLength) break;
        }
    }

    /**
     * Parsing of sequences between alternations.
     */
    @SuppressWarnings("fallthrough")
    private void sequence() {
LOOP:
        for (; ; ) {
            int ch = peek();
            int closureIndex = sequenceIndex;
            addSequence("");

            switch (ch) {
                case '(':
                    group();
                    break;

                case '[':
                    int end = clazz(cursor, true);
                    if (end < cursor) {
                        // output the rest of what was consumed
                        addSequence(pattern.substring(end, cursor));
                    }
                    break;
                case '\\':
                    int start = cursor;
                    ch = nextEscaped();
                    if (ch == 'p' || ch == 'P') {
                        boolean oneLetter = true;
                        boolean comp = (ch == 'P');
                        ch = next(); // Consume { if present
                        if (ch != '{') {
                            unread();
                        } else {
                            oneLetter = false;
                        }
                        family(oneLetter);
                        addSequence(pattern.substring(start, cursor));
                    } else {
                        unread();
                        atom();
                    }
                    break;
                case '^':
                    next();
                    addSequence("$");
                    break;
                case '$':
                    next();
                    addSequence("^");
                    break;
                case '.':
                    next();
                    addSequence(".");
                    break;
                case '|':
                case ')':
                    break LOOP;
                case ']': // Now interpreting dangling ] and } as literals
                case '}':
                    atom();
                    break;
                case '?':
                case '*':
                case '+':
                    next();
                    throw error("Dangling meta character '" + ((char) ch) + "'");

                case -1:
                    if (cursor >= patternLength) {
                        break LOOP;
                    }
                    // Fall through
                default:
                    atom();
                    break;
            }

            closure(closureIndex);
        }
    }

    /**
     * Parses and returns the name of a "named capturing group", the trailing
     * ">" is consumed after parsing.
     */
    private String groupName(int start) {
        int ch;
        while (Character.isLowerCase(ch = read()) || Character.isUpperCase(ch) ||
                Character.isDigit(ch)) {
        }
        if (cursor == start + 1)
            throw error("named capturing group has 0 length name");
        if (ch != '>')
            throw error("named capturing group is missing trailing '>'");
        return pattern.substring(start, cursor - 1);
    }

    /**
     * Parses a group
     */
    private void group() {
        int save = flags;
        int closureIndex = sequenceIndex;

        // add an empty place holder for closure, which has to be before the group when reversed
        addSequence("");

        int ch = next();
        if (ch == '?') {
            ch = skip();
            switch (ch) {
                case ':':   //  (?:xxx) pure group
                    addSequence(")");
                    expr();
                    addSequence("(?:");
                    break;
                case '=':   // (?=xxx) and (?!xxx) lookahead
                    addSequence(")");
                    expr();
                    addSequence("(?<=");
                    break;
                case '!':
                    addSequence(")");
                    expr();
                    addSequence("(?<!");
                    break;
                case '>':   // (?>xxx)  independent group
                    addSequence(")");
                    expr();
                    addSequence("(?>");
                    break;
                case '<':   // (?<xxx)  look behind
                    int start = cursor;
                    ch = read();
                    if (Character.isLowerCase(ch) || Character.isUpperCase(ch)) {
                        // named captured group
                        String name = groupName(start);
                        if (getGroupNameIndex(name) != -1)
                            throw error("Named capturing group <" + name
                                    + "> is already defined");

                        int groupIndex = capturedGroupIndex;
                        addSequence(")");
                        // create a 0 length one since we don't know where it will end
                        addNamedGroup(name, closureIndex, sequenceIndex);
                        expr();
                        addSequence("(?<" + name + ">");
                        // update to actual sequence end
                        capturingGroupEndIndices[groupIndex] = sequenceIndex;
                        break;
                    } else {
                        addSequence(")");
                        expr();
                        if (ch == '=') {
                            addSequence("(?=");
                        } else if (ch == '!') {
                            addSequence("(?!");
                        } else {
                            throw error("Unknown look-behind group");
                        }
                    }
                    break;
                case '$':
                case '@':
                    throw error("Unknown group type");
                default:    // (?xxx:) inlined match flags
                    unread();
                    addSequence(")");

                    String flags = addFlag();
                    ch = read();
                    if (ch == ')') {
                        addSequence(flags);
                        addSequence("(?");
                        return;    // Inline modifier only
                    }
                    if (ch != ':') {
                        throw error("Unknown inline modifier");
                    }

                    expr();
                    addSequence(":");
                    addSequence(flags);
                    addSequence("(?");
                    break;
            }
        } else { // (xxx) a regular group
            int groupIndex = capturedGroupIndex;
            addSequence(")");
            addGroup(closureIndex, sequenceIndex);

            expr();
            addSequence("(");

            // update to actual sequence end
            capturingGroupEndIndices[groupIndex] = sequenceIndex;
        }

        ch = codePointAt(cursor);
        cursor += codePointStep(cursor);

        if (has(COMMENTS))
            ch = parsePastWhitespace(ch);

        if ((int) ')' != ch) {
            throw error("Unclosed group");
        }
        flags = save;

        // Check for quantifiers and add it ahead of the expression
        closure(closureIndex);
    }

    /**
     * Parses inlined match flags and set them appropriately.
     */
    @SuppressWarnings("fallthrough")
    private String addFlag() {
        int ch = peek();
        int start = cursor;
        int end = cursor;

        for (; ; ) {
            switch (ch) {
                case 'i':
                    flags |= CASE_INSENSITIVE;
                    break;
                case 'm':
                    flags |= MULTILINE;
                    break;
                case 's':
                    flags |= DOTALL;
                    break;
                case 'd':
                    flags |= UNIX_LINES;
                    break;
                case 'u':
                    flags |= UNICODE_CASE;
                    break;
                case 'c':
                    flags |= CANON_EQ;
                    break;
                case 'x':
                    flags |= COMMENTS;
                    break;
                case 'U':
                    flags |= (UNICODE_CHARACTER_CLASS | UNICODE_CASE);
                    break;
                case '-': // subFlag then fall through
                    next();
                    end = subFlag();

                default:
                    return pattern.substring(start, end);
            }
            ch = next();
            end = cursor;
        }
    }

    /**
     * Parses the second part of inlined match flags and turns off
     * flags appropriately.
     */
    @SuppressWarnings("fallthrough")
    private int subFlag() {
        int ch = peek();
        int end = cursor;

        for (; ; ) {
            switch (ch) {
                case 'i':
                    flags &= ~CASE_INSENSITIVE;
                    break;
                case 'm':
                    flags &= ~MULTILINE;
                    break;
                case 's':
                    flags &= ~DOTALL;
                    break;
                case 'd':
                    flags &= ~UNIX_LINES;
                    break;
                case 'u':
                    flags &= ~UNICODE_CASE;
                    break;
                case 'c':
                    flags &= ~CANON_EQ;
                    break;
                case 'x':
                    flags &= ~COMMENTS;
                    break;
                case 'U':
                    flags &= ~(UNICODE_CHARACTER_CLASS | UNICODE_CASE);
                    next();
                    end = cursor;

                default:
                    return end;
            }

            ch = next();
            end = cursor;
        }
    }

    static final int MAX_REPS = 0x7FFFFFFF;

    /**
     * Processes repetition. If the next character peeked is a quantifier
     * then new nodes must be appended to handle the repetition.
     * Prev could be a single or a group, so it could be a chain of nodes.
     */
    private void closure(int closureIndex) {
        int ch = peek();
        switch (ch) {
            case '?':
                ch = next();
                if (ch == '?') {
                    next();
                    sequences[closureIndex] = "??";
                } else if (ch == '+') {
                    next();
                    sequences[closureIndex] = "?+";
                } else {
                    sequences[closureIndex] = "?";
                }
                break;
            case '*':
                ch = next();
                if (ch == '?') {
                    next();
                    sequences[closureIndex] = "*?";
                } else if (ch == '+') {
                    next();
                    sequences[closureIndex] = "*+";
                } else {
                    sequences[closureIndex] = "*";
                }
                break;
            case '+':
                ch = next();
                if (ch == '?') {
                    next();
                    sequences[closureIndex] = "+?";
                } else if (ch == '+') {
                    next();
                    sequences[closureIndex] = "++";
                } else {
                    sequences[closureIndex] = "+";
                }
                break;
            case '{':
                int start = cursor;

                ch = codePointAt(cursor + codePointStep(cursor));
                if (Character.isDigit(ch)) {
                    skip();
                    int cmin = 0;
                    do {
                        cmin = cmin * 10 + (ch - '0');
                    } while (Character.isDigit(ch = read()));
                    int cmax = cmin;
                    if (ch == ',') {
                        ch = read();
                        cmax = MAX_REPS;
                        if (ch != '}') {
                            cmax = 0;
                            while (Character.isDigit(ch)) {
                                cmax = cmax * 10 + (ch - '0');
                                ch = read();
                            }
                        }
                    }
                    if (ch != '}')
                        throw error("Unclosed counted closure");
                    if ((cmin | cmax | (cmax - cmin)) < 0)
                        throw error("Illegal repetition range");
                    ch = peek();

                    if (ch == '?') {
                        next();
                    } else if (ch == '+') {
                        next();
                    }

                    sequences[closureIndex] = pattern.substring(start, cursor);
                    break;
                } else {
                    throw error("Illegal repetition");
                }
            default:
                break;
        }
    }

    /**
     * Utility method for parsing control escape sequences.
     */
    private int c() {
        if (cursor < patternLength) {
            return read() ^ 64;
        }
        throw error("Illegal control escape sequence");
    }

    /**
     * Utility method for parsing octal escape sequences.
     */
    private int o() {
        int n = read();
        if (((n - '0') | ('7' - n)) >= 0) {
            if (cursor < patternLength) {
                int m = read();
                if (((m - '0') | ('7' - m)) >= 0) {
                    if (cursor < patternLength) {
                        int o = read();
                        if ((((o - '0') | ('7' - o)) >= 0) && (((n - '0') | ('3' - n)) >= 0)) {
                            return (n - '0') * 64 + (m - '0') * 8 + (o - '0');
                        }
                        unread();
                    }
                    return (n - '0') * 8 + (m - '0');
                }
                unread();
            }
            return (n - '0');
        }
        throw error("Illegal octal escape sequence");
    }

    private static boolean isHexDigit(int n) {
        return n >= '0' && n <= '9' || n >= 'a' && n <= 'f' || n >= 'A' && n <= 'F';
    }

    private static int toDigit(int n) {
        return n >= 'a' ? n - 'a' + 10 : n >= 'A' ? n - 'A' + 10 : n - '0';
    }

    /**
     * Utility method for parsing hexadecimal escape sequences.
     */
    private int x() {
        int n = read();
        if (isHexDigit(n)) {
            int m = read();
            if (isHexDigit(m)) {
                return toDigit(n) * 16 + toDigit(m);
            }
        } else if (n == '{' && isHexDigit(peek())) {
            int ch = 0;
            while (isHexDigit(n = read())) {
                ch = (ch << 4) + toDigit(n);
                if (ch > Character.MAX_CODE_POINT)
                    throw error("Hexadecimal codepoint is too big");
            }
            if (n != '}')
                throw error("Unclosed hexadecimal escape sequence");
            return ch;
        }
        throw error("Illegal hexadecimal escape sequence");
    }

    /**
     * Parse a character class, and return the position in pattern starting from which sequences
     * were not generated.
     * <p>
     * Consumes a ] on the way out if consume is true. Usually consume
     * is true except for the case of [abc&&def] where def is a separate
     * right hand node with "understood" brackets.
     *
     * @param startCursor - cursor from which output has not been generated
     * @return startCursor from which output has not been generated
     */
    private int clazz(int startCursor, boolean consume) {
        boolean include = true;
        boolean firstInClass = true;
        int start = startCursor;
        boolean hadClass = false;

        int ch = next();
        for (; ; ) {
            switch (ch) {
                case '^':
                    // Negates if first char in a class, otherwise literal
                    if (firstInClass) {
                        if (codePointAt(cursor - codePointBackStep(cursor)) != '[')
                            break;
                        ch = next();
                        include = !include;
                        continue;
                    } else {
                        // ^ not first in class, treat as literal
                        break;
                    }
                case '[':
                    firstInClass = false;
                    start = clazz(start, true);
                    hadClass = true;
                    ch = peek();
                    continue;
                case '&':
                    firstInClass = false;
                    ch = next();
                    if (ch == '&') {
                        ch = next();
                        while (ch != ']' && ch != '&') {
                            if (ch == '[') {
                                start = clazz(start, true);
                            } else { // abc&&def
                                unread();
                                start = clazz(start, false);
                            }
                            ch = peek();
                        }
                    } else {
                        // treat as a literal &
                        unread();
                        break;
                    }
                    continue;
                case -1:
                    firstInClass = false;
                    if (cursor >= patternLength)
                        throw error("Unclosed character class");
                    break;
                case ']':
                    firstInClass = false;
                    if (hadClass) {
                        if (consume)
                            next();
                        return start;
                    }
                    break;
                default:
                    firstInClass = false;
                    break;
            }
            start = range(start);
            hadClass = true;
            ch = peek();
        }
    }

    /**
     * Parse a single character or a character range in a character class
     */
    private int range(int startCursor) {
        int[] start = { startCursor };

        int ch = peek();
        if (ch == '\\') {
            ch = nextEscaped();
            if (ch == 'p' || ch == 'P') { // A property
                boolean comp = (ch == 'P');
                boolean oneLetter = true;
                // Consume { if present
                ch = next();
                if (ch != '{')
                    unread();
                else {
                    oneLetter = false;
                }

                family(oneLetter);
                return start[0];
            } else { // ordinary escape
                unread();
                ch = escape(start, true, true);
                if (ch == -1)
                    return start[0];
            }
        } else {
            next();
        }
        if (ch >= 0) {
            if (peek() == '-') {
                int endRange = codePointAt(cursor + codePointStep(cursor));

                if (endRange == '[') {
                    return start[0];
                }

                if (endRange != ']') {
                    next();
                    int m = peek();
                    if (m == '\\') {
                        m = escape(start, true, false);
                    } else {
                        next();
                    }
                    if (m < ch) {
                        throw error("Illegal character range");
                    }
                    return start[0];
                }
            }
            return start[0];
        }
        throw error("Unexpected character '" + ((char) ch) + "'");
    }

    /**
     * Parses a Unicode character family and returns its name
     */
    @SuppressWarnings("UnusedReturnValue")
    private String family(boolean singleLetter) {
        next();
        String name;

        if (singleLetter) {
            int c = codePointAt(cursor);
            if (!Character.isSupplementaryCodePoint(c)) {
                name = String.valueOf((char) c);
            } else {
                name = new String(new int[] { codePointAt(cursor) }, 0, 1);
            }
            read();
        } else {
            int i = cursor;

            //noinspection StatementWithEmptyBody
            while (cursor < patternLength && read() != '}') {
            }

            int j = cursor;
            if (j > patternLength)
                throw error("Unclosed character family");
            if (i + 1 >= j)
                throw error("Empty character family");
            name = pattern.substring(i, j - 1);
        }
        return name;
    }

    /**
     * Parse and add a sequence in reverse
     */
    @SuppressWarnings("fallthrough")
    private void atom() {
        int first = 0;
        int prev = -1;
        int[] start = { cursor };
        int ch = peek();

        for (; ; ) {
            switch (ch) {
                case '*':
                case '+':
                case '?':
                case '{':
                    if (first > 1) {
                        cursor = prev;    // Unwind one character
                        //noinspection UnusedAssignment
                        first--;
                    }
                    break;
                case '$':
                case '.':
                case '^':
                case '(':
                case '[':
                case '|':
                case ')':
                    break;
                case '\\':
                    if (first > 0) {
                        break;
                    }

                    ch = nextEscaped();
                    if (ch == 'p' || ch == 'P') { // Property
                        boolean oneLetter = true;

                        ch = next(); // Consume { if present
                        if (ch != '{')
                            unread();
                        else {
                            oneLetter = false;
                        }

                        family(oneLetter);
                        addSequence(pattern.substring(start[0], cursor));
                        return;
                    }

                    unread();
                    prev = cursor;

                    ch = escape(start, false, true);
                    if (ch >= 0) {
                        // output the escape sequence
                        addSequence(pattern.substring(start[0], cursor));
                        start[0] = cursor;
                        first = 0;

                        ch = peek();
                        continue;
                    } else {
                        assert start[0] == cursor;
                        return;
                    }

                case -1:
                    if (cursor >= patternLength) {
                        break;
                    }
                    throw error("Internal error");

                default:
                    prev = cursor;
                    first++;
                    ch = next();
                    continue;
            }
            break;
        }

        if (start[0] < cursor) {
            addSequenceReversed(pattern.substring(start[0], cursor));
        }
    }

    /**
     * Parses a backref greedily, taking as many numbers as it
     * can. The first digit is always treated as a backref, but
     * multi digit numbers are only treated as a backref if at
     * least that many backrefs exist at this point in the regex.
     */
    private int ref(int refNum) {
        boolean done = false;

        while (!done) {
            int ch = peek();
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    int newRefNum = (refNum * 10) + (ch - '0');
                    // Add another number if it doesn't make a group
                    // that doesn't exist
                    if (capturingGroupCount - 1 < newRefNum) {
                        done = true;
                        break;
                    }
                    refNum = newRefNum;
                    read();
                    break;
                default:
                    done = true;
                    break;
            }
        }

        return refNum;
    }

    /**
     * Parses an escape sequence
     */
    private int escape(int[] start, boolean inclass, boolean create) {
        int startM2 = cursor;
        int ch = skip();

        switch (ch) {
            case '0':
                return o();

            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                if (inclass) break;
                if (create) {
                    if (start[0] < startM2) addSequence(pattern.substring(start[0], startM2));

                    int groupNum = ref(ch - '0');
                    start[0] = cursor;

                    int sequenceStart = sequenceIndex;
                    addSequence(pattern.substring(startM2, cursor));
                    addBackReference(groupNum - 1, sequenceStart);
                }
                return -1;

            case 'A':
                if (inclass) break;
                if (create) {
                    if (start[0] < startM2) addSequence(pattern.substring(start[0], startM2));
                    start[0] = cursor;
                    addSequence("\\z");
                }
                return -1;

            case 'Z':
            case 'z':
                if (inclass) break;
                if (create) {
                    if (start[0] < startM2) addSequence(pattern.substring(start[0], startM2));
                    start[0] = cursor;
                    addSequence("\\A");
                }
                return -1;

            case 'B':
            case 'G':
            case 'b':
                if (inclass) break;
                if (create) {
                    addSequence(pattern.substring(start[0], cursor));
                    start[0] = cursor;
                }
                return -1;

            case 'D':
            case 'S':
            case 'W':
            case 'd':
            case 's':
            case 'v':
            case 'w':
                if (create) {
                    addSequence(pattern.substring(start[0], cursor));
                    start[0] = cursor;
                }
                return -1;

            case 'Q':
                // take all to next \E as literals, but reversed
                if (inclass) break;
                if (create) {
                    if (start[0] < startM2) addSequence(pattern.substring(start[0], startM2));

                    // look for \E
                    int end = cursor;
                    startM2 = end;

                    while (cursor < patternLength) {
                        end = cursor;
                        ch = read();
                        if (ch != '\\') continue;

                        if (cursor == patternLength)
                            throw error("Unterminated \\Q");

                        ch = read();
                        if (ch == 'E') break;
                    }

                    if (cursor == patternLength && end != cursor - 2)
                        throw error("Unterminated \\Q");

                    addSequence("\\E");
                    addSequenceReversed(pattern.substring(startM2, end));
                    addSequence("\\Q");
                    start[0] = cursor;
                }
                return -1;

            case 'k':
                if (inclass) break;

                if (read() != '<')
                    throw error("\\k is not followed by '<' for named capturing group");

                String name = groupName(cursor);
                int namedGroup = getGroupNameIndex(name);

                if (namedGroup == -1)
                    throw error("(named capturing group <" + name + "> does not exit");

                if (create) {
                    if (start[0] < startM2) addSequence(pattern.substring(start[0], startM2));
                    start[0] = cursor;

                    int startSequence = sequenceIndex;
                    addSequence(pattern.substring(startM2, cursor));
                    addNamedBackReference(namedGroup, startSequence);
                }
                return -1;

            case 'C':
            case 'E':
            case 'F':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'X':
            case 'Y':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'l':
            case 'm':
            case 'o':
            case 'p':
            case 'q':
            case 'y':
                break;

            case 'a':
                return '\007';
            case 'c':
                return c();
            case 'e':
                return '\033';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case 'u':
                return u();
            case 'x':
                return x();
            default:
                return ch;
        }
        throw error("Illegal/unsupported escape sequence");
    }

    private int uxxxx() {
        int n = 0;
        for (int i = 0; i < 4; i++) {
            int ch = read();
            if (!isHexDigit(ch)) {
                throw error("Illegal Unicode escape sequence");
            }
            n = n * 16 + toDigit(ch);
        }
        return n;
    }

    private int u() {
        int n = uxxxx();
        if (Character.isHighSurrogate((char) n)) {
            int cur = cursor;
            if (read() == '\\' && read() == 'u') {
                int n2 = uxxxx();
                if (Character.isLowSurrogate((char) n2))
                    return Character.toCodePoint((char) n, (char) n2);
            }
            cursor = cur;
        }
        return n;
    }
}
