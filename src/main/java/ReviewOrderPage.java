import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ReviewOrderPage extends LoadableComponent<ReviewOrderPage>{

    private WebDriver driver;
    private WebDriverWait wait;
    public static final String URL ="https://www.cineworld.co.uk/shop/#/shop/review";

    public ReviewOrderPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    //Deletes chosen product from order-summary page
    public void clearProduct(int product){
            try {
                WebElement cross = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("html/body/div/ui-view/div/section[2]/div[1]/div/div/section/div["
                                + product +"]/div[1]/div[3]/img")));
                HomePage.basketCount--;
                HomePage.toPay = HomePage.toPay.subtract(getPriceOf(product));
                cross.click();
                System.out.println("Items in basket: " + HomePage.basketCount);
                System.out.println("Total price: " + HomePage.toPay);
                new WebDriverWait(driver, 2).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                        "html/body/div/ui-view/div/section[2]/div[1]/div/div/section/div[3]/div[1]/div/p/span")));
                return;
            } catch (NoSuchElementException e) {
                System.out.println("Nothing to remove");
                return;
            }

    }

    //return price of chosen product from order-summary page
    public BigDecimal getPriceOf(int productNumber){
            WebElement productsField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "html/body/div/ui-view/div/section[2]/div[1]/div/div/section/div["
                           + productNumber + "]/div[1]/div[2]/div/div[2]/p")));
            String field1 = productsField.getText();
            String productQuantityString = field1.substring(0, 1);
            String productPriceString = field1.substring(5, field1.length()-1);
        BigDecimal productQuantity = new BigDecimal(productQuantityString);
        BigDecimal productPrice = new BigDecimal(productPriceString);
        System.out.println(productQuantity);
        System.out.println(productPrice);
            try {
                WebElement deliveryField = driver.findElement(By.xpath(
                        "html/body/div/ui-view/div/section[2]/div[1]/div/div/section/div["
                                + productNumber +"]/div[2]/div/div/p[3]"));
                String field2 = deliveryField.getText();
                if (field2.contains("Special Delivery")){
                    String productDeliveryString = field2.substring(35, field2.length()-1);
                    BigDecimal productDelivery = new BigDecimal(productDeliveryString);
                    System.out.println(productDelivery);
                    return (productQuantity.multiply(productPrice)).add(productDelivery);
                }
                String productDeliveryString = field2.substring(36, field2.length()-1);
                BigDecimal productDelivery = new BigDecimal(productDeliveryString);
                System.out.println(productDelivery);
                return (productQuantity.multiply(productPrice)).add(productDelivery);
            } catch (NoSuchElementException e){
                return productQuantity.multiply(productPrice);
            }
        }

    protected void load() {
        driver.get(URL);
    }

    protected void isLoaded() throws Error {
        if(!driver.getTitle().contentEquals("Latest Movies - New Films - 3D Movies | Cineworld Cinemas")) {
            throw new Error("page was not loaded");
        }
    }

}
