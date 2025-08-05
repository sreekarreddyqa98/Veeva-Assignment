Feature: validation for CP website

  @CoreTests @Sanity
  Scenario: Capture all Men's Jackets with Price, Title, and Top Seller message from all pages
    Given I navigate to "CP_Home_Page"
    Then I skip signup
    And I accept cookies
    And I wait for "homePageAdBanner" to be visible in "HomePage" page
    Then I hover to the "shopMenuOption" in "HomePage" and click on "mens_category"
    Then I switch to "Men's Golden State Warriors Gear, Mens Warriors Apparel, Guys Clothing | shop.warriors.com" page
#    And I click on "offerBannerCloseBtn" from page "MensShopPage"
    And I click on "Jackets_Filter" from page "MensShopPage"
    Then I extract all the product details
    And I save data to text file as "Jerseys Products.txt"



  @CoreTests @Sanity
  Scenario: Validation of the Video Feeds
    Given I navigate to "CP_Home_Page"
    And I accept cookies
    Then I skip signup
    And I wait for "homePageAdBanner" to be visible in "HomePage" page
    Then I hover to the "moreOption" in "HomePage" and click on "News_and_features"
    And I count the instances of "allVideoFeeds" from "NewsAndFeedpage" page
    And I validate element count to be "21"
    Then I count the videos with "uploadTimeOfVideos" older than "3" days
    And I validate element count to be "20"