package com.lseg.digital.framework.qa.api.base;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        BaseApiClient client = new BaseApiClient()
            .withBaseUrl("https://postman-echo.com");

        // Example 1: GET with Basic Authentication
        client.setBasicAuth("postman", "password");
        Response authResponse = client.get("/basic-auth", null, null);
        System.out.println("Basic Auth Response: " + authResponse.getBody().asString());
        System.out.println("Status Code: " + authResponse.getStatusCode());

        // Example 2: GET with Basic Auth and Path Parameters
        client.setBasicAuth("postman", "password");
        Response authParamResponse = client.get("/basic-auth/{param1}/{param2}", null, null, "value1", "value2");
        System.out.println("Basic Auth with Params Response: " + authParamResponse.getBody().asString());

        // Example 3: GET with Basic Auth, Path Params and Query Params
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key1", "value1");
        queryParams.put("key2", "value2");
        
        client.setBasicAuth("postman", "password");
        Response complexResponse = client.get("/basic-auth/{param}", queryParams, null, "test");
        System.out.println("Complex Auth Response: " + complexResponse.getBody().asString());
    }
}
