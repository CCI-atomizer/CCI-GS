import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

public class BuyPage extends LoadableComponent<BuyPage>{

    private WebDriver driver;
    public static final String URL = "https://www.cineworld.co.uk/shop/#/shop/details";
    private WebDriverWait wait;

    private String forename = "forenameABC";
    private String surname = "surnameABC";
    private String email = "asdf@wp.pl";
    private String phone = "794823745";
    private String address1 = "Lilly St.";
    private String address2 = "3rd dpt";
    private String town = "London";
    private String postcode = "EC1Y 8SY";

    public String forenameError = "";
    public String surnameError = "";
    public String emailError = "";
    public String phoneError = "";
    public String address1Error = "";
    public String address2Error = "";
    public String townError = "";
    public String postcodeError = "";

    public BuyPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    //submit empty fields
    public void emptyFieldsSubmit(){
        clearFields();
        clearFields(); //double clearFields() to resolve synchronisation issues
        submit();
        getErrors();
    }

    //submit valid string to each field
    public void validValuesSubmit() {
        //Wait for field and enter forename; second wait after first click to prevent occasional "DOM element not found"
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).sendKeys(forename);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.lastName"))).sendKeys(surname);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.email"))).sendKeys(email);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.phone"))).sendKeys(phone);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.addressLine1"))).sendKeys(address1);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.addressLine2"))).sendKeys(address2);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.city"))).sendKeys(town);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.postalCode"))).sendKeys(postcode);
        submit();
    }

    //submit random string consisting of one invalid char to each field
    public void invalidCharsSubmit() {
        clearFields();
        //Wait for field and enter forename; second wait after first click to prevent occasional "DOM element not found"
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.lastName"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.email"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.phone"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.addressLine1"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.addressLine2"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.city"))).sendKeys(randomInvalid());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.postalCode"))).sendKeys(randomInvalid());
        submit();
        getErrors();
    }

    //submit 0 to 'name', 'surname' and 'postcode' fields
    public void zeroNamePostcodeSubmit(){
        clearFields();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).sendKeys("0");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.lastName"))).sendKeys("0");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.postalCode"))).sendKeys("0");
        submit();
        getErrors();
    }

    //submit 0 to 'email' field
    public void zeroEmailSubmit(){
        clearFields();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.email"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.email"))).sendKeys("0");
        submit();
        getErrors();
    }

    //Clears previous text from fields
    private void clearFields(){
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.firstName"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.lastName"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.email"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.phone"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.addressLine1"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.addressLine2"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.city"))).clear();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billing.billingForm.addresses.billing.postalCode"))).clear();
    }

    //Click submit button
    private void submit(){
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#billing-confirm"))).click();
    }

    //Collect errors under fields and assign them to values
    private void getErrors(){
        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gift-billing-forename']/div[2]/p")));
        } catch (TimeoutException e) {/* Wait for errors to load */}

        try {
            forenameError = driver.findElement(By.xpath(".//*[@id='gift-billing-forename']/div[2]/p")).getText();
        } catch (NoSuchElementException e){ forenameError = "No error"; }
        try {
            surnameError = driver.findElement(By.xpath(".//*[@id='gift-billing-surname']/div[2]/p")).getText();
        } catch (NoSuchElementException e){ surnameError = "No error"; }
        try {
            emailError = driver.findElement(By.xpath(".//*[@id='gift-billing-email']/div[2]/p")).getText();
        } catch (NoSuchElementException e){ emailError = "No error"; }
        try {
            phoneError = driver.findElement(By.xpath(".//*[@id='gift-billing-phone']/div[2]/p")).getText();
        } catch (NoSuchElementException e){ phoneError = "No error"; }
        try {
            address1Error = driver.findElement(By.xpath(".//*[@id='gift-billing-adress']/div[2]/p")).getText();
        } catch (NoSuchElementException e){ townError = "No error"; }
        try {
            townError = driver.findElement(By.xpath(".//*[@id='gift-billing-town']/div[2]/p")).getText();
        } catch (NoSuchElementException e){ townError = "No error"; }
        try {
            postcodeError = driver.findElement(By.xpath(".//*[@id='gift-billing-postcode']/div[2]/p")).getText();
        } catch (NoSuchElementException e) { postcodeError = "No error"; }
    }

    //Generates random 5-char string consisting of four valid chars and one invalid;
    private String randomInvalid(){
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String invalidChars = "!\"£$%^&*()_+ó";
        Random rand = new Random();
        String result = "";
        for (int i = 0; i <=4; i++){
            int rndIndex = rand.nextInt(validChars.length());
            char rndChar = validChars.charAt(rndIndex);
            result += rndChar;
        }
        int rndIndex = rand.nextInt(invalidChars.length());
        char rndInvalidChar = invalidChars.charAt(rndIndex);
        result+=rndInvalidChar;
        return result;
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