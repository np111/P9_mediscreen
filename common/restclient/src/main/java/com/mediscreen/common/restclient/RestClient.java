package com.mediscreen.common.restclient;

import java.util.concurrent.CompletableFuture;

public interface RestClient {
    <T> CompletableFuture<RestResponse<T>> call(RestCall<T> call);
}
