Feature: API Authentication

  Scenario: Successful API login
    Given I have valid API credentials
    When I make a POST request to "/login" with credentials
    Then the response status code should be 200
    And the response should contain an auth token 