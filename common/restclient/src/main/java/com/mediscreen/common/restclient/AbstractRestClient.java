package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public abstract class AbstractRestClient implements RestClient {
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

    @SuppressWarnings("unchecked")
    @Override
    public final <T> CompletableFuture<RestResponse<T>> call(RestCall<T> call) {
        CompletableFuture<RestResponse<T>> ret = new CompletableFuture<>();
        AtomicBoolean called = new AtomicBoolean();
        try {
            doCall(call, (err, res) -> {
                if (called.getAndSet(true)) {
                    throw new IllegalStateException("this callback was already called");
                }

                if (err != null) {
                    ret.completeExceptionally(err);
                    return;
                }

                try {
                    int httpStatus = res.getStatusCode();
                    switch (httpStatus / 100) {
                        case 2:
                            // HTTP 2XX means that the call was successful!
                            // Decode the body and return it as the result
                            Object bodyObj;
                            if (httpStatus == 204) {
                                bodyObj = null;
                            } else {
                                bodyObj = call.responseIsList
                                        ? getSerializer().deserializeList(res.getBody(), call.responseType)
                                        : getSerializer().deserialize(res.getBody(), call.responseType);
                            }
                            ret.complete(new RestResponse.Success<>((T) bodyObj));
                            break;

                        case 4:
                            // HTTP 4XX means that the call failed!
                            // Decode the body and return it as the error
                            ApiError apiError = getSerializer().deserialize(res.getBody(), ApiError.class);
                            if (apiError.getType() == null) {
                                throw new IllegalArgumentException("ApiError type cannot be null");
                            }
                            if (apiError.getCode() == null) {
                                throw new IllegalArgumentException("ApiError code cannot be null");
                            }
                            if (apiError.getMessage() == null) {
                                throw new IllegalArgumentException("ApiError message cannot be null");
                            }
                            if (apiError.getMetadata() == null) {
                                apiError = apiError.withMetadata(new LinkedHashMap<>(0));
                            }
                            if (apiError.getType() != ApiErrorType.SERVICE) {
                                // Handle non-SERVICE types as an exception
                                ret.completeExceptionally(new ApiErrorException(apiError));
                            } else {
                                ret.complete(new RestResponse.Failure<>(apiError));
                            }
                            break;

                        default:
                            throw new IllegalArgumentException("Unsupported HTTP status: " + httpStatus);
                    }
                } catch (Throwable ex) {
                    ret.completeExceptionally(ex);
                } finally {
                    try {
                        res.close();
                    } catch (Throwable ignored) {
                    }
                }
            });
        } catch (Exception ex) {
            ret.completeExceptionally(ex);
        }
        return ret;
    }

    protected abstract RestSerializer getSerializer();

    protected abstract <T> void doCall(RestCall<T> call, BiConsumer<Throwable, HttpResponse> callback);

    protected interface HttpResponse {
        int getStatusCode();

        Reader getBody();

        void close();
    }
}
