/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package QKART_SANITY_LOGIN.Module1;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.xml.xpath.XPath;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QkartSanity {

    public static String lastGeneratedUserName;


    public static RemoteWebDriver createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        return driver;
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION - Implement method using below steps
        /*
         * 1. Check if the folder "/screenshots" exists, create if it doesn't
         * 2. Generate a unique string using the timestamp
         * 3. Capture screenshot
         * 4. Save the screenshot inside the "/screenshots" folder using the following
         * naming convention: screenshot_<Timestamp>_<ScreenshotType>_<Description>.png
         * eg: screenshot_2022-03-05T06:59:46.015489_StartTestcase_Testcase01.png
         */
        try {
			File theDir = new File("/screenshots");
			if (!theDir.exists()) {
				theDir.mkdirs();
			}

			String timestamp = String.valueOf(java.time.LocalDateTime.now());
			String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);

			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			File DestFile = new File("screenshots/" + fileName);
			FileUtils.copyFile(SrcFile, DestFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    /*
     * Testcase01: Verify the functionality of Login button on the Home page
     */
    public static Boolean TestCase01(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase01");
        Boolean status;
        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");

            // Return False as the test case Fails
            takeScreenshot(driver, "TestCaseFail", "TestCase01");
            return false;
        } else {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "TestCaseFail", "TestCase01");
            return false;
        }

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();
        logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");

        takeScreenshot(driver, "EndTestCase", "TestCase01");
        return status;
    }

    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    public static Boolean TestCase02(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase02");
        Boolean status;
        logStatus("Start Testcase", "Test Case 2: Verify User Registration with an existing username ", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" : "FAIL");
            
            takeScreenshot(driver, "TestCaseFail", "TestCase02");
            return false;
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success

        logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");
        
        takeScreenshot(driver, "EndTestCase", "TestCase02");
        return !status;
    }

    /*
     * Verify the functinality of the search text box
     */
    public static Boolean TestCase03(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase03");

        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Thread.sleep(5000);

        // Search for the "yonex" product
        status = homePage.searchForProduct("yonex");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
            
            takeScreenshot(driver, "FailTestCase", "TestCase03");
            
            return false;
        }

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase 3", "Test Case Failure. There were no results for the given search string", "FAIL");
        
            takeScreenshot(driver, "FailTestCase", "TestCase03");
            
            return false;
        }

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");
                
                takeScreenshot(driver, "FailTestCase", "TestCase03");
                
                return false;
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");
        Thread.sleep(2000);

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
            
            takeScreenshot(driver, "FailTestCase", "TestCase03");
            
            return false;
        }

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        if (searchResults.size() == 0) {
            if (homePage.isNoResultFound()) {
                logStatus("Step Success", "Successfully validated that no products found message is displayed", "PASS");
            }
            logStatus("TestCase 3", "Test Case PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            logStatus("TestCase 3", "Test Case Fail. Expected: no results , actual: Results were available", "FAIL");
            takeScreenshot(driver, "FailTestCase", "TestCase03");
            return false;
        }
        takeScreenshot(driver, "EndTestCase", "TestCase03");
        return true;
    }

    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */
    public static Boolean TestCase04(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase04");
        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        Thread.sleep(5000);

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists(driver)) {
                logStatus("Step Success", "Successfully validated presence of Size Chart Link", "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                logStatus("Step Success", "Validated presence of drop down", status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart(driver)) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver)) {
                        logStatus("Step Success", "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure", "Failure while validating contents of Size Chart Link", "FAIL");
                    }

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
                    takeScreenshot(driver, "FailTestCase", "TestCase04");
                    return false;
                }

            } else {
                logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
                takeScreenshot(driver, "FailTestCase", "TestCase04");
                return false;
            }
        }
        logStatus("TestCase 4", "Test Case PASS. Validated Size Chart Details", "PASS");
        takeScreenshot(driver, "EndTestCase", "TestCase04");
        return status;
    }

    /*
     * Verify the complete flow of checking out and placing order for products is
     * working correctly
     */
    public static Boolean TestCase05(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase05");
        Boolean status;
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        Thread.sleep(5000);
        if (!status) {
            logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Thread.sleep(3000);
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        Thread.sleep(5000);
        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        Thread.sleep(3000);
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        Thread.sleep(3000);
        status = homePage.searchForProduct("Tan");
        Thread.sleep(3000);
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");
        Thread.sleep(3000);
        // Click on the checkout button
        homePage.clickCheckout();
        Thread.sleep(3000);

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        Thread.sleep(3000);
        // Place the order
        checkoutPage.placeOrder();
        // Thread.sleep(3000);
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.urlContains("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();
        Thread.sleep(3000);

        // Log out the user
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "EndTestCase", "TestCase05");
        return status;
    }

    /*
     * Verify the quantity of items in cart can be updated
     */
    public static Boolean TestCase06(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase06");
        Boolean status;
        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

        // TODO: Register a new user
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUserVishal", "vishal@123", true);
        Thread.sleep(5000);

        // TODO: Login using the newly registed user
        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "vishal@123");
        Thread.sleep(3000);

        // TODO: Add "Xtend Smart Watch" to cart
        status = homePage.searchForProduct("Xtend Smart Watch");
        Thread.sleep(3000);
        homePage.addProductToCart("Xtend Smart Watch");
        Thread.sleep(3000);

        // TODO: Add "Yarine Floor Lamp" to cart
        status = homePage.searchForProduct("Yarine Floor Lamp");
        Thread.sleep(3000);
        homePage.addProductToCart("Yarine Floor Lamp");
        Thread.sleep(3000);

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);
        Thread.sleep(3000);
        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);
        Thread.sleep(3000);
        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);
        Thread.sleep(3000);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        Thread.sleep(3000);
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "EndTestCase", "TestCase06");
        return status;
    }


    public static Boolean TestCase07(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase07");
        Boolean status;
        logStatus("Start TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        Thread.sleep(3000);
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
                    takeScreenshot(driver, "FailTestCase", "TestCase07");
            return false;
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
                    takeScreenshot(driver, "FailTestCase", "TestCase07");
            return false;
        }

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set");
        Thread.sleep(3000);

        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set", 10);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        Thread.sleep(3000);
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        Thread.sleep(3000);
        checkoutPage.placeOrder();
        Thread.sleep(2000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        // if (status) {
        //     System.out.println("You do not have enough balance in your wallet for this purchase");
        // }

        logStatus("End TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                status ? "PASS" : "FAIL");
        
        takeScreenshot(driver, "EndTestCase", "TestCase07");
        return status;
    }

    public static Boolean TestCase08(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "StartTestCase", "TestCase08");
        Boolean status = false;
        logStatus("Start TestCase", "Test Case 8: Another test case", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser1", "abc@1234", true);
        if (!status) {
            logStatus("TestCase 8", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 8: Verify user Registration : ", status ? "PASS" : "FAIL");

            // Return False as the test case Fails
            takeScreenshot(driver, "TestCaseFail", "TestCase08");
            return false;
        } else {
            logStatus("TestCase 8", "Test Case Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@1234");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 8: Verify user Registration : ", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "TestCaseFail", "TestCase08");
            takeScreenshot(driver, "TestCaseFail", "TestCase08");
            return false;
        }

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        homePage.searchForProduct("Yonex");
        Thread.sleep(3000);
        // Add product to cart
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        Thread.sleep(3000);
        // Open a new tab and navigate to home page
        driver.switchTo().newWindow(WindowType.TAB);
        Thread.sleep(3000);
        homePage.navigateToHome();
        // Check the contents of the cart
        status = false;
        List<WebElement> cartItems = driver.findElementsByXPath("//*[@id='root']/div/div/div[3]/div[2]/div/div/div/div[2]/div[1]");
        for (WebElement item : cartItems) {
            if (item.getText().contains("YONEX Smash Badminton Racquet")) {
                status = true;
            }
        }
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        return status;
    }

    public static Boolean TestCase09(RemoteWebDriver driver) throws InterruptedException {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        Boolean status = true;
        logStatus("Start TestCase",
                "Test Case 9: Verifying test case",
                "DONE");

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        //Click on the “Privacy Policy” page link
        WebElement privacyPolicy = driver.findElement(By.xpath("//*[@id='root']/div/div/div[5]/div[2]/p[1]/a"));
        privacyPolicy.click();
        Thread.sleep(3000);

        // Verify that the url of the current tab does not change
        String homeUrl = driver.getCurrentUrl();
        if (!homeUrl.equals("https://crio-qkart-frontend-qa.vercel.app/")) {
            logStatus("TestCase 9", "Verify that the url of the current tab does not change", "FAIL");
            return false;
        }

        // Switch to the newly opened tab
        String homeWindowHandle = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        for (String handle : windowHandles) {
            if (!handle.equals(homeWindowHandle)) {
                driver.switchTo().window(handle);
                Thread.sleep(3000);
            }
        }

        // Check if the privacy policy content is available on the newly opened tab
        WebElement privacyContent = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/p"));
        if (privacyContent.getText().equals("")) {
            logStatus("TestCase 9", "Privacy Policy content is not available", "FAIL");
            return false;
        }

        driver.close();
        // Move back to the original tab (with QKart home page opened)
        driver.switchTo().window(homeWindowHandle);
        
        // Click on the “Terms Of Service” page link in the footer
        WebElement termsOfService = driver.findElement(By.xpath("//*[@id='root']/div/div/div[5]/div[2]/p[4]/a"));
        termsOfService.click();
        Thread.sleep(3000);

        // Verify that the url of the current tab does not change
        String homeUrl2 = driver.getCurrentUrl();
        if (!homeUrl2.equals("https://crio-qkart-frontend-qa.vercel.app/")) {
            logStatus("TestCase 9", "Verify that the url of the current tab does not change", "FAIL");
            return false;
        }

        // Switch to the newly opened tab
        String homeWindowHandle2 = driver.getWindowHandle();
        Set<String> windowHandles2 = driver.getWindowHandles();
        for (String handle : windowHandles2) {
            if (!handle.equals(homeWindowHandle2)) {
                driver.switchTo().window(handle);
                Thread.sleep(3000);
            }
        }

        // Check if the about us content is displayed on the newly opened tab
        WebElement termsContent = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/p"));
        if (termsContent.getText().equals("")) {
            logStatus("TestCase 9", "Terms Of Service content is not available", "FAIL");
            return false;
        }

        // Close all the newly opened tabs and switch focus back to the original tab
        driver.close();
        driver.switchTo().window(homeWindowHandle2);

        logStatus("End TestCase", "Test Case 9: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        return status;
    }

    public static Boolean TestCase10(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = true;
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        logStatus("Start TestCase",
                "Test Case 10: Verifying test case",
                "DONE");

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        WebElement contactUs = driver.findElement(By.xpath("//*[@id='root']/div/div/div[5]/div[2]/p[3]"));
        contactUs.click();
        Thread.sleep(2000);

        WebElement name = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[2]/div[1]/div/input"));
        WebElement email = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[2]/div[2]/div/input"));
        WebElement message = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[3]/input"));
        
        name.sendKeys("crio user");
        email.sendKeys("criouser@gmail.com");
        message.sendKeys("Testing the contact us page");

        WebElement contactNow = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));
        contactNow.click();
        Thread.sleep(3000);

        boolean present;
        try {
            driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div"));
            status = false;
            present = true;
        } catch (Exception e) {
            present = false;
            status = true;
        }
        
        logStatus("End TestCase", "Contact Dialog box is being displayed : ", present ? "YES" : "NO");
    
        logStatus("End TestCase", "Test Case 10: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        return status;
    }

    public static Boolean TestCase11(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = false;
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser3", "abc@12345", true);
        if (!status) {
            logStatus("TestCase 11", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 11: Verify user Registration : ", status ? "PASS" : "FAIL");

            // Return False as the test case Fails
            takeScreenshot(driver, "TestCaseFail", "TestCase11");
            return false;
        } else {
            logStatus("TestCase 11", "Test Case Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@12345");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "TestCaseFail", "TestCase01");
            return false;
        }

        // Visit the home page and log out the logged in user
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        Thread.sleep(5000);
        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        Thread.sleep(3000);
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        Thread.sleep(3000);
        // Click on the checkout button
        homePage.clickCheckout();
        Thread.sleep(3000);

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        Thread.sleep(3000);
        // Place the order
        checkoutPage.placeOrder();
        // Thread.sleep(3000);
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.urlContains("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        List<WebElement> adv = driver.findElements(By.tagName("iframe"));
        int count = adv.size();
        if (count==3) {
            logStatus("TestCase 11", "3 Advertisements are present", "PASS");
        }
        else {
            logStatus("EndTestCase 11", "3 Advertisements are not present", "FAIL");
            return false;
        }

        int i = 1;
        for (WebElement ad : adv) {
            if (i<3) {
                driver.switchTo().frame(ad);
                WebElement buy = driver.findElement(By.xpath("//button[text()='Buy Now']"));
                if (!buy.isEnabled()) {
                    logStatus("EndTestCase 11", "Advertisement buttons are not clickable", "FAIL");
                    return false;
                }
                driver.switchTo().parentFrame();
            }
            i++;
        }
        
        logStatus("EndTestCase 11", "Advertisement buttons are clickable", "PASS");
        logStatus("EndTestCase 11", "Test Case 11: Happy Flow Test Completed : ", "PASS");
        return status;
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        int totalTests = 0;
        int passedTests = 0;
        Boolean status;
        RemoteWebDriver driver = createDriver();
        // Maximize and Implicit Wait for things to initailize
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        try {
            // Execute Test Case 1
            totalTests += 1;
            status = TestCase01(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 2
            totalTests += 1;
            status = TestCase02(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 3
            totalTests += 1;
            status = TestCase03(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 4
            totalTests += 1;
            status = TestCase04(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 5
            totalTests += 1;
            status = TestCase05(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 6
            totalTests += 1;
            status = TestCase06(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 7
            totalTests += 1;
            status = TestCase07(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 8
            totalTests += 1;
            status = TestCase08(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 9
            totalTests += 1;
            status = TestCase09(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 10
            totalTests += 1;
            status = TestCase10(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

            // Execute Test Case 11
            totalTests += 1;
            status = TestCase11(driver);
            if (status) {
            passedTests += 1;
            }

            System.out.println("");

        } catch (Exception e) {
            throw e;
        } finally {
            // quit Chrome Driver
            driver.quit();

            System.out.println(String.format("%s out of %s test cases Passed ", Integer.toString(passedTests),
                    Integer.toString(totalTests)));
        }

    }
}