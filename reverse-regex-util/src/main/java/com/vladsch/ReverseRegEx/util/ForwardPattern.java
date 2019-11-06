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

import java.util.regex.Pattern;

public final class ForwardPattern implements RegExPattern {
    /**
     * The original regular-expression pattern string.
     */
    private Pattern myPattern;

    private ForwardPattern(final Pattern pattern) {
        myPattern = pattern;
    }

    @Override
    public Pattern compiled() {
        return myPattern;
    }

    public String toString() {
        return myPattern.toString();
    }

    public static ForwardPattern compile(String p) {
        //noinspection UnnecessaryLocalVariable
        ForwardPattern regEx = new ForwardPattern(Pattern.compile(p, 0));
        return regEx;
    }

    public static ForwardPattern compile(String p, int f) {
        //noinspection UnnecessaryLocalVariable
        ForwardPattern regEx = new ForwardPattern(Pattern.compile(p, f));
        return regEx;
    }

    @Override
    public ForwardMatcher matcher(CharSequence input) {
        return new ForwardMatcher(myPattern.matcher(input));
    }

    public static boolean matches(String regex, CharSequence input) {
        RegExPattern p = compile(regex);
        RegExMatcher m = p.matcher(input);
        return m.matches();
    }

    @Override
    public String[] split(CharSequence input, int limit) {
        return myPattern.split(input, limit);
    }

    @Override
    public String[] split(CharSequence input) {
        return split(input, 0);
    }

    public String originalPattern() {
        return myPattern.toString();
    }

    @Override
    public String pattern() {
        return myPattern.toString();
    }

    @Override
    public int flags() {
        return myPattern.flags();
    }
}
