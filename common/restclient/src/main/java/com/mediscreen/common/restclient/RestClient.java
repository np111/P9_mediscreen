package com.mediscreen.common.restclient;

import java.util.concurrent.CompletableFuture;

/**
 * Asynchronous client to perform HTTP requests on REST services.
 */
public interface RestClient {
    /**
     * Execute an HTTP call.
     *
     * @param call The call parameters
     * @param <T>  The call result type
     * @return The call response
     */
    <T> CompletableFuture<RestResponse<T>> call(RestCall<T> call);
}
