reverse-regex
=============

**reverse-regex** is a Java utility library that allows Java to perform backward searches using
standard RegEx Java classes on a reversed character sequence of original text.

### Requirements

* Java 7 or above
* The core has no dependencies

[![Build status](https://travis-ci.org/vsch/reverse-regex.svg?branch=master)](https://travis-ci.org/vsch/reverse-regex)
[![Maven Central status](https://img.shields.io/maven-central/v/com.vladsch.reverse-regex/reverse-regex.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.vladsch.reverse-regex%22)

### Motivation

I searched for a solution to reverse searching using RegEx in Java and found references that it
is supported by .NET but not Java. There were also trivial suggestions such as do a forward
search on a reversed sequence which obviously would not work without also reversing the regular
expression which is used.

Some simpler solutions reversed the characters of sequences between `|` and called it a day. I
wanted to have something that would be robust to handle any valid Java regular expression with
the use case only requiring:

* generate reversed regular expression
* wrap the original sequence into ReverseCharSequence
* use standard Java regex support library with ReverseRegEx wrapper classes to make mapping from
  reversed sequence offsets and changed captured groups transparent.
* the matches should be identical to original regex but in reverse order as you would expect
  from a backwards search.
* backwards replacements should be possible by using included utility classes

### What is supported

To make reverse search work it requires reversing all affected parts of the regular expression
so that when applied to a reversed version of the text it results in the same matches.

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

  To make the reverse regex work, the last back reference has to be changed to the capturing
  group, since it will be the first occurrence that is found in the reversed text. The original
  capturing group should be changed to a back reference.

  Numbered back references have to be re-numbered because their appearance in the reversed
  pattern will not generally correspond to the the same group number as in the original pattern.

  Named back references are also affected in that the last back reference in the original
  pattern has to be changed to the named capturing group and the capturing group to a named back
  reference, since in reverse it will be the last back reference that will be encountered first.

- everything else can passed through as is.

Some examples:

| Original                             | Original Match                                 | Reversed                             | Reversed Match                                 |
|--------------------------------------|------------------------------------------------|--------------------------------------|------------------------------------------------|
| `(a(b))(c)\3\2\1`                    | `abccbab`                                      | `((b)a)\2(c)\3\1`                    | `babccba`                                      |
| `(1(b)\2)(c)\3\2\1`                  | `1bbccb1bb`                                    | `((b)(?:\2)1)\2(c)\3\1`              | `bb1bccbb1`                                    |
| `(1(abc)\2)(def)\3\2\1`              | `1abcabcdefdefabc1abcabc`                      | `((cba)(?:\2)1)\2(fed)\3\1`          | `cbacba1cbafedfedcbacba1`                      |
| `(?<A>a)(b)(c)\3\2\k<A>`             |                                                | `(?<A>a)(b)(c)\3\2\k<A>`             |                                                |
| `^(1(abc)\2)(def)\3\2\1(?=xyz)`      | `1abcabcdefdefabc1abcabc` if followed by `xyz` | `(?<=zyx)((cba)(?:\2)1)\2(fed)\3\1$` | `cbacba1cbafedfedcbacba1` if preceded by `zyx` |
| `(a(b))(c)\Q(a(b))(c)\3\2\1\E\3\2\1` |                                                | `((b)a)\2(c)\Q1\2\3\)c())b(a(\E\3\1` |                                                |

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

