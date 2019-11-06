reverse-regex
=============

&nbsp;<details id="version-history"><summary>**Version History**</summary>

[TOC]: # " "

- [To Do](#to-do)
- [0.3.4](#034)
- [0.3.2](#032)
- [0.3.1](#031)
- [0.3.0](#030)
- [0.2.0](#020)
- [0.1.0](#010)


&nbsp;</details>

&nbsp;<details id="version-history"><summary>**To Do**</summary>

## To Do


&nbsp;</details>

0.3.4
-----

* change project java to 1.8
* reformat code

0.3.2
-----

* Change: min java version to 1.8
* Fix: pom url

0.3.1
-----

* Fix: #1, `\Q` `\E` not supported inside character classes

0.3.0
-----

* Add: `RegExMatcher` interface to allow access to reverse and forward searches through a single
  type.

* Add: `ForwardMatcher` wrapper class for `Matcher` that implements `RegExMatcher`

* Add: `RegExPattern` interface to allow access to reversed and forward pattern through a single
  type.

* Add: `ForwardPattern` wrapper class for `Pattern` which implements `RegExPattern`

* Change: rename `ReversedRegEx` to `ReversePattern`

0.2.0
-----

- Add: `ReverseMatcher`

- Add: tests for reversed regex, reverse matcher and reversed char sequence

0.1.0
-----

- Initial version commit

