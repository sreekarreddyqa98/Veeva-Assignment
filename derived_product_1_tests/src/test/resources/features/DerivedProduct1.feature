Feature: validation for DP1 website

  @DP1_test @Sanity
  Scenario: Validation of the Slides from the Home Page
    Given I navigate to "DP1_Home_Page"
#    And I accept cookies
    And I click on "closeSignUp" from page "HomePage"
    Then I count the instances of "slides" from "HomePage" page
    And I validate element count to be "5"
    And I get the title of each slide and validate with expected data from file
    Then I validate the slide durations should be equal to "11" seconds
