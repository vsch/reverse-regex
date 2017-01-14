package com.vladsch.ReverseRegEx.util;

public interface IndexMapper {
    IndexMapper NULL = new IndexMapper() {
        @Override
        public int mapIndex(final int index) {
            return index;
        }

        @Override
        public int mapBoundary(final int index) {
            return index;
        }
    };

    int mapIndex(int index);
    int mapBoundary(int index);
}
