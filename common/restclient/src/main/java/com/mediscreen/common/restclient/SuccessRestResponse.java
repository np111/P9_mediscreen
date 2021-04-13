package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public final class SuccessRestResponse<T> implements RestResponse<T> {
    private final T result;

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public ApiError getError() {
        return null;
    }
}
