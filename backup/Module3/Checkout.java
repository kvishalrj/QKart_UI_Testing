package QKART_SANITY_LOGIN.Module1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            WebElement addNewAddressButton = this.driver.findElement(By.xpath("//*[@id='add-new-btn']"));
            addNewAddressButton.click();
            Thread.sleep(3000);
            WebElement addNewAddress = this.driver.findElementByXPath("//*[@id='root']/div/div[2]/div[1]/div/div[2]/div[1]/div/textarea[1]");
            addNewAddress.sendKeys(addresString);
            WebElement add = this.driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div[1]/div/div[2]/div[2]/button[1]"));
            add.click();
            Thread.sleep(3000);
            return true;

        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
            int i = 1;
            List<WebElement> selectAddress = this.driver.findElements(By.xpath("//*[@id='root']/div/div[2]/div[1]/div/div[1]/div/div[1]/p"));
            for (WebElement address : selectAddress) {
                if (address.getText().contains(addressToSelect)) {
                    WebElement selectAdd = this.driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div[1]/div/div[1]/div["+i+"]/div[1]/span/input"));
                    selectAdd.click();
                    return true;
                }
                i++;
            }
            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it
            WebElement placeOrderButton = this.driver.findElementByXPath("//*[@id='root']/div/div[2]/div[1]/div/button[2]");
            placeOrderButton.click();
            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
         // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6
        WebElement alertBalance = driver.findElementByXPath("//*[@id='notistack-snackbar']");
        if (alertBalance.isDisplayed()) {
            if (alertBalance.getText().contains("You do not have enough balance in your wallet for this purchase")) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
        
    }
}
