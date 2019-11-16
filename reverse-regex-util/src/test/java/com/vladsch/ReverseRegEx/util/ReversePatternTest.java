package com.vladsch.ReverseRegEx.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReversePatternTest {
    private static String reversed(String p) {
        ReversePattern regEx = ReversePattern.compile(p);
        return regEx.pattern();
    }

    private static String reversed(String p, int f) {
        ReversePattern regEx = ReversePattern.compile(p, f);
        return regEx.pattern();
    }

    @Test
    public void test_basic() throws Exception {
        // @formatter:off
        assertEquals("",                        reversed(""));
        assertEquals("a",                       reversed("a"));
        assertEquals("ba",                      reversed("ab"));
        assertEquals("cba",                     reversed("abc"));
        assertEquals("$",                       reversed("^"));
        assertEquals("^",                       reversed("$"));
        assertEquals("\\z",                     reversed("\\A"));
        assertEquals("\\A",                     reversed("\\Z"));
        assertEquals("\\A",                     reversed("\\z"));

        assertEquals("|",                       reversed("|"));
        assertEquals("|a",                      reversed("a|"));
        assertEquals("b|",                      reversed("|b"));
        assertEquals("b|a",                     reversed("a|b"));
        assertEquals("dc|ba",                   reversed("ab|cd"));
        assertEquals("fed|cba",                 reversed("abc|def"));

        assertEquals("()",                      reversed("()"));
        assertEquals("(a)",                     reversed("(a)"));
        assertEquals("(ba)",                    reversed("(ab)"));
        assertEquals("(cba)",                   reversed("(abc)"));

        assertEquals("(?:)",                    reversed("(?:)"));
        assertEquals("(?:a)",                   reversed("(?:a)"));
        assertEquals("(?:ba)",                  reversed("(?:ab)"));
        assertEquals("(?:cba)",                 reversed("(?:abc)"));

        assertEquals("(?>)",                    reversed("(?>)"));
        assertEquals("(?>a)",                   reversed("(?>a)"));
        assertEquals("(?>ba)",                  reversed("(?>ab)"));
        assertEquals("(?>cba)",                 reversed("(?>abc)"));

        assertEquals("(?<=)",                   reversed("(?=)"));
        assertEquals("(?<=a)",                  reversed("(?=a)"));
        assertEquals("(?<=ba)",                 reversed("(?=ab)"));
        assertEquals("(?<=cba)",                reversed("(?=abc)"));

        assertEquals("(?<!)",                   reversed("(?!)"));
        assertEquals("(?<!a)",                  reversed("(?!a)"));
        assertEquals("(?<!ba)",                 reversed("(?!ab)"));
        assertEquals("(?<!cba)",                reversed("(?!abc)"));

        assertEquals("(?=)",                    reversed("(?<=)"));
        assertEquals("(?=a)",                   reversed("(?<=a)"));
        assertEquals("(?=ba)",                  reversed("(?<=ab)"));
        assertEquals("(?=cba)",                 reversed("(?<=abc)"));

        assertEquals("(?!)",                    reversed("(?<!)"));
        assertEquals("(?!a)",                   reversed("(?<!a)"));
        assertEquals("(?!ba)",                  reversed("(?<!ab)"));
        assertEquals("(?!cba)",                 reversed("(?<!abc)"));

        assertEquals("(?<a>)",                  reversed("(?<a>)"));
        assertEquals("(?<a>A)",                 reversed("(?<a>A)"));
        assertEquals("(?<ab>BA)",               reversed("(?<ab>AB)"));
        assertEquals("(?<abc>CBA)",             reversed("(?<abc>ABC)"));

        // @formatter:on
    }

    @Test
    public void test_backRef() throws Exception {
        // @formatter:off
        assertEquals("(a)cb\\1",                    reversed("(a)bc\\1"));
        assertEquals("(?<a>A)cb\\1",                reversed("(?<a>A)bc\\1"));
        assertEquals("(?<a>A)cb\\k<a>",             reversed("(?<a>A)bc\\k<a>"));
        // @formatter:on
    }

    @Test
    public void test_backRefComplex() throws Exception {
        // @formatter:off
        assertEquals("(c)(b)(a)\\1\\2\\3",          reversed("(a)(b)(c)\\1\\2\\3"));
        assertEquals("(b)(c)(a)\\2\\1\\3",          reversed("(a)(b)(c)\\1\\3\\2"));
        assertEquals("(c)(a)(b)\\1\\3\\2",          reversed("(a)(b)(c)\\2\\1\\3"));
        assertEquals("(a)(c)(b)\\2\\3\\1",          reversed("(a)(b)(c)\\2\\3\\1"));
        assertEquals("(b)(a)(c)\\3\\1\\2",          reversed("(a)(b)(c)\\3\\1\\2"));
        assertEquals("(a)(b)(c)\\3\\2\\1",          reversed("(a)(b)(c)\\3\\2\\1"));
        // @formatter:on
    }

    @Test
    public void test_namedBackRefComplex() throws Exception {
        // @formatter:off
        assertEquals("(?<c>c)(b)(a)\\k<c>\\2\\3",           reversed("(a)(b)(?<c>c)\\1\\2\\k<c>"));
        assertEquals("(b)(c)(a)\\2\\1\\3",                  reversed("(a)(b)(c)\\1\\3\\2"));
        assertEquals("(c)(a)(b)\\1\\3\\2",                  reversed("(a)(b)(c)\\2\\1\\3"));
        assertEquals("(a)(c)(b)\\2\\3\\1",                  reversed("(a)(b)(c)\\2\\3\\1"));
        assertEquals("(b)(a)(c)\\3\\1\\2",                  reversed("(a)(b)(c)\\3\\1\\2"));
        assertEquals("(?<A>a)(b)(c)\\3\\2\\1",              reversed("(?<A>a)(b)(c)\\3\\2\\1"));
        assertEquals("(?<A>a)(b)(c)\\3\\2\\k<A>",           reversed("(?<A>a)(b)(c)\\3\\2\\k<A>"));
        // @formatter:on
    }

    @Test
    public void test_backRefNested() throws Exception {
        // @formatter:off
        assertEquals("((b)a)\\2(c)\\3\\1",                  reversed("(a(b))(c)\\3\\2\\1"));
        assertEquals("((b)\\2a)\\2(c)\\3\\1",               reversed("(a(b)\\2)(c)\\3\\2\\1"));
        assertEquals("((b)(?:\\2)1)\\2(c)\\3\\1",           reversed("(1(b)\\2)(c)\\3\\2\\1"));
        assertEquals("((?<b>b)\\k<b>1)\\2(c)\\3\\1",        reversed("(1(?<b>b)\\k<b>)(c)\\3\\2\\1"));
        assertEquals("((?<b>b)(?:\\2)1)\\k<b>(c)\\3\\1",    reversed("(1(?<b>b)\\2)(c)\\3\\k<b>\\1"));
        assertEquals("((?<b>b)\\k<b>1)\\k<b>(c)\\3\\1",     reversed("(1(?<b>b)\\k<b>)(c)\\3\\k<b>\\1"));
        assertEquals("((cba)(?:\\2)1)\\2(fed)\\3\\1",       reversed("(1(abc)\\2)(def)\\3\\2\\1"));
        assertEquals("^((cba)(?:\\2)1)\\2(fed)\\3\\1$",     reversed("^(1(abc)\\2)(def)\\3\\2\\1$"));
        assertEquals("(?<=zyx)((cba)(?:\\2)1)\\2(fed)\\3\\1$",     reversed("^(1(abc)\\2)(def)\\3\\2\\1(?=xyz)"));
        // @formatter:on
    }

    @Test
    public void test_quotes() throws Exception {
        // @formatter:off
        assertEquals("((b)a)\\2(c)\\Q1\\2\\3\\)c())b(a(\\E\\3\\1",                  reversed("(a(b))(c)\\Q(a(b))(c)\\3\\2\\1\\E\\3\\2\\1"));
        // @formatter:on
    }

    @Test
    public void test_classes() throws Exception {
        // @formatter:off
        assertEquals("[abc]",                   reversed("[abc]"));
        assertEquals("[^abc]",                  reversed("[^abc]"));
        assertEquals("[a-zA-Z]",                reversed("[a-zA-Z]"));
        assertEquals("[a-d[m-p]]",              reversed("[a-d[m-p]]"));
        assertEquals("[a-z&&[def]]",            reversed("[a-z&&[def]]"));
        assertEquals("[a-z&&[^bc]]",            reversed("[a-z&&[^bc]]"));
        assertEquals("[a-z&&[^m-p]]",           reversed("[a-z&&[^m-p]]"));

        assertEquals("\\00",                       reversed("\\00"));
        assertEquals("\\01",                       reversed("\\01"));
        assertEquals("\\02",                       reversed("\\02"));
        assertEquals("\\03",                       reversed("\\03"));
        assertEquals("\\04",                       reversed("\\04"));
        assertEquals("\\05",                       reversed("\\05"));
        assertEquals("\\06",                       reversed("\\06"));
        assertEquals("\\07",                       reversed("\\07"));
        assertEquals("\\000",                       reversed("\\000"));
        assertEquals("\\001",                       reversed("\\001"));
        assertEquals("\\002",                       reversed("\\002"));
        assertEquals("\\003",                       reversed("\\003"));
        assertEquals("\\004",                       reversed("\\004"));
        assertEquals("\\005",                       reversed("\\005"));
        assertEquals("\\006",                       reversed("\\006"));
        assertEquals("\\007",                       reversed("\\007"));
        assertEquals("\\010",                       reversed("\\010"));
        assertEquals("\\011",                       reversed("\\011"));
        assertEquals("\\012",                       reversed("\\012"));
        assertEquals("\\013",                       reversed("\\013"));
        assertEquals("\\014",                       reversed("\\014"));
        assertEquals("\\015",                       reversed("\\015"));
        assertEquals("\\016",                       reversed("\\016"));
        assertEquals("\\017",                       reversed("\\017"));
        assertEquals("\\020",                       reversed("\\020"));
        assertEquals("\\021",                       reversed("\\021"));
        assertEquals("\\022",                       reversed("\\022"));
        assertEquals("\\023",                       reversed("\\023"));
        assertEquals("\\024",                       reversed("\\024"));
        assertEquals("\\025",                       reversed("\\025"));
        assertEquals("\\026",                       reversed("\\026"));
        assertEquals("\\027",                       reversed("\\027"));
        assertEquals("\\030",                       reversed("\\030"));
        assertEquals("\\031",                       reversed("\\031"));
        assertEquals("\\032",                       reversed("\\032"));
        assertEquals("\\033",                       reversed("\\033"));
        assertEquals("\\034",                       reversed("\\034"));
        assertEquals("\\035",                       reversed("\\035"));
        assertEquals("\\036",                       reversed("\\036"));
        assertEquals("\\037",                       reversed("\\037"));
        assertEquals("\\077",                       reversed("\\077"));
        assertEquals("\\0000",                      reversed("\\0000"));
        assertEquals("\\0100",                      reversed("\\0100"));
        assertEquals("\\0177",                      reversed("\\0177"));
        assertEquals("\\0377",                      reversed("\\0377"));
        assertEquals("\\x00",                       reversed("\\x00"));
        assertEquals("\\xff",                       reversed("\\xff"));
        assertEquals("\\xFF",                       reversed("\\xFF"));
        assertEquals("\\u0000",                     reversed("\\u0000"));
        assertEquals("\\x{0000}",                   reversed("\\x{0000}"));
        assertEquals("\\uffff",                     reversed("\\uffff"));
        assertEquals("\\x{ffff}",                   reversed("\\x{ffff}"));
        assertEquals("\\uFFFF",                     reversed("\\uFFFF"));
        assertEquals("\\x{FFFF}",                   reversed("\\x{FFFF}"));
        assertEquals("\\t",                         reversed("\\t"));
        assertEquals("\\n",                         reversed("\\n"));
        assertEquals("\\r",                         reversed("\\r"));
        assertEquals("\\f",                         reversed("\\f"));
        assertEquals("\\a",                         reversed("\\a"));
        assertEquals("\\e",                         reversed("\\e"));
        assertEquals("\\ca",                        reversed("\\ca"));
        assertEquals("\\cb",                        reversed("\\cb"));
        assertEquals("\\cc",                        reversed("\\cc"));
        assertEquals("\\cz",                        reversed("\\cz"));

        assertEquals("\\d",                         reversed("\\d"));
        assertEquals("\\D",                         reversed("\\D"));
        assertEquals("\\s",                         reversed("\\s"));
        assertEquals("\\S",                         reversed("\\S"));
        assertEquals("\\v",                         reversed("\\v"));
        assertEquals("\\w",                         reversed("\\w"));
        assertEquals("\\W",                         reversed("\\W"));
        assertEquals("\\p{Lower}",                  reversed("\\p{Lower}"));
        assertEquals("\\p{Upper}",                  reversed("\\p{Upper}"));
        assertEquals("\\p{ASCII}",                  reversed("\\p{ASCII}"));
        assertEquals("\\p{Alpha}",                  reversed("\\p{Alpha}"));
        assertEquals("\\p{Digit}",                  reversed("\\p{Digit}"));
        assertEquals("\\p{Alnum}",                  reversed("\\p{Alnum}"));
        assertEquals("\\p{Punct}",                  reversed("\\p{Punct}"));
        assertEquals("\\p{Graph}",                  reversed("\\p{Graph}"));
        assertEquals("\\p{Print}",                  reversed("\\p{Print}"));
        assertEquals("\\p{Blank}",                  reversed("\\p{Blank}"));
        assertEquals("\\p{Cntrl}",                  reversed("\\p{Cntrl}"));
        assertEquals("\\p{XDigit}",                 reversed("\\p{XDigit}"));
        assertEquals("\\p{Space}",                  reversed("\\p{Space}"));
        assertEquals("\\p{javaLowerCase}",          reversed("\\p{javaLowerCase}"));
        assertEquals("\\p{javaUpperCase}",          reversed("\\p{javaUpperCase}"));
        assertEquals("\\p{javaWhitespace}",         reversed("\\p{javaWhitespace}"));
        assertEquals("\\p{javaMirrored}",           reversed("\\p{javaMirrored}"));
        assertEquals("\\p{IsLatin}",                reversed("\\p{IsLatin}"));
        assertEquals("\\p{InGreek}",                reversed("\\p{InGreek}"));
        assertEquals("\\p{Lu}",                     reversed("\\p{Lu}"));
        assertEquals("\\p{IsAlphabetic}",           reversed("\\p{IsAlphabetic}"));
        assertEquals("\\p{Sc}",                     reversed("\\p{Sc}"));
        assertEquals("\\P{InGreek}",                reversed("\\P{InGreek}"));
        assertEquals("[\\p{L}&&[^\\p{Lu}]]",        reversed("[\\p{L}&&[^\\p{Lu}]]"));
        assertEquals("$",                           reversed("^"));
        assertEquals("^",                           reversed("$"));
        assertEquals("\\b",                         reversed("\\b"));
        assertEquals("\\B",                         reversed("\\B"));
        assertEquals("\\z",                         reversed("\\A"));
        assertEquals("\\G",                         reversed("\\G"));
        assertEquals("\\A",                         reversed("\\Z"));
        assertEquals("\\A",                         reversed("\\z"));
        // @formatter:on
    }

    @Test
    public void test_constructs() throws Exception {
        // @formatter:off
        assertEquals("X?",                          reversed("X?"));
        assertEquals("X*",                          reversed("X*"));
        assertEquals("X+",                          reversed("X+"));
        assertEquals("X{2}",                        reversed("X{2}"));
        assertEquals("X{3,}",                       reversed("X{3,}"));
        assertEquals("X{1,3}",                      reversed("X{1,3}"));
        assertEquals("X??",                         reversed("X??"));
        assertEquals("X*?",                         reversed("X*?"));
        assertEquals("X+?",                         reversed("X+?"));
        assertEquals("X{2}?",                       reversed("X{2}?"));
        assertEquals("X{3,}?",                      reversed("X{3,}?"));
        assertEquals("X{1,3}?",                     reversed("X{1,3}?"));
        assertEquals("X?+",                         reversed("X?+"));
        assertEquals("X*+",                         reversed("X*+"));
        assertEquals("X++",                         reversed("X++"));
        assertEquals("X{2}+",                       reversed("X{2}+"));
        assertEquals("X{3,}+",                      reversed("X{3,}+"));
        assertEquals("X{1,3}+",                     reversed("X{1,3}+"));
        assertEquals("YX",                          reversed("XY"));
        assertEquals("Y|X",                         reversed("X|Y"));
        assertEquals("Y+|X",                         reversed("X|Y+"));
        assertEquals("(X)",                         reversed("(X)"));
        assertEquals("\\n",                         reversed("\\n"));
        assertEquals("(?:X)",                       reversed("(?:X)"));
        assertEquals("(?idmsuxU-idmsuxU)",          reversed("(?idmsuxU-idmsuxU)"));
        assertEquals("(?idmsux-idmsux:X)",          reversed("(?idmsux-idmsux:X)"));
        assertEquals("(?<=X)",                      reversed("(?=X)"));
        assertEquals("(?<!X)",                      reversed("(?!X)"));
        assertEquals("(?=X)",                       reversed("(?<=X)"));
        assertEquals("(?!X)",                       reversed("(?<!X)"));
        assertEquals("(?>X)",                       reversed("(?>X)"));
        // @formatter:on
    }

    @Test
    public void test_classesExtra() throws Exception {
        // @formatter:off
        assertEquals("[\\Q;\\E]",                    reversed("[\\Q;\\E]"));
        assertEquals("[\\Qabc\\E]",                  reversed("[\\Qabc\\E]"));
        assertEquals("[a-z]",                        reversed("[a-z]"));
        assertEquals("[^a-z]",                       reversed("[^a-z]"));
        // @formatter:on
    }

    @Test
    public void test_backwardsBackSlashes() throws Exception {
        // @formatter:off
        assertEquals("(?<!\\Q\\\\E)(\\Q\\\\\\E)(?!\\Q\\\\E)",                    reversed("(?<!\\Q\\\\E)(\\Q\\\\\\E)(?!\\Q\\\\E)"));
        // @formatter:on
    }
}
