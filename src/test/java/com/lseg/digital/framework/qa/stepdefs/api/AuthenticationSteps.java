package com.lseg.digital.framework.qa.stepdefs.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

@Slf4j
public class AuthenticationSteps {
    private RequestSpecification request;
    private Response response;
    private String username;
    private String password;

    @Given("I have valid API credentials")
    public void i_have_valid_api_credentials() {
        log.info("Starting API authentication test");
        username = System.getProperty("api.username", "testuser");
        password = System.getProperty("api.password", "testpass");
        log.debug("Using username: {}", username);
        
        request = given()
            .contentType("application/json")
            .baseUri(System.getProperty("apiBaseUrl"));
        log.info("API request initialized with baseUri: {}", System.getProperty("apiBaseUrl"));
    }

    @When("I make a POST request to {string} with credentials")
    public void i_make_a_post_request_to_with_credentials(String endpoint) {
        log.info("Making POST request to endpoint: {}", endpoint);
        String requestBody = String.format(
            "{"
            + "\"username\": \"%s\","
            + "\"password\": \"%s\""
            + "}", 
            username, password
        );
        log.debug("Request body prepared: {}", requestBody.replace(password, "****"));

        response = request
            .body(requestBody)
            .when()
            .post(endpoint);
        log.info("POST request completed with status code: {}", response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        log.info("Verifying response status code. Expected: {}, Actual: {}", 
            expectedStatusCode, response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode.intValue(),
            "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
    }

    @Then("the response should contain an auth token")
    public void the_response_should_contain_an_auth_token() {
        log.info("Checking for auth token in response");
        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Auth token should not be null");
        assertFalse(token.isEmpty(), "Auth token should not be empty");
        log.info("Auth token successfully verified");
        log.debug("Received token: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
    }
} 