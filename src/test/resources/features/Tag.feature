Feature: verify tag creation
@Database
  Scenario: User must be able to create tag
    Given base url "https://backend.cashwise.us/"
    When user provides valid token
    And user provides request body with "name_tag" and "value9"
    Then user hits POST endpoint "/api/myaccount/tags"
    Then verify status code is 201
  Given user set up connection to database
  When user sends the query "select * from tags where tag_id = ?"
  Then verify result set contains "name_tag" with "TomCruz1"

  @Database22
  Scenario: User updates and checks the database
    Given base url "https://backend.cashwise.us/"
    When user provides valid token
    And user hits get all endpoint "/api/myaccount/tags/all"
    And user get the third tag and store it
    And user changes the tag_name to "Salavat12"
    And user hits PUT api "/api/myaccount/tags/"
    Given user set up connection to database
    When user sends the query "select * from tags where tag_id = ?"
    Then verify result set contains "name_tag" with "Salavat12"


