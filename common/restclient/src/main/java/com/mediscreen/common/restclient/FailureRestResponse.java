package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class FailureRestResponse<T> implements RestResponse<T> {
    private final @NonNull ApiError error;

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getResult() {
        return null;
    }

    @Override
    public ApiError getError() {
        return error;
    }
}
