package com.mediscreen.common.restclient;

import com.mediscreen.common.util.StringBuilderWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
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

/**
 * A {@link RestClient} implementation using the OkHttp library.
 */
public class OkHttpRestClient extends AbstractRestClient {
    private static final MediaType OKHTTP_CONTENT_TYPE = MediaType.parse(AbstractRestClient.CONTENT_TYPE);

    private final HttpUrl baseUrl;
    private final RestSerializer serializer;
    private final OkHttpClient httpClient;

    /**
     * @param baseUrl            Base url of the target service (eg. "https://api.example.com/v1/").
     * @param serializer         Serializer to be used to encode/decode body.
     * @param maxRequests        Maximum number of queries to run simultaneously (others will be queued; default: 64)
     * @param maxIdleConnections Maximum number of idle connections to keep open for future use (default: 5)
     * @param keepAliveDuration  HTTP keep alive duration.
     * @param callTimeout        Maximum duration of a call before timeout.
     */
    @lombok.Builder
    private OkHttpRestClient(
            String baseUrl,
            RestSerializer serializer,
            Integer maxRequests,
            Integer maxIdleConnections,
            Duration keepAliveDuration,
            Duration callTimeout
    ) {
        Objects.requireNonNull(baseUrl, "baseUrl cannot be null");
        Objects.requireNonNull(serializer, "serializer cannot be null");
        maxRequests = initDefault(maxRequests, 64);
        maxIdleConnections = initDefault(maxIdleConnections, 5);
        keepAliveDuration = initDefault(keepAliveDuration, Duration.ofMinutes(5));
        callTimeout = initDefault(callTimeout, Duration.ofSeconds(30));

        this.baseUrl = HttpUrl.parse(baseUrl);
        this.serializer = serializer;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequests);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration.toMillis(), TimeUnit.MILLISECONDS))
                .dispatcher(dispatcher)
                .callTimeout(callTimeout)
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ZERO)
                .writeTimeout(Duration.ZERO);
        customizeOkHttpClient(httpClientBuilder);
        this.httpClient = httpClientBuilder.build();
    }

    /**
     * An overridable method to customize the underlying OkHttp client in an advanced way.
     */
    public void customizeOkHttpClient(OkHttpClient.Builder builder) {
    }

    @Override
    protected RestSerializer getSerializer() {
        return serializer;
    }

    @Override
    protected <T> void doCall(RestCall<T> call, BiConsumer<Throwable, HttpResponse> callback) {
        Request.Builder req = new Request.Builder();

        // Set url
        HttpUrl.Builder url = baseUrl.newBuilder();
        call.paths.forEach(url::addPathSegment);
        for (Iterator<String> it = call.queryParams.iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = it.next();
            url.addQueryParameter(key, value);
        }
        req.url(url.build());

        // Set method and body
        req.method(call.method, createBody(call.body));

        // Run the call
        httpClient.newCall(req.build()).enqueue(new Callback() {
            @Override
            public void onResponse(Call okCall, Response response) {
                callback.accept(null, new HttpResponse() {
                    @Override
                    public int getStatusCode() {
                        return response.code();
                    }

                    @Override
                    public Reader getBody() {
                        return Objects.requireNonNull(response.body()).charStream();
                    }

                    @Override
                    public void close() {
                        response.close();
                    }
                });
            }

            @Override
            public void onFailure(Call okCall, IOException ex) {
                callback.accept(ex, null);
            }
        });
    }

    private RequestBody createBody(Object body) {
        if (body == null) {
            return null;
        }

        StringBuilderWriter sw = new StringBuilderWriter();
        try {
            serializer.serialize(sw, body);
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode body", e);
        }
        return RequestBody.create(OKHTTP_CONTENT_TYPE, sw.toString());
    }

    private static <T> T initDefault(T a, T b) {
        return a != null ? a : b;
    }
}
