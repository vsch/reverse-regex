package com.vladsch.ReverseRegEx.util;

public interface IndexMapper {
    IndexMapper NULL = new IndexMapper() {
        @Override
        public int map(final int index) {
            return index;
        }
    };

    int map(int index);
}
