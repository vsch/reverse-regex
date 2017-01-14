package com.vladsch.ReverseRegEx.util;

import java.util.regex.Pattern;

public interface RegExPattern {
    Pattern compiled();
    RegExMatcher matcher(CharSequence input);
    String[] split(CharSequence input, int limit);
    String[] split(CharSequence input);
    String pattern();
    int flags();
}
