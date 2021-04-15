package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.util.exception.FastRuntimeException;
import lombok.Getter;

/**
 * Exception thrown when an unexpected {@link ApiError} is encountered.
 * During a {@linkplain RestClient#call(RestCall) call} this happens for {@linkplain ApiError errors} whose type
 * is not {@link ApiErrorType#SERVICE}.
 */
@Getter
public class ApiErrorException extends FastRuntimeException {
    private final ApiError apiError;

    public ApiErrorException(ApiError apiError) {
        super(apiError.getType() + ": " + apiError.getMessage());
        this.apiError = apiError;
    }
}
