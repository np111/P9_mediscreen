package com.mediscreen.common.restclient;

import com.mediscreen.common.api.model.ApiError;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpRestClient implements RestClient {
    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    private final HttpUrl baseUrl;
    private final RestSerializer serializer;
    private final OkHttpClient httpClient;

    @lombok.Builder
    private OkHttpRestClient(
            String baseUrl,
            RestSerializer serializer,
            Integer maxRequests,
            Integer maxRequestsPerHost,
            Integer maxIdleConnections,
            Duration keepAliveDuration,
            Duration callTimeout
    ) {
        Objects.requireNonNull(baseUrl, "baseUrl cannot be null");
        Objects.requireNonNull(serializer, "serializer cannot be null");
        maxRequests = initDefault(maxRequests, 64);
        maxRequestsPerHost = initDefault(maxRequestsPerHost, maxRequests);
        maxIdleConnections = initDefault(maxIdleConnections, 5);
        keepAliveDuration = initDefault(keepAliveDuration, Duration.ofMinutes(5));
        callTimeout = initDefault(callTimeout, Duration.ofSeconds(30));

        this.baseUrl = HttpUrl.parse(baseUrl);
        this.serializer = serializer;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);
        this.httpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration.toMillis(), TimeUnit.MILLISECONDS))
                .dispatcher(dispatcher)
                .callTimeout(callTimeout)
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ZERO)
                .writeTimeout(Duration.ZERO)
                .build();
    }

    @Override
    public <T> CompletableFuture<RestResponse<T>> call(RestCall<T> call) {
        Request.Builder req = new Request.Builder();

        HttpUrl.Builder url = baseUrl.newBuilder();
        call.paths.forEach(url::addPathSegment);
        for (Iterator<String> it = call.queryParams.iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = it.next();
            url.addQueryParameter(key, value);
        }
        req.url(url.build());

        req.method(call.method, createBody(call.body));

        CompletableFuture<RestResponse<T>> ret = new CompletableFuture<>();
        httpClient
                .newCall(req.build())
                .enqueue(new Callback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(Call okCall, Response response) {
                        //noinspection TryFinallyCanBeTryWithResources
                        try {
                            int httpStatus = response.code();
                            switch (httpStatus / 100) {
                                case 2:
                                    Object bodyObj;
                                    if (httpStatus == 204) {
                                        bodyObj = null;
                                    } else {
                                        Reader body = Objects.requireNonNull(response.body()).charStream();
                                        bodyObj = call.responseIsList ? serializer.deserializeList(body, call.responseType) : serializer.deserialize(body, call.responseType);
                                    }
                                    ret.complete(new SuccessRestResponse<>((T) bodyObj));
                                    break;

                                case 4:
                                    Reader body = Objects.requireNonNull(response.body()).charStream();
                                    ApiError apiError = serializer.deserialize(body, ApiError.class);
                                    ret.complete(new FailureRestResponse<>(apiError));
                                    break;

                                default:
                                    throw new IllegalStateException("Unsupported HTTP status: " + httpStatus);
                            }
                        } catch (Throwable ex) {
                            ret.completeExceptionally(ex);
                        } finally {
                            response.close();
                        }
                    }

                    @Override
                    public void onFailure(Call okCall, IOException ex) {
                        ret.completeExceptionally(ex);
                    }
                });
        return ret;
    }

    private RequestBody createBody(Object body) {
        if (body == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try {
            serializer.serialize(sw, body);
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode body", e);
        }
        return RequestBody.create(APPLICATION_JSON, sw.toString());
    }

    private static <T> T initDefault(T a, T b) {
        return a != null ? a : b;
    }
}
