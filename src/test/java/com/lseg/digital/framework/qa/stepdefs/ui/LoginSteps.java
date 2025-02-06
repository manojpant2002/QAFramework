package com.lseg.digital.framework.qa.stepdefs.ui;

import com.lseg.digital.framework.qa.driver.DriverManager;
import com.lseg.digital.framework.qa.ui.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import com.lseg.digital.framework.qa.utils.SeleniumUtils;

@Slf4j
public class LoginSteps {
    private LoginPage loginPage;

    @Before
    public void setUp() {
        DriverManager.initializeDriver();
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @After
    public void tearDown() {
        SeleniumUtils.cleanupThreadLocals();
        DriverManager.quitDriver();
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        log.info("Initializing login page");
        loginPage = new LoginPage(DriverManager.getDriver());
        log.info("Login page initialized successfully");
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String username, String password) {
        log.info("Performing login with username: {}", username);
        loginPage.login(username, password);
        log.info("Login action completed");
    }

    @Then("I should be logged in successfully")
    public void iShouldBeLoggedIn() {
        // Verify successful login
    }
} 