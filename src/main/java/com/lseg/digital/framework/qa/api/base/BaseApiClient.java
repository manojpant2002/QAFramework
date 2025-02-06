package com.lseg.digital.framework.qa.api.base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import static io.restassured.RestAssured.given;

import java.util.Map;

@Slf4j
public class BaseApiClient {
    protected RequestSpecification request;
    protected String baseUrl;
    protected String username;
    protected String password;

    private String CONTENT_TYPE_HEADER = "Content-Type";
    private String DEFAULT_CONTENT_TYPE = "application/json";

    public BaseApiClient() {
        log.debug("Initializing BaseApiClient");
        this.baseUrl = System.getProperty("apiBaseUrl");
        initializeRequest();
    }

    private void initializeRequest() {
        log.debug("Setting up base request specification with URL: {}", baseUrl);
        this.request = given()
            .baseUri(baseUrl)
            .contentType(DEFAULT_CONTENT_TYPE);
        log.info("Base request specification initialized");
    }

    /**
     * Initialize the base URL for the API client
     * @param baseUrl the base URL for API requests
     * @return BaseApiClient instance for method chaining
     */
    public BaseApiClient withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        this.request.baseUri(baseUrl);
        return this;
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
        return pathParams != null ? req.get(endpoint, pathParams) : req.get(endpoint);
    }

    public Response get(String endpoint, Map<String, String> queryParams) {
        return get(endpoint, queryParams, null, (Object[]) null);
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
        return pathParams != null ? req.post(endpoint, pathParams) : req.post(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return post(endpoint, body, null, (Object[]) null);
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
        return pathParams != null ? req.put(endpoint, pathParams) : req.put(endpoint);
    }

    public Response put(String endpoint, Object body) {
        return put(endpoint, body, null, (Object[]) null);
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
        return pathParams != null ? req.delete(endpoint, pathParams) : req.delete(endpoint);
    }

    public Response delete(String endpoint) {
        return delete(endpoint, null, null, (Object[]) null);
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
        return pathParams != null ? req.patch(endpoint, pathParams) : req.patch(endpoint);
    }

    public Response patch(String endpoint, Object body) {
        return patch(endpoint, body, null, (Object[]) null);
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
        return req.options(endpoint);
    }

    /**
     * Overloaded OPTIONS method without query parameters
     */
    public Response options(String endpoint) {
        return options(endpoint, null);
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
        return req.head(endpoint);
    }

    /**
     * Overloaded HEAD method without query parameters
     */
    public Response head(String endpoint) {
        return head(endpoint, null);
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
     * Sets authentication token
     * @param token Bearer token
     * @return BaseApiClient instance for method chaining
     */
    public BaseApiClient setBearerToken(String token) {
        log.debug("Setting authorization token");
        request.header("Authorization", "Bearer " + token);
        log.info("Authorization token set successfully");
        return this;
    }

    /**
     * Sets basic authentication credentials
     * @param username username
     * @param password password
     * @return BaseApiClient instance for method chaining
     */
    public BaseApiClient setBasicAuth(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Resets the request specification to default
     */
    public void resetRequestSpec() {
        this.request = RestAssured.given()
                .contentType(DEFAULT_CONTENT_TYPE);
        if (this.baseUrl != null) {
            this.request.baseUri(this.baseUrl);
        }
        this.username = null;
        this.password = null;
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
} 