package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A REST response.
 *
 * @param <T> The result type
 */
public interface RestResponse<T> {
    /**
     * Checks if the answer represents a success or a failure.
     */
    boolean isSuccess();

    /**
     * Return the successful response result.
     *
     * @return the result if {@link #isSuccess()} is {@code true}; or {@code null} otherwise
     */
    T getResult();

    /**
     * Return the failed response error.
     *
     * @return the error if {@link #isSuccess()} is {@code false}; or {@code null} otherwise
     */
    ApiError getError();

    @RequiredArgsConstructor
    @Data
    final class Success<T> implements RestResponse<T> {
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

    @RequiredArgsConstructor
    @Data
    final class Failure<T> implements RestResponse<T> {
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
}
