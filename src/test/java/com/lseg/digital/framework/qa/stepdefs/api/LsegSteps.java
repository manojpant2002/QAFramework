package com.lseg.digital.framework.qa.stepdefs.api;

import com.lseg.digital.framework.qa.api.base.BaseApiClient;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import static org.testng.Assert.*;

@Slf4j
public class LsegSteps {
    BaseApiClient baseApiClient = new BaseApiClient();
    Response response;

    @When("I make a request to {string} with credentials")
    public void i_make_request_with_credentials(String endpoint) {
        log.info("Making GET request to endpoint: {}", endpoint);
        response = baseApiClient.get(endpoint,null,null);
        log.info("Response: {}", response.asString());
        log.info("GET request completed with status code: {}", response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void verify_status_code(int expectedStatusCode) {
        log.info("Verifying response status code. Expected: {}, Actual: {}",
                expectedStatusCode, response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode,
                "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
    }
}