package com.vladsch.ReverseRegEx.util;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class ReverseMatcherTest {

    @Test
    public void test_basic() throws Exception {
        ReversePattern regEx = ReversePattern.compile("\\b(id)\\b\\s*=\\s*(.+?)\\s*;$");
        assertEquals("^;\\s*(.+?)\\s*=\\s*\\b(di)\\b", regEx.pattern());

        final String text = "test.id  = def;";

        ReverseMatcher matcher = regEx.matcher(text);
        assertEquals(true, matcher.find());
        assertEquals("id  = def;", matcher.group());
        assertEquals("id", matcher.group(1));
        assertEquals("def", matcher.group(2));
        assertEquals("id", text.substring(matcher.start(1), matcher.end(1)));
        assertEquals("def", text.substring(matcher.start(2), matcher.end(2)));
        assertEquals("id  = def;", text.substring(matcher.start(), matcher.end()));

        assertEquals(true, matcher.find(0));
        assertEquals(5, matcher.start(1));
        assertEquals(true, matcher.find(1));
        assertEquals(5, matcher.start(1));
        assertEquals(true, matcher.find(2));
        assertEquals(5, matcher.start(1));
        assertEquals(true, matcher.find(3));
        assertEquals(5, matcher.start(1));
        assertEquals(true, matcher.find(4));
        assertEquals(5, matcher.start(1));
        assertEquals(true, matcher.find(5));
        assertEquals(5, matcher.start(1));
        assertEquals(false, matcher.find(6));
        assertEquals(false, matcher.find(7));
        assertEquals(false, matcher.find(8));
        assertEquals(false, matcher.find(9));
        assertEquals(false, matcher.find(10));
    }

    @Test
    public void test_split() throws Exception {
        ReversePattern regEx = ReversePattern.compile("\\s*,\\s*");
        final String text = "a , abc,def, xyz";
        Pattern original = Pattern.compile(regEx.originalPattern());
        String[] originalSplit = original.split(text);
        String[] split = regEx.split(text);
        String[] reversedSplit = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            reversedSplit[split.length - i - 1] = split[i];
        }

        assertArrayEquals(originalSplit, reversedSplit);
    }
}
