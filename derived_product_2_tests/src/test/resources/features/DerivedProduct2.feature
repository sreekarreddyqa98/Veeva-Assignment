Feature: validation for DP2 website

  @DP2_test @Sanity
  Scenario: Validation of the Slides from the Home Page
    Given I navigate to "DP2_Home_Page"
    And I accept cookies
    Then I count the instances of "slides" from "HomePage" page
    And I validate element count to be "5"
    And I get the title of each slide and validate with expected data from file
    Then I validate the slide durations should be equal to "10" seconds

  @DP2_test @Sanity
  Scenario: Validation Footer Links from the Home Page
    Given I navigate to "DP2_Home_Page"
    And I accept cookies
    Then I scroll to end of the Page
    And Capture all the footer links to file "FooterLinks.csv"
    Then validate all the links from file "FooterLinks.csv" are working
    And Validate No duplicate links are present in the "FooterLinks.csv"


