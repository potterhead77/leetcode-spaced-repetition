package com.nandan.spaced_repetition.repository;

public interface TagCountProjection {
    String getTagSlug();
    String getTagName();
    Long getTotalCount();
}