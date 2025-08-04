# Veeva Assignment - UI Automation Framework

## 📋 Project Overview

This is a comprehensive **Java-based UI Automation Framework** built using modern testing technologies including **TestNG**, **Cucumber BDD**, **Selenium WebDriver**, and **Rest Assured**. The framework follows a multi-module Maven architecture designed for scalable and maintainable test automation across different product lines.

## 🏗️ Architecture & Design Patterns

- **Multi-Module Maven Project**: Organized into separate modules for framework utilities and product-specific tests
- **Page Object Model (POM)**: Encapsulates web elements and page-specific actions
- **Behavior Driven Development (BDD)**: Uses Cucumber for readable test scenarios
- **ThreadLocal WebDriver**: Supports parallel test execution
- **Factory Design Pattern**: For browser initialization and management
- **Utility Classes**: Reusable components for common operations

## 📁 Project Structure

```
Veeva-Assignment/
├── automation_framework/          # Core framework utilities
│   ├── src/main/java/org/veeva/utilities/
│   │   ├── AllureReportUtils.java    # Allure reporting utilities
│   │   ├── BaseClass.java            # Base class for page objects
│   │   ├── ConfigReader.java         # Configuration file reader
│   │   ├── DriverFactory.java        # WebDriver factory with ThreadLocal
│   │   ├── ElementFetcher.java       # Dynamic element retrieval
│   │   ├── Hooks.java                # Cucumber hooks for setup/teardown
│   │   ├── LinkValidationUtil.java   # URL validation utilities
│   │   ├── RetryHandler.java         # Test retry mechanism
│   │   ├── RetryStep.java            # Step-level retry logic
│   │   ├── RunnerGenerator.java      # Dynamic test runner generation
│   │   └── Utilities.java            # Common utility methods
│   └── src/main/resources/configuration/
│       └── config.properties         # Environment configuration
├── core_product_tests/            # Core product test suite
│   ├── src/test/java/org/veeva/core/
│   │   ├── pageObjects/CoreProduct/  # Page object classes
│   │   ├── runner/                   # TestNG-Cucumber runner
│   │   └── stepDefinitions/          # Cucumber step definitions
│   ├── src/test/resources/
│   │   ├── features/                 # Cucumber feature files
│   │   └── test_data/                # Test data files
│   └── testng.xml                    # TestNG configuration
├── derived_product_1_tests/       # Derived product 1 test suite
│   ├── src/test/java/org/veeva/
│   │   ├── DP1_Utils/                # Product-specific utilities
│   │   ├── pageObjects/Pages/        # Page object classes
│   │   ├── runner/                   # TestNG-Cucumber runner
│   │   └── stepDefinitions/          # Cucumber step definitions
│   ├── src/test/resources/
│   │   ├── features/                 # Cucumber feature files
│   │   └── test_data/                # Test data files
│   └── testng.xml                    # TestNG configuration
├── derived_product_2_tests/       # Derived product 2 test suite
│   ├── allure-results/               # Allure test results
│   ├── src/test/java/org/veeva/dp2/
│   │   ├── DP2_Utils/                # Product-specific utilities
│   │   ├── pageObjects/Pages/        # Page object classes
│   │   ├── runner/                   # TestNG-Cucumber runner
│   │   └── stepDefinitions/          # Cucumber step definitions
│   ├── src/test/resources/
│   │   ├── features/                 # Cucumber feature files
│   │   └── test_data/                # Test data files
│   └── testng.xml                    # TestNG configuration
└── pom.xml                        # Parent POM with dependency management
```

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21 | Programming language |
| **Maven** | - | Build and dependency management |
| **Selenium WebDriver** | 4.14.0 | Web browser automation |
| **TestNG** | - | Test execution framework |
| **Cucumber** | 7.11.1 | BDD framework |
| **Allure** | 2.24.0 | Test reporting |
| **Rest Assured** | 5.5.5 | API testing |
| **SLF4J** | 2.0.13 | Logging framework |

## 🔧 Key Framework Components

### 1. **DriverFactory.java**
- **ThreadLocal WebDriver Management**: Ensures thread-safe parallel execution
- **Multi-Browser Support**: Chrome, Firefox, Edge
- **Headless Execution**: Configured for CI/CD environments
- **Custom Browser Options**: Anti-detection, window sizing, user agents

### 2. **BaseClass.java**
- **Abstract Base Class**: Foundation for all page objects
- **WebDriver Initialization**: Automatic PageFactory initialization
- **Utility Integration**: Built-in access to common utilities
- **Wait Management**: Centralized WebDriverWait configuration

### 3. **Hooks.java**
- **Cucumber Lifecycle Management**: Before/After scenario hooks
- **Screenshot Capture**: Automatic failure screenshots for Allure
- **Driver Cleanup**: Proper resource management
- **Allure Integration**: Browser labeling and metadata

### 4. **ElementFetcher.java**
- **Dynamic Element Retrieval**: Reflection-based element access
- **Wait Integration**: Automatic visibility waits
- **Type Safety**: Generic return types for elements and lists

### 5. **Utilities.java**
- **Common Actions**: Hover, click, scroll operations
- **Tab Management**: Window switching and title validation
- **Element Operations**: Count, visibility checks
- **XPath Extraction**: Dynamic locator retrieval

## 📊 Test Modules

### **Core Product Tests**
- **Target Application**: NBA Warriors website
- **Test Scenarios**:
  - Men's jacket product extraction with pricing
  - Video feed validation and age verification
- **Features**: Product data extraction, file operations

### **Derived Product 1 Tests**
- **Target Application**: NBA Sixers website
- **Test Scenarios**:
  - Homepage slide validation
  - Slide duration timing verification
- **Features**: File comparison, timing validations

### **Derived Product 2 Tests**
- **Target Application**: NBA Bulls website
- **Test Scenarios**:
  - Homepage slide validation with timing
  - Footer link validation and duplicate detection
- **Features**: CSV operations, link validation, duplicate detection

## 🚀 Getting Started

### Prerequisites
- **Java 21** or higher
- **Maven 3.6+**
- **Chrome/Firefox** browser installed
- **Git** for version control

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd Veeva-Assignment
   ```

2. **Install Dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure Environment**
   - Update `automation_framework/src/main/resources/configuration/config.properties`
   - Set browser preferences and target URLs

### Configuration Properties

```properties
# Browser Configuration
browser=chrome

# Application URLs
CP_Home_Page = https://www.nba.com/warriors/
DP1_Home_Page = https://www.nba.com/sixers/
DP2_Home_Page = https://www.nba.com/bulls/
```

## 🏃‍♂️ Execution Methods

### 1. **Maven Command Line**

**Run All Tests:**
```bash
mvn clean test
```

**Run Specific Module:**
```bash
mvn clean test -pl core_product_tests
mvn clean test -pl derived_product_1_tests
mvn clean test -pl derived_product_2_tests
```

**Run with Custom Tags:**
```bash
mvn clean test -Dcucumber.filter.tags="@Sanity"
mvn clean test -Dcucumber.filter.tags="@CoreTests"
```

**Run with Custom Browser:**
```bash
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=chrome
```

### 2. **TestNG XML Execution**

**Core Product Tests:**
```bash
mvn test -Dtest=RunCucumberTestCP -DsuiteXmlFile=core_product_tests/testng.xml
```

**Derived Product Tests:**
```bash
mvn test -Dtest=RunCucumberTestDP1 -DsuiteXmlFile=derived_product_1_tests/testng.xml
mvn test -Dtest=RunCucumberTestDP2 -DsuiteXmlFile=derived_product_2_tests/testng.xml
```

### 3. **Parallel Execution**
```bash
mvn clean test -Dparallel=tests -DthreadCount=3
```

## 📈 Reporting & Results

### **Allure Reports**

**Generate Allure Report:**
```bash
# Navigate to the specific test module directory
cd core_product_tests
# OR
cd derived_product_1_tests
# OR
cd derived_product_2_tests

# Generate Allure report from test results
allure generate src/test/resources/reports/allure-results --clean -o src/test/resources/reports/allure-report

# Open the generated report in browser
allure open src/test/resources/reports/allure-report
```

**Report Features:**
- ✅ Test execution timeline
- 📊 Pass/Fail statistics
- 📸 Failure screenshots
- 📋 Step-by-step execution details
- 🏷️ Test categorization and tagging
- 📎 File attachments (CSV, TXT data files)

### **Cucumber Reports**
- **HTML Reports**: `target/cucumber-reports.html`
- **JSON Reports**: `target/cucumber.json`
- **Console Output**: Detailed step execution logs

## 🏷️ Test Tags & Categories

| Tag | Purpose | Usage |
|-----|---------|-------|
| `@Sanity` | Smoke tests for critical functionality | Quick validation |
| `@CoreTests` | Core product specific tests | Main product testing |
| `@DP1_test` | Derived product 1 tests | Product variant testing |
| `@DP2_test` | Derived product 2 tests | Product variant testing |
| `@regression` | Full regression suite | Comprehensive testing |

## 🔍 Key Features

### **Advanced Capabilities**
- ✅ **Parallel Execution**: ThreadLocal WebDriver for concurrent testing
- ✅ **Cross-Browser Testing**: Chrome, Firefox, Edge support
- ✅ **Headless Execution**: CI/CD friendly configuration
- ✅ **Dynamic Element Handling**: Reflection-based element access
- ✅ **Retry Mechanism**: Automatic test retry on failures
- ✅ **Screenshot Capture**: Automatic failure documentation
- ✅ **File Operations**: CSV, TXT data handling
- ✅ **Link Validation**: URL accessibility verification
- ✅ **Data Extraction**: Product information scraping
- ✅ **Timing Validations**: Duration and performance checks

### **BDD Test Scenarios**

**Core Product Features:**
```gherkin
@CoreTests @Sanity
Scenario: Capture all Men's Jackets with Price, Title, and Top Seller message
  Given I navigate to "CP_Home_Page"
  And I accept cookies
  Then I skip signup
  And I hover to the "shopMenuOption" and click on "mens_category"
  Then I extract all the product details
  And I save data to text file as "Jerseys Products.txt"
```

**Derived Product Features:**
```gherkin
@DP2_test @Sanity
Scenario: Validation Footer Links from the Home Page
  Given I navigate to "DP2_Home_Page"
  And I accept cookies
  Then I scroll to end of the Page
  And Capture all the footer links to file "FooterLinks.csv"
  Then validate all the links are working
  And Validate No duplicate links are present
```

## 🛡️ Best Practices Implemented

### **Code Quality**
- **SOLID Principles**: Single responsibility, dependency injection
- **DRY Principle**: Reusable utility classes and methods
- **Page Object Pattern**: Maintainable and scalable page representations
- **Exception Handling**: Comprehensive error management
- **Resource Management**: Proper WebDriver cleanup

### **Test Design**
- **Independent Tests**: No test dependencies
- **Data-Driven Testing**: External test data files
- **Parameterized Execution**: Configurable test parameters
- **Assertion Strategies**: Multiple validation approaches
- **Test Documentation**: Clear BDD scenarios

### **Framework Architecture**
- **Modular Design**: Separate concerns and responsibilities
- **Configuration Management**: Centralized property files
- **Logging Integration**: Comprehensive test logging
- **Report Integration**: Multiple reporting formats
- **CI/CD Ready**: Headless and parallel execution support


## 📞 Support & Maintenance

### **Framework Maintenance**
- **Dependency Updates**: Regular Maven dependency updates
- **Browser Compatibility**: WebDriver version alignment
- **Test Data Management**: Regular test data validation
- **Report Cleanup**: Automated result cleanup scripts

### **Extending the Framework**
- **New Page Objects**: Follow BaseClass extension pattern
- **Additional Utilities**: Add to utilities package
- **New Test Modules**: Create separate Maven modules
- **Custom Reporters**: Implement TestNG/Cucumber listeners

## 📝 Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | Current | Initial framework implementation |
| - | - | Multi-module architecture |
| - | - | Allure reporting integration |
| - | - | Parallel execution support |

---

**Framework Developed By**: Sreekar Reddy  
**Last Updated**: 2025  
**Framework Type**: UI Automation - Java/Selenium/TestNG/Cucumber  
**License**: Internal Use Only