# QKart UI Testing

## Overview
**QKart** is an e-commerce platform, and the purpose of this project is to design and automate test cases for the application using the **TestNG** framework.

### Project Highlights:
- Debugged and fixed **failing test cases** and improved existing test coverage for critical functionalities like **Register**, **Login**, **Product Search**, and **Cart Management**.
- Automated manual test cases using **Selenium** with proper synchronization via **implicit and explicit waits**.
- Enhanced locators with **dynamic XPath**, improving the reliability of the test scripts.
- Migrated tests to the **TestNG** framework, enabling features like **test grouping**, **prioritization**, and **parameterization**.
- Implemented **data-driven testing** using **Apache POI**, ensuring robustness with varied test inputs.
- Modularized and maintained the test code for better readability and reduced duplication.
- Captured screenshots before and after each test and upon encountering errors to aid in debugging.

## Tools and Technologies Used:
- **Java**
- **Selenium WebDriver**
- **TestNG**
- **Apache POI**
- **XPath**
- **Developer Tools**
- **HTML**

## Project Structure:
The project is organized into the following modules:

1. **Page Objects**:
   - Encapsulates web elements and their respective methods for different QKart pages like Registration, Login, Products, and Cart.

2. **Test Cases**:
   - Designed following the **Page Object Model (POM)** with reusable methods for operations like searching, adding to the cart, and session management.

3. **TestNG Configurations**:
   - TestNG XML files manage test suites, parameterization, and test grouping (e.g., **Sanity** and **Regression**).

4. **Synchronization Enhancements**:
   - Improved test resilience by placing appropriate **implicit and explicit waits** for better synchronization between test scripts and the UI.

5. **Reports**:
   - **TestNG Listeners** generate detailed logs and screenshots for passed and failed tests.

## Features Implemented:
- **Data-Driven Testing**:
  - Automated tests dynamically utilize data from Excel files via **Apache POI**.
- **Modularization**:
  - Modular test code enhances reusability and reduces redundancy.
- **Enhanced Locators**:
  - Dynamic XPath for robust and reliable element selection.
- **Screenshots**:
  - Screenshots are captured before and after test execution and upon failures.
- **Cross-Browser Validations**:
  - Automation validations span multiple tabs and browser windows.

## How to Run:
1. Clone the repository:
   ```bash
   git clone https://github.com/kvishalrj/QKart_UI_Testing.git
   ```
2. Import the project into your preferred IDE (IntelliJ IDEA, Eclipse, etc.).
3. Ensure **Java**, **Selenium**, and required dependencies are installed.
4. Execute the test suite via `TestNG.xml` or using Maven/Gradle.

### Running Tests:
#### Using Maven:
```bash
mvn test
```

#### Using Gradle:
```bash
gradle test
```

## Reporting:
After the test run, detailed reports are generated:
- **Screenshots**: Located in the `screenshots/` directory.
- **Test Reports**: Found in the `reports/` directory, providing detailed insights into the test execution.

## Future Scope:
- Expand test coverage for advanced features like payment gateways and user reviews.
- Integrate with CI/CD pipelines for continuous testing.
- Enhance reporting with tools like **Extent Reports**.