package com.lseg.digital.framework.qa.stepdefs.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class AuthenticationSteps {
    private RequestSpecification request;
    private Response response;
    private String username;
    private String password;

    @Given("I have valid API credentials")
    public void i_have_valid_api_credentials() {
        // Initialize credentials from configuration or environment variables
        username = System.getProperty("api.username", "testuser");
        password = System.getProperty("api.password", "testpass");
        
        // Initialize REST Assured request
        request = given()
            .contentType("application/json")
            .baseUri(System.getProperty("apiBaseUrl"));
    }

    @When("I make a POST request to {string} with credentials")
    public void i_make_a_post_request_to_with_credentials(String endpoint) {
        // Create request body using Java 11 compatible string format
        String requestBody = String.format(
            "{"
            + "\"username\": \"%s\","
            + "\"password\": \"%s\""
            + "}", 
            username, password
        );

        // Make the POST request
        response = request
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertEquals(response.getStatusCode(), expectedStatusCode.intValue(),
            "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
    }

    @Then("the response should contain an auth token")
    public void the_response_should_contain_an_auth_token() {
        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Auth token should not be null");
        assertFalse(token.isEmpty(), "Auth token should not be empty");
    }
} 