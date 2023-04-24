# reverse-regex

[TOC]: # ""

- [1.0.2](#102)
- [1.0.0](#100)
- [0.3.6](#036)
- [0.3.4](#034)
- [0.3.2](#032)
- [0.3.1](#031)
- [0.3.0](#030)
- [0.2.0](#020)
- [0.1.0](#010)

## 1.0.2

* Change: version number. For some reason 1.0.0 was published but would not work as a
  dependency. It would silently fail to download the jar. Comparing local and github packages
  showed a difference on the module md5,sha1,sha256,sha512 checksums. Everything else was the
  same. Changing the version and republishing fixed the issue.

## 1.0.0

* Change: to gradle build
* Change: build to java 11
* Change: min java version 11

## 0.3.6

* Fix: improper parsing of `\Q\E` with odd number of backslashes would generate
  PatternSyntaxException.

## 0.3.4

* change project java to 1.8
* reformat code

## 0.3.2

* Change: min java version to 1.8
* Fix: pom url

## 0.3.1

* Fix: #1, `\Q` `\E` not supported inside character classes

## 0.3.0

* Add: `RegExMatcher` interface to allow access to reverse and forward searches through a single
  type.

* Add: `ForwardMatcher` wrapper class for `Matcher` that implements `RegExMatcher`

* Add: `RegExPattern` interface to allow access to reversed and forward pattern through a single
  type.

* Add: `ForwardPattern` wrapper class for `Pattern` which implements `RegExPattern`

* Change: rename `ReversedRegEx` to `ReversePattern`

## 0.2.0

* Add: `ReverseMatcher`

* Add: tests for reversed regex, reverse matcher and reversed char sequence

## 0.1.0

* Initial version commit

