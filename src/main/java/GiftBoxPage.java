import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.util.Random;

public class GiftBoxPage extends LoadableComponent<HomePage> {

    private WebDriver driver;
    private final String URL = "https://www.cineworld.co.uk/shop/#/shop/categories/unlimited";
    private WebDriverWait wait;

    public final static String basketInfo = "Unlimited Gift Box";

    public GiftBoxPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    //Adds product with default values
    public void addDefaultUnlimitedBox() {
        //Wait and click "deliver to my billing address"
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#billing-address-delivery-checkbox"))).click();

        //Wait and click delivery method 1st radio button
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gift-delivery-method-0"))).click();

        addProduct();
    }

    //Adds product with random values
    public void addRandomGiftBox() {
        Random rand = new Random();
        int quantity = rand.nextInt((5 - 1) + 1) + 1;
        int delivery = rand.nextInt((1 - 0) + 1) + 0;

        //Choose random product quantity
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='gift-quantity-select']/option["
                + quantity +"]"))).click();

        //Wait and click "deliver to my billing address"
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#billing-address-delivery-checkbox"))).click();

        //Choose random delivery method
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gift-delivery-method-" + delivery))).click();

        addProduct();
    }

    public void addProduct() {
        //Click <Add to order> and add item to basket
        driver.findElement(By.cssSelector("#product-add-item")).click();
        HomePage.basketCount++;

        //Wait for homepage and elements used for assertion to load, add price of added product to BigDecimal controlling total price
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("html/body/div/ui-view/div/section[2]/div[1]/div/section[1]/div/div[2]/div[1]/div["
                + HomePage.basketCount + "]/div[1]/div[2]/p")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buy-products")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buy-products")));
        HomePage.toPay = HomePage.toPay.add(HomePage.getPriceOf(1));
        System.out.println("Added giftBox with price: " + HomePage.getPriceOf(1));
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
