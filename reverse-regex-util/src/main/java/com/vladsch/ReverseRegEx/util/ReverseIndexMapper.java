package com.vladsch.ReverseRegEx.util;

public class ReverseIndexMapper extends ReverseIndexMapperBase {
    private final int myEnd;

    @SuppressWarnings("WeakerAccess")
    public ReverseIndexMapper(final int end) {
        this.myEnd = end;
    }

    @Override
    public int getEndIndex() {
        return myEnd;
    }
}
