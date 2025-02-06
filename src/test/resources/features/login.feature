Feature: Login Functionality

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter username "testuser" and password "testpass"
    Then I should be logged in successfully 