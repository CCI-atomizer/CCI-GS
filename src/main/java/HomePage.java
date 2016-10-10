import com.sun.jna.platform.win32.OaIdl;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class HomePage extends LoadableComponent<HomePage> {

    private static WebDriver driver;
    private static WebDriverWait wait;
    final String URL = "https://www.cineworld.co.uk/gifts#/";
    public static int basketCount;
    public static BigDecimal toPay;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
        basketCount = 0;
        toPay = new BigDecimal("0");
    }

    public enum Category {

        GIFTCARDS("//img[@src='https://giftshop.cineworld.co.uk/cmsstatic/category/-10/giftcard-fan.png']"),
        VOUCHERS("//img[@src='https://giftshop.cineworld.co.uk/cmsstatic/category/-20/egift.png']"),
        GIFTBOX("//img[@src='https://giftshop.cineworld.co.uk/cmsstatic/category/-40/unlimitedgift_140616.png']"),
        EGIFT("//img[@src='https://giftshop.cineworld.co.uk/cmsstatic/product/-301/Unlimited e-gift 208.png']");

        private final String src;

        private Category(String src) {
            this.src = src;
        }

        public String path(){
            return src;
        }
    }

    //Goes to chosen product category
    public void toCategory(Category category){
        //Choose item by clicking on of four gifts categories picture
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(category.path()))).click();
    }

    //Checks if basket is empty
    public static boolean basketIsEmpty() {
        //returns True if no button is displayed, False when <Buy> button is active
        try {
            new WebDriverWait(driver, 1).until(ExpectedConditions.elementToBeClickable(By.id("buy-products")));
        } catch (TimeoutException e) {
            return true;
        }
        return false;
    }

    //Removes products untill basket is empty
    public static void clearBasket() {
        while (!basketIsEmpty()){
            clearProduct(1);
        }
    }

    //Deletes chosen product from basket list
    public static void clearProduct(int product) {
        try {
            WebElement cross = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("html/body/div/ui-view/div/section[2]/div[1]/div/section[1]/div/div[2]/div[1]/div["
                            + product + "]/div[1]/div[3]/img")));
            cross.click();
            try {
                basketCount--;
                toPay = toPay.subtract(getPriceOf(product));
                System.out.println("Items in basket: " + basketCount);
                System.out.println("Total price: " + toPay);
                new WebDriverWait(driver, 1).until(ExpectedConditions.invisibilityOfElementLocated(By.id("buy-products")));
            } catch (TimeoutException e) {
                //resolves synchronisation issues, ensures cross button is clicked only one time in clearBasket()
            }

        } catch (NoSuchElementException e) {
            System.out.println("Nothing to remove");
        }
    }

    //Deletes random product from basket list
    public static void clearRandom(int randomMax) {
        Random rand = new Random();
        int toDelete = rand.nextInt((randomMax - 1) + 1) + 1;
        clearProduct(toDelete);
    }

    //Returns total price shown on HomePage or OrderReviewPage
    public static BigDecimal totalPrice(){
        if (driver.getCurrentUrl().contentEquals(ReviewOrderPage.URL)){
            WebElement totalPrice = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "html/body/div/ui-view/div/section[2]/div[1]/div/div/section/div["
            + (HomePage.basketCount+1) +"]/div[1]/div/p/span")));
            String total = totalPrice.getText().substring(1).replace(",", "");
            return new BigDecimal(total);

        }
        WebElement totalPrice = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                "html/body/div/ui-view/div/section[2]/div[1]/div/section[1]/div/div[2]/div[2]/div[1]/p/span[2]")));
        String total = totalPrice.getText().substring(1).replace(",", "");
        return new BigDecimal(total);
    }

    //Get price of chosen product(s) from basket, e.g. getPriceOf(2) returns price of second product in basket list
    public static BigDecimal getPriceOf(int productNumber){
        WebElement productsField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                "html/body/div/ui-view/div/section[2]/div[1]/div/section[1]/div/div[2]/div[1]/div["
                        + productNumber +"]/div[1]/div[2]/p/span[1]")));

        String field1 = productsField.getText();
        String productQuantityString = field1.substring(0, 1);
        String productPriceString = field1.substring(5, field1.length()-1);
        BigDecimal productQuantity = new BigDecimal(productQuantityString);
        BigDecimal productPrice = new BigDecimal(productPriceString);

        try {
            WebElement deliveryField = driver.findElement(By.xpath(
                    "html/body/div/ui-view/div/section[2]/div[1]/div/section[1]/div/div[2]/div[1]/div["
                            + productNumber + "]/div[1]/div[2]/p/span[2]"));
            String field2 = deliveryField.getText();
            String productDeliveryString = field2.substring(3, field2.length()-9);
            BigDecimal productDelivery = new BigDecimal(productDeliveryString);
            return (productQuantity.multiply(productPrice)).add(productDelivery);
        } catch (NoSuchElementException e){
            return productQuantity.multiply(productPrice);
        }
    }

    //Goes to buy section if there is any item in the basket
    public void toBuySection(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='buy-products']"))).click();
        } catch(TimeoutException e) {
            System.out.println("No items in basket");
        }
    }

    protected void load() {
        driver.get(URL);
    }

    protected void isLoaded() throws Error {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/ui-view/div/section[2]/div[2]/div/div/div[3]/div/p/strong/a")));
        } catch (TimeoutException e) {
            throw new Error("page was not loaded");
        }
    }
}
