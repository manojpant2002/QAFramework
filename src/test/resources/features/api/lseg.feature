Feature: API Authentication

  Scenario: Successful authentication with valid credentials
    When I make a request to "/en" with credentials
    Then the response status code should be 200