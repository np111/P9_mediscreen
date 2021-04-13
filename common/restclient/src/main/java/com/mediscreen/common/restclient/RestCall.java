package com.mediscreen.common.restclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class RestCall<T> {
    final Class<?> responseType;
    final boolean responseIsList;
    final List<String> paths = new ArrayList<>();
    final List<String> queryParams = new ArrayList<>();
    String method = "GET";
    Object body;

    public static <T> RestCall<T> of(Class<T> type) {
        return new RestCall<>(type, false);
    }

    public static <T> RestCall<List<T>> ofList(Class<T> type) {
        return new RestCall<>(type, true);
    }

    public RestCall<T> path(@NonNull String pathSegment) {
        this.paths.add(pathSegment);
        return this;
    }

    public RestCall<T> query(@NonNull String key, @NonNull String value) {
        this.queryParams.add(key);
        this.queryParams.add(value);
        return this;
    }

    public RestCall<T> method(@NonNull String method) {
        this.method = method;
        return this;
    }

    public RestCall<T> body(@NonNull Object body) {
        this.body = body;
        return this;
    }

    public RestCall<T> post(@NonNull Object body) {
        return this.method("POST").body(body);
    }

    public RestCall<T> apply(Consumer<RestCall<T>> consumer) {
        consumer.accept(this);
        return this;
    }
}
