package com.study.simpleTodo;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class LombokTest {

    @NonNull
    private String id;
}
