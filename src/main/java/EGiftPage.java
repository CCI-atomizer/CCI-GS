import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.util.Random;

public class EGiftPage extends LoadableComponent<EGiftPage>{

    private WebDriver driver;
    private final String URL = "https://www.cineworld.co.uk/shop/#/shop/categories/ULegift";
    private WebDriverWait wait;

    private String forename = "forenameABC";
    private String email = "asdf@wp.pl";
    private String comment = "qwertyuiop";
    public final static String basketInfo = "Unlimited e-Gift";

    public EGiftPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    //Adds product with default values
    public void addDefaultEGift() {
        fillPersonalDetails();
        addProduct();
    }

    //Adds product with random values
    public void addRandomEGift() {
        Random rand = new Random();
        int quantity = rand.nextInt((5 - 1) + 1) + 1;

        //Choose random product quantity
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='gift-quantity-select']/option["
                + quantity +"]"))).click();

        fillPersonalDetails();
        addProduct();
    }

    public void fillPersonalDetails() {
        //Wait for field and enter forename; second wait after first click to prevent occasional "Element is no longer attached to the DOM"
        wait.until(ExpectedConditions.elementToBeClickable(By.id("products.addressForm.address.firstName"))).click();
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
        System.out.println("Added eGift with price: " + HomePage.getPriceOf(1));
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
