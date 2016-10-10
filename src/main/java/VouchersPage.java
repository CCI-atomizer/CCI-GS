import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.util.Random;

public class VouchersPage extends LoadableComponent {

    private WebDriver driver;
    private final String URL = "https://www.cineworld.co.uk/shop/#/shop/categories/egift";
    private WebDriverWait wait;

    private String forename = "forenameABC";
    private String email = "asdf@wp.pl";
    private String comment = "qwertyuiop";
    public final static String basketInfo = "e-Gift Voucher";
    public static DecimalFormat df = new DecimalFormat("#.##");

    public VouchersPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    //Adds product with default values
    public void addDefaultVoucher() {
        fillPersonalDetails();
        addProduct();
    }

    //Adds product with random values
    public void addRandomVoucher() {
        Random rand = new Random();
        int value = rand.nextInt((19 - 1) + 1) + 1;
        int quantity = rand.nextInt((5 - 1) + 1) + 1;

        //Choose random value of product and product quantity
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='price-select']/option["
                + value +"]")));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e){
            //100ms to prevent"Element is no longer attached to the DOM"
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='price-select']/option["
                + value +"]"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='gift-quantity-select']/option["
                + quantity +"]"))).click();

        fillPersonalDetails();
        addProduct();
    }

    WebElement getStaleElem(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        } catch (StaleElementReferenceException e) {
            System.out.println("Attempting to recover from StaleElementReferenceException ...");
            return getStaleElem(by, driver);
        } catch (NoSuchElementException ele) {
            System.out.println("Attempting to recover from NoSuchElementException ...");
            return getStaleElem(by, driver);
        }
    }
    public void fillPersonalDetails(){

        //Wait for field and enter forename; second wait after first click to prevent occasional "Element is no longer attached to the DOM"
        wait.until(ExpectedConditions.elementToBeClickable(By.id("products.addressForm.address.firstName")));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e){
            //100ms to prevent"Element is no longer attached to the DOM"
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.id("products.addressForm.address.firstName"))).sendKeys(forename);


        //Wait for field and enter e-mail
        wait.until(ExpectedConditions.elementToBeClickable(By.id("products.addressForm.address.email"))).sendKeys(email);

        //Wait for field and enter comment
        wait.until(ExpectedConditions.elementToBeClickable(By.id("products.options.personalMessage"))).sendKeys(comment);

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
        System.out.println("Added voucher with price: " + HomePage.getPriceOf(1));
    }

    protected void load() {
        driver.get(URL);
    }

    protected void isLoaded() throws Error {
        try {
            WebElement imgPreview = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".img-preview")));
        } catch (TimeoutException e) {
            throw new Error("page was not loaded");
        }
    }
}
