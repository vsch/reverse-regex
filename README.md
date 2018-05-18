reverse-regex
=============

**reverse-regex** is a Java utility library that allows Java to perform backward search using
standard RegEx Java classes on a reversed character sequence of original text.

[![GitQ](https://gitq.com/badge.svg)](https://gitq.com/vsch/reverse-regex)

### Requirements

* Java 8 or above
* The core has no dependencies

[![Build status](https://travis-ci.org/vsch/reverse-regex.svg?branch=master)](https://travis-ci.org/vsch/reverse-regex)
[![Maven Central status](https://img.shields.io/maven-central/v/com.vladsch.reverse-regex/reverse-regex.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.vladsch.reverse-regex%22)

### Examples:

| Original RegEx                       | Reversed RegEx                       | Original Match                                 | Reversed Match                                 |
|--------------------------------------|--------------------------------------|------------------------------------------------|------------------------------------------------|
| `(a(b))(c)\3\2\1`                    | `((b)a)\2(c)\3\1`                    | `abccbab`                                      | `babccba`                                      |
| `(1(b)\2)(c)\3\2\1`                  | `((b)(?:\2)1)\2(c)\3\1`              | `1bbccb1bb`                                    | `bb1bccbb1`                                    |
| `(1(abc)\2)(def)\3\2\1`              | `((cba)(?:\2)1)\2(fed)\3\1`          | `1abcabcdefdefabc1abcabc`                      | `cbacba1cbafedfedcbacba1`                      |
| `(?<A>a)(b)(c)\3\2\k<A>`             | `(?<A>a)(b)(c)\3\2\k<A>`             |                                                |                                                |
| `^(1(abc)\2)(def)\3\2\1(?=xyz)`      | `(?<=zyx)((cba)(?:\2)1)\2(fed)\3\1$` | `1abcabcdefdefabc1abcabc` if followed by `xyz` | `cbacba1cbafedfedcbacba1` if preceded by `zyx` |
| `(a(b))(c)\Q(a(b))(c)\3\2\1\E\3\2\1` | `((b)a)\2(c)\Q1\2\3\)c())b(a(\E\3\1` |                                                |                                                |

### Using reverse-regex-util

Eliminate the need to convert offsets, groups and reversing input and result strings when using
reverse search with the Java RegEx library functions by using the wrapper classes in this
library which provide transparent conversion where needed and possible.

```java
import com.vladsch.ReverseRegEx.util.ForwardPattern;
import com.vladsch.ReverseRegEx.util.RegExMatcher;
import com.vladsch.ReverseRegEx.util.RegExPattern;
import com.vladsch.ReverseRegEx.util.ReversePattern;

public class ReverseSearch {
    public static void main(String[] args) {
        RegExPattern p;
        RegExMatcher m;

        // find all occurrences of var = var.func()
        String regEx = "([a-zA-z_][a-zA-z_0-9]*)\\s*=\\s*\\1\\.([a-zA-z_][a-zA-z_0-9]*)\\(\\)";
        String input = "  abc = abc.trim()\n" +
                "  def = def.trimEnd()\n" +
                "";

        p = ForwardPattern.compile(regEx);
        System.out.println("Forward Search: " + p.pattern());

        m = p.matcher(input);
        while (m.find()) {
            System.out.println("Found: " + m.group() + " [" + m.start() + ", " + m.end() + ")\n" +
                    "   var: " + m.group(1) + " [" + m.start(1) + ", " + m.end(1) + ")\n" +
                    "   func: " + m.group(2) + " [" + m.start(2) + ", " + m.end(2) + ")");
        }

        p = ReversePattern.compile(regEx);
        System.out.println("\nReverse Search: " + p.pattern());
        m = p.matcher(input);
        while (m.find()) {
            System.out.println("Found: " + m.group() + " [" + m.start() + ", " + m.end() + ")\n" +
                    "   var: " + m.group(1) + " [" + m.start(1) + ", " + m.end(1) + ")\n" +
                    "   func: " + m.group(2) + " [" + m.start(2) + ", " + m.end(2) + ")");
        }
    }
}
```

output of above:

```text
Forward Search: ([a-zA-z_][a-zA-z_0-9]*)\s*=\s*\1\.([a-zA-z_][a-zA-z_0-9]*)\(\)
Found: abc = abc.trim() [2, 18)
   var: abc [2, 5)
   func: trim [12, 16)
Found: def = def.trimEnd() [21, 40)
   var: def [21, 24)
   func: trimEnd [31, 38)

Reverse Search: \)\(([a-zA-z_0-9]*[a-zA-z_])\.([a-zA-z_0-9]*[a-zA-z_])\s*=\s*\2
Found: def = def.trimEnd() [21, 40)
   var: def [27, 30)
   func: trimEnd [31, 38)
Found: abc = abc.trim() [2, 18)
   var: abc [8, 11)
   func: trim [12, 16)
```

:information_source: Offsets for **`var`** part of the regular expression differ between forward
and backward searches because of the difference in which occurrence is treated as a capturing
group vs back reference. In the forward search it is the first occurrence of the variable that
is captured with the second occurrence being a back reference. In the reverse search it is the
second occurrence that is captured with the first occurrence treated as a back reference.

### Motivation

I needed backward search functionality for the **[Mia: Missing In Actions]** plugin and
preferred to use regular expressions in the implementation. A search for a solution to reverse
searching using regex in Java yielded:

* references that it is supported by .NET but not Java, which were not much help
* trivial suggestions to do a forward search on a reversed sequence which obviously would not
  work without also reversing the regular expression which is used
* solutions that reversed the characters of sequences between `|` and flipped `^` and `$` and
  called it a day, which were better than nothing but they were not implemented in Java and were
  far from being able to handle any valid regex.

I wanted to have something that would be robust enough to handle any valid Java regular
expression to allow me to forget about the reverse regex implementation and focus on writing
plugin code and the use cases only required to:

* generate reversed regular expression
* wrap the original input sequence into `ReversedCharSequence`
* use standard Java regex support with `ReverseMatcher` wrapper class to make mapping from
  reversed sequence offsets and changed captured groups transparent.
* the matches should be identical to original regex but in reverse order as you would expect
  from a backwards search.
* backwards replacements should also work

The result is the `reverse-regex-util` library that does all of the above and makes it painless
to perform backward searches as easily as forward searches.

### Q: What RegEx constructs are supported? A: All

To make reverse search work requires reversing all affected parts of the regular expression so
that when it is applied to a reversed version of the text it will result in the same matches but
in reverse order.

Analyzing the supported regex syntax the following were isolated as needing changes, where `X`
is original sequence of characters and `rX` is the reverse of `X`,

- If `X` consists of a single character `x` then `x` === `X` === `rX` otherwise `rX` is the
  sequence of characters in `X`, reversed.

- `XY` - converted to `rYrX`

- `X|Y` - converted to `rY|rX` to ensure that order dependent matches will be reflected in
  reversed search. The order of alternate matches affects JDK 1.7 but not JDK 1.8

- `(X)` - converted to `(rX)`

- `(?:X)` - converted to `(?:rX)`

- `(?<name>X)` - converted to `(?<name>rX)`

- `(?>X)` - converted to `(?>rX)`

- `^` - converted to `$`

- `$` - converted to `^`

- `\A` - converted to `\z`

- `\z` - converted to `\A`

- `\Z` - converted to `\A`

- `(?=X)` - converted to `(?<=rX)`, zero-width positive lookbehind

- `(?!X)` - converted to `(?<!rX)`, zero-width negative lookbehind

- `(?<=X)` - converted to `(?=rX)`, zero-width positive lookahead

- `(?<!X)` - converted to `(?!rX)`, zero-width negative lookahead

- If `LITERAL` flag is not used then:
  - all characters between `\Q` and `\E` are output between `\Q` and `\E` but in reversed
    sequence.

- If `COMMENTS` flag is used then
  - `#.*\n` is left as is otherwise it is treated as a normal sequence and reversed

- **Capturing Groups and Back References**

  The last back reference has to be changed to the capturing group, since it will be the first
  occurrence that is found in the reversed text. The original capturing group must be changed to
  a back reference.

  Numbered back references have to be re-numbered because their appearance in the reversed
  pattern will not generally correspond to the the same group number as in the original pattern.

  Named back references are also affected in that the last back reference in the original
  pattern has to be changed to the named capturing group and the capturing group to a named back
  reference, since in reverse it will be the last back reference that will be encountered first.

- everything else can passed through as is.

### Utility Classes

- `ReversePattern` class provides the parsing and reverse RegEx generating capability in
  addition to mimicking the `Pattern` class functionality for reverse searches.

- `ForwardPattern` class is a wrapper for `Pattern` that implements `RegExPattern` interface to
  allow access to forward and backward searches through a single type.

- `RegExPattern` is an interface to allow access to `ForwardPattern` wrapper and
  `ReversePattern` classes through a single type as would be required if your code can work with
  both search types.

- `ReverseMatcher` class provides the equivalent to `Matcher` for backwards searches that maps
  offsets, group numbers and reverses start/end offsets as needed so that your code works with
  the original, not reversed, regex references when using reverse regex search.

  :warning: `StringBuffer` append functions do not reverse the contents appended to the buffer
  because the reversal has to be performed on the final buffer results. The
  `ReverseMatcher.appendReplacement(StringBuffer, String)` will reverse the characters in the
  second parameter so that when the final string buffer results are reversed the result will be
  as expected.

- `ForwardPattern` class is a wrapper for `Matcher` that implements `RegExMatcher` interface to
  allow access to forward and backward searches through a single type.

- `RegExMatcher` is an interface to allow access to `ForwardMatcher` wrapper and
  `ReverseMatcher` classes through a single type as would be required if your code can work with
  both search types.

- `ReverseCharSequence` interface to be implemented by all char sequences that represent a
  reversed version of an original.

- `ReversedCharSequence` implementation of `ReverseCharSequence` for use on `String` and
  `CharSequence` arguments whose content needs reversing.

Contributing
------------

Pull requests, issues and comments welcome :smile:. For pull requests:

* Add tests for new features and bug fixes
* Follow the existing style to make merging easier, as much as possible: 4 space indent,
  trailing spaces trimmed.

* * *

License
-------

Copyright (c) 2016-2017, Vladimir Schneider <vladimir.schneider@gmail.com>,

GNU LESSER GENERAL PUBLIC LICENSE Version 2.1 licensed, see [LICENSE] file.

[LICENSE]: https://github.com/vsch/reverse-regex/blob/master/LICENSE
[Mia: Missing In Actions]: https://plugins.jetbrains.com/idea/plugin/9257-missing-in-actions
[All about me]: https://vladsch.com/about
[Android Studio]: http://developer.android.com/sdk/installing/studio.html
[GitHub Issues page]: https://github.com/vsch/reverse-regex/issues
[Markdown Navigator]: http://vladsch.com/product/markdown-navigator
[Maven Central status]: https://img.shields.io/maven-central/v/com.vladsch.reverse-regex/reverse-regex.svg
[MultiMarkdown]: http://fletcherpenney.net/multimarkdown/
[Semantic Versioning]: http://semver.org/
[reverse-regex]: https://github.com/vsch/reverse-regex
[reverse-regex on Maven]: https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.vladsch.reverse-regex%22
[reverse-regex wiki]: https://github.com/vsch/reverse-regex/wiki

