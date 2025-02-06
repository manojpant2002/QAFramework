package com.lseg.digital.framework.qa.stepdefs.ui;

import com.lseg.digital.framework.qa.driver.DriverManager;
import com.lseg.digital.framework.qa.ui.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginSteps {
    private LoginPage loginPage;

    @Before
    public void setUp() {
        DriverManager.initializeDriver();
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        // Initialize LoginPage with WebDriver from DriverManager
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("I should be logged in successfully")
    public void iShouldBeLoggedIn() {
        // Verify successful login
    }
} 