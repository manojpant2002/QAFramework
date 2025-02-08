package com.lseg.digital.framework.qa.api.base;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import static io.restassured.RestAssured.given;
import java.util.Map;

@Slf4j
public class BaseApiClient {
    private static final ThreadLocal<RequestSpecification> requestThreadLocal = new ThreadLocal<>();
    protected RequestSpecification request;
    protected String baseUrl;
    protected String username;
    protected String password;
    protected final String CONTENT_TYPE_HEADER = "Content-Type";
    protected final String DEFAULT_CONTENT_TYPE = "application/json";

    public BaseApiClient() {
        log.debug("Initializing BaseApiClient with property file: {}", System.getProperty("baseUrl"));
        String baseUrl = System.getProperty("baseUrl");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalStateException("Base URL is not configured. Please check baseUrl is set properly");
        }
        this.baseUrl = baseUrl;
        initializeRequest();
    }

    private void initializeRequest() {
        log.debug("Setting up base request specification with URL: {}", baseUrl);
        this.request = given()
            .baseUri(baseUrl)
            .contentType(DEFAULT_CONTENT_TYPE);
        requestThreadLocal.set(this.request);
        log.info("Thread {} - Initialized request specification", Thread.currentThread().getId());
    }

    /**
     * Performs a GET request with path parameters
     */
    public Response get(String endpoint, Map<String, String> queryParams, Object body, Object... pathParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        if (body != null) {
            req.body(body);
        }
        Response response = pathParams != null ? req.get(endpoint, pathParams) : req.get(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }

    /**
     * Performs a POST request
     */
    public Response post(String endpoint, Object body, Map<String, String> queryParams, Object... pathParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        if (body != null) {
            req.body(body);
        }
        Response response = pathParams != null ? req.post(endpoint, pathParams) : req.post(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }

    /**
     * Performs a PUT request
     */
    public Response put(String endpoint, Object body, Map<String, String> queryParams, Object... pathParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        if (body != null) {
            req.body(body);
        }
        Response response = pathParams != null ? req.put(endpoint, pathParams) : req.put(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }

    /**
     * Performs a DELETE request
     */
    public Response delete(String endpoint, Object body, Map<String, String> queryParams, Object... pathParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        if (body != null) {
            req.body(body);
        }
        Response response = pathParams != null ? req.delete(endpoint, pathParams) : req.delete(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }

    /**
     * Performs a PATCH request
     */
    public Response patch(String endpoint, Object body, Map<String, String> queryParams, Object... pathParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        if (body != null) {
            req.body(body);
        }
        Response response = pathParams != null ? req.patch(endpoint, pathParams) : req.patch(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }

    /**
     * Performs an OPTIONS request
     * @param endpoint the API endpoint
     * @param queryParams optional query parameters
     * @return Response object
     */
    public Response options(String endpoint, Map<String, String> queryParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        Response response = req.options(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }


    /**
     * Performs a HEAD request
     * @param endpoint the API endpoint
     * @param queryParams optional query parameters
     * @return Response object
     */
    public Response head(String endpoint, Map<String, String> queryParams) {
        RequestSpecification req = prepareRequest();
        if (queryParams != null) {
            req.queryParams(queryParams);
        }
        Response response = req.head(endpoint);
        log.info("Response: {}", response.asString());
        return response;
    }

    /**
     * Adds headers to the request
     * @param headers Map of headers
     * @return BaseApiClient instance for method chaining
     */
    public BaseApiClient addHeaders(Map<String, String> headers) {
        if (headers != null) {
            // Check if Content-Type is specified in headers
            String contentType = headers.get(CONTENT_TYPE_HEADER);
            if (contentType != null) {
                // Remove the Content-Type from headers map to avoid duplicate
                headers.remove(CONTENT_TYPE_HEADER);
                // Set the Content-Type separately
                request.contentType(contentType);
            }
            // Add remaining headers
            request.headers(headers);
        }
        return this;
    }

    /**
     * Validates that base URL is set before making any request
     * @throws IllegalStateException if base URL is not set
     */
    private void validateBaseUrl() {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalStateException("Base URL must be set before making any requests. Use withBaseUrl() method.");
        }
    }

    /**
     * Prepares request specification with authentication if set
     * @return RequestSpecification with authentication applied
     */
    private RequestSpecification prepareRequest() {
        validateBaseUrl();
        RequestSpecification req = request.given();
        if (username != null && password != null) {
            req.auth().basic(username, password);
        }
        return req;
    }
    public void cleanup() {
        requestThreadLocal.remove();
        log.debug("Thread {} - Cleaned up API client resources", Thread.currentThread().getId());
    }
} 