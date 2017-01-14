package com.vladsch.ReverseRegEx.util;

public abstract class ReverseIndexMapperBase implements ReverseIndexHolder {
    public abstract int getEndIndex();

    @Override
    public IndexMapper getIndexMapper() {
        return this;
    }

    @Override
    public int mapIndex(final int index) {
        final int end = getEndIndex();
        if (index < 0 || index >= end) throw new IndexOutOfBoundsException("" + index + " not in [0," + end + ")");
        return end - 1 - index;
    }

    @Override
    public int mapBoundary(final int index) {
        final int end = getEndIndex();
        if (index < 0 || index > end) throw new IndexOutOfBoundsException("" + index + " not in [0," + end + "]");
        return end - index;
    }
}
