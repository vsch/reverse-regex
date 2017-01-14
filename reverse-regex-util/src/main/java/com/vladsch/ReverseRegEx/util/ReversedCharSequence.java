package com.vladsch.ReverseRegEx.util;

/**
 * CharSequence that is the reverse of the given sequence
 * <p>
 * The hashCode is purposefully matched to the string equivalent or this.toString().hashCode()
 */
public class ReversedCharSequence extends ReverseIndexMapperBase implements ReverseCharSequence {
    private final CharSequence myChars;
    private final int myStartIndex;
    private final int myEndIndex;
    private int myHash;
    private IndexMapper myMapper;

    @Override
    public CharSequence getReversedChars() {
        return myChars;
    }

    public int getStartIndex() {
        return myStartIndex;
    }

    @SuppressWarnings("WeakerAccess")
    private ReversedCharSequence(CharSequence chars, int start, int end) {
        if (start < 0 || end > chars.length() || start > end)
            throw new IndexOutOfBoundsException("[" + start + "," + end + ") not in [0," + length() + ")");
        myChars = chars;
        myStartIndex = start;
        myEndIndex = end;
        myMapper = null;
    }

    @Override
    public IndexMapper getIndexMapper() {
        if (myMapper == null) {
            myMapper = new ReverseIndexMapper(myEndIndex);
        }
        return myMapper;
    }

    @Override
    public int getEndIndex() {
        return myEndIndex;
    }

    @Override
    public int length() {
        return myEndIndex - myStartIndex;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length()) throw new IndexOutOfBoundsException("" + index + " not in [0," + length() + ")");
        return myChars.charAt(mapIndex(index));
    }

    @Override
    public ReversedCharSequence subSequence(int start, int end) {
        if (start < 0 || end > length())
            throw new IndexOutOfBoundsException("[" + start + ", " + end + ") not in [0," + length() + "]");
        final int startIndex = mapBoundary(end);
        final int endIndex = startIndex + end - start;
        return startIndex == myStartIndex && endIndex == myEndIndex ? this : new ReversedCharSequence(myChars, startIndex, endIndex);
    }

    @Override
    public String toString() {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(length());
        sb.append(this);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharSequence)) return false;

        if (o instanceof String || o instanceof ReversedCharSequence) {
            return hashCode() == o.hashCode();
        }

        CharSequence os = (CharSequence) o;
        if (length() != os.length()) return false;
        int iMax = length();
        for (int i = 0; i < iMax; i++) {
            if (charAt(i) != os.charAt(i)) return false;
        }

        return true;
    }

    /**
     * Make it equal the same hash code as the string it represents reversed
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int h = myHash;
        if (h == 0 && length() > 0) {
            for (int i = myEndIndex; i-- > myStartIndex; ) {
                h = 31 * h + myChars.charAt(i);
            }
            myHash = h;
        }
        return h;
    }

    public static ReversedCharSequence of(final CharSequence chars) {
        return of(chars, 0, chars.length());
    }

    public static ReversedCharSequence of(final CharSequence chars, final int start) {
        return of(chars, start, chars.length());
    }

    public static ReversedCharSequence of(final CharSequence chars, final int start, final int end) {
        if (chars instanceof ReversedCharSequence) {
            final ReversedCharSequence reversedChars = (ReversedCharSequence) chars;
            if (reversedChars.myChars instanceof ReversedCharSequence) {
                final int startIndex = reversedChars.mapBoundary(end);
                final int endIndex = startIndex + end - start;
                return startIndex == 0 && endIndex == chars.length() ? (ReversedCharSequence) reversedChars.myChars : ((ReversedCharSequence) reversedChars.myChars).subSequence(startIndex, endIndex);
            }
        }
        return new ReversedCharSequence(chars, start, end);
    }
}
