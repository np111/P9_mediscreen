package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;

public interface RestResponse<T> {
    boolean isSuccess();

    T getResult();

    ApiError getError();
}
