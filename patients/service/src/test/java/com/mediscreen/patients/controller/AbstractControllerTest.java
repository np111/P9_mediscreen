package com.mediscreen.patients.controller;

import au.com.origin.snapshots.SnapshotMatcher;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@ComponentScan(basePackages = {"com.mediscreen.common.spring"})
@ExtendWith(SnapshotExtension.class)
public abstract class AbstractControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected void toMatchSnapshot(MvcResult firstObject, MvcResult... objects) throws Exception {
        SnapshotMatcher.expect(asResponse(firstObject), asResponses(objects)).toMatchSnapshot();
    }

    private static Object asResponse(MvcResult result) throws Exception {
        StringBuilder sb = new StringBuilder().append("-\n");

        MockHttpServletRequest request = result.getRequest();
        sb.append("> ").append(request.getMethod()).append(" ").append(request.getPathInfo());
        if (request.getQueryString() != null) {
            sb.append("?").append(request.getQueryString());
        }
        forEnumeration(request.getHeaderNames()).forEachRemaining(headerName -> {
            forEnumeration(request.getHeaders(headerName)).forEachRemaining(headerValue -> {
                sb.append("\n> ").append(headerName).append(": ").append(headerValue);
            });
        });
        String requestContent = request.getContentAsString();
        if (requestContent != null) {
            sb.append("\n").append(encodeBody(requestContent));
        }

        MockHttpServletResponse response = result.getResponse();
        sb.append("\n< HTTP ").append(response.getStatus());
        response.getHeaderNames().forEach(headerName -> {
            response.getHeaderValues(headerName).forEach(headerValue -> {
                sb.append("\n< ").append(headerName).append(": ").append(headerValue);
            });
        });
        sb.append("\n").append(encodeBody(response.getContentAsString()));

        return sb.toString();
    }

    private static Object[] asResponses(MvcResult[] results) throws Exception {
        Object[] ret = new Object[results.length];
        for (int i = 0; i < results.length; ++i) {
            ret[i] = asResponse(results[i]);
        }
        return ret;
    }

    private static String encodeBody(String body) {
        return Arrays.stream(body.split("\r?\n")).map(line -> {
            try {
                return OBJECT_MAPPER.writeValueAsString(line);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.joining("\n"));
    }

    private static <T> Iterator<T> forEnumeration(Enumeration<T> enumeration) {
        return new Iterator<T>() {
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            public T next() {
                return enumeration.nextElement();
            }
        };
    }
}
