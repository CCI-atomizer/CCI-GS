import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

///////////////////////////////////////////
//               BETA TESTS              //
//      TO BE RUN ON FIREFOX 45.4.0      //
//                  !!!                  //
///////////////////////////////////////////

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirstChapter {

    private static WebDriver driver;
    private static HomePage homePage;
    private static GiftsCardsPage giftsCardsPage;
    private static VouchersPage vouchersPage;
    private static GiftBoxPage giftBoxPage;
    private static EGiftPage eGiftPage;
    private static BuyPage buyPage;
    private static ReviewOrderPage reviewOrderPage;


    @BeforeClass
    public static void setupTest(){
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        homePage.get();
        giftsCardsPage = new GiftsCardsPage(driver);
        vouchersPage = new VouchersPage(driver);
        giftBoxPage = new GiftBoxPage(driver);
        eGiftPage = new EGiftPage(driver);
        buyPage = new BuyPage(driver);
        reviewOrderPage = new ReviewOrderPage(driver);
    }

    @AfterClass
    public static void closeTest(){
        driver.close();
    }


    //1. ADD GIFT TO THE BASKET

    //Checks whether basket is empty
    @Test
    public void test001_EmptyBasket(){
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }

    //Next 3 tests checks the multi-adding functionality of gift cards with defualt values
    @Test
    public void test002_DefaultGiftCardAddAmazed(){
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addDefaultCard(GiftsCardsPage.GiftCard.AMAZED);
        boolean cardAdded = (driver.getPageSource().contains(GiftsCardsPage.GiftCard.AMAZED.basketInfo())); //checks if added gift card is in homepage source code
        assertTrue("Expected " + GiftsCardsPage.GiftCard.AMAZED.basketInfo() + " in source code", cardAdded);
    }

    @Test
    public void test004_DefaultGiftCardAddShaken() {
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addDefaultCard(GiftsCardsPage.GiftCard.SHAKEN);
        boolean cardAdded = (driver.getPageSource().contains(GiftsCardsPage.GiftCard.SHAKEN.basketInfo()));
        assertTrue("Expected " + GiftsCardsPage.GiftCard.SHAKEN.basketInfo() + " in source code", cardAdded);
    }


    @Test
    public void test006_DefaultGiftCardAddThrilled() {
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addDefaultCard(GiftsCardsPage.GiftCard.THRILLED);
        boolean cardAdded = (driver.getPageSource().contains(GiftsCardsPage.GiftCard.THRILLED.basketInfo()));
        assertTrue("Expected " + GiftsCardsPage.GiftCard.THRILLED.basketInfo() + " in source code", cardAdded);
    }

    //Clears basket for next test
    @Test
    public void test007_EmptyBasket() {
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }

    //Checks single-card with default values adding functionality
    @Test
    public void test008_DefaultGiftCardAddExcited() {
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addDefaultCard(GiftsCardsPage.GiftCard.EXCITED);
        boolean cardAdded = (driver.getPageSource().contains(GiftsCardsPage.GiftCard.EXCITED.basketInfo()));
        assertTrue("Expected " + GiftsCardsPage.GiftCard.EXCITED.basketInfo() + " in source code", cardAdded);
    }

    //Clears basket for next test
    @Test
    public void test009_EmptyBasket() {
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }

    //Checks single-eVoucher with default values adding functionality
    @Test
    public void test010_DefaultEVoucherAdd(){
        homePage.toCategory(HomePage.Category.VOUCHERS);
        vouchersPage.addDefaultVoucher();
        boolean voucherAdded = (driver.getPageSource().contains(vouchersPage.basketInfo));
        assertTrue("Expected " + vouchersPage.basketInfo + " in source code", voucherAdded);
    }

    //Clears basket for next test
    @Test
    public void test011_EmptyBasket() {
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }

    //Checks single-UnlimitedBox with default values adding functionality
    @Test
    public void test012_DefaultUnlimitedBoxAdd(){
        homePage.toCategory(HomePage.Category.GIFTBOX);
        giftBoxPage.addDefaultUnlimitedBox();
        boolean giftBoxAdded = (driver.getPageSource().contains(giftBoxPage.basketInfo));
        assertTrue("Expected " + giftBoxPage.basketInfo + " in source code", giftBoxAdded);
    }

    //Clears basket for next test
    @Test
    public void test013_EmptyBasket() {
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }

    //Checks single-UnlimitedEGift with default values adding functionality
    @Test
    public void test014_DefaultUnlimitedEGiftAdd() {
        homePage.toCategory(HomePage.Category.EGIFT);
        eGiftPage.addDefaultEGift();
        boolean eGiftAdded = (driver.getPageSource().contains(eGiftPage.basketInfo));
        assertTrue("Expected " + eGiftPage.basketInfo + " in source code", eGiftAdded);
    }

    //Clears basket for next tests section
    @Test
    public void test015_EmptyBasket() {
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }


    //2.REMOVE GIFTS FROM BASKET

    //Adds 4 products with random values - 1 of each category - to be used is later tests regarding products removal and price verification (section 2)
    @Test
    public void test016_AddRandomOfEachCategory() {
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addRandomCard(GiftsCardsPage.GiftCard.AMAZED);
        homePage.toCategory(HomePage.Category.VOUCHERS);
        vouchersPage.addRandomVoucher();
        homePage.toCategory(HomePage.Category.GIFTBOX);
        giftBoxPage.addRandomGiftBox();
        homePage.toCategory(HomePage.Category.EGIFT);
        eGiftPage.addRandomEGift();
        System.out.println("Expected/shown on page: " + HomePage.totalPrice()
                + ", actual: " + HomePage.toPay);
        assertEquals(HomePage.totalPrice(), HomePage.toPay); // HomePage.totalPrice() - shows total price displayed on current webpage
                                                            // HomePage.toPay - BigDecimal variable storing actual basket value (updated on every item add/removal)
    }

    //Removes random product from available four and verifies the price is correct after product removal
    @Test
    public void test017_RemoveRandomItem_VerifyPrice(){
        HomePage.clearRandom(4); //deletes one random item out of four
        assertEquals("Expected: " + HomePage.totalPrice()
                + ", actual: " + HomePage.toPay, HomePage.totalPrice(), HomePage.toPay);
    }

    //Goes to buy section, enters valid values and proceeds to payment, deletes first product from the list and verifies price is correct after product removal
    @Test
    public void test018_GoToBuySection_RemoveFirstItem_VerifyPrice(){
        homePage.toBuySection();
        buyPage.validValuesSubmit();
        reviewOrderPage.clearProduct(1);
        assertEquals("Expected: " + HomePage.totalPrice()
                + ", actual: " + HomePage.toPay, HomePage.totalPrice(), HomePage.toPay);
    }

    //3 ADDRESS DETAILS - NEGATIVES

    //Loads homepage and clears basket for section 3 tests (test'll be optimised to run faster later)
    @Test
    public void test097_GetHomepage_ClearBasket() {
        homePage.get();
        HomePage.clearBasket();
        assertTrue("Expected empty basket", homePage.basketIsEmpty());
    }

    //Adds 2 random gift cards for future tests
    @Test
    public void test098_AddTwoRandomGiftcards(){
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addRandomCard(GiftsCardsPage.GiftCard.AMAZED);
        homePage.toCategory(HomePage.Category.GIFTCARDS);
        giftsCardsPage.addRandomCard(GiftsCardsPage.GiftCard.EXCITED);
    }

    //Goes to payment section, submit empty fields and checks error for fields FORENAME, SURNAME, EMAIL, PHONE, ADDRESS1, ADDRESS2, TOWN, POSTCODE (does NOT include ADDRESS2 field),
    //checks for error information
    @Test
    public void test099_VerifyMandatoryFieldsEmptySubmitErrors(){
        homePage.toBuySection();
        buyPage.emptyFieldsSubmit();
        String expectedError = "This field is required.";
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.forenameError, buyPage.forenameError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.surnameError, buyPage.surnameError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.emailError, buyPage.emailError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.phoneError, buyPage.phoneError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.address1Error, buyPage.address1Error.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.townError, buyPage.townError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.postcodeError, buyPage.postcodeError.contentEquals(expectedError));
    }

    //Reloads page and goes to payment, submit random strings which contain 1 invalid char in fields FORENAME, SURNAME,
    //PHONE, POSTCODE (does NOT include EMAIL, ADDRESS1, ADDRESS2, TOWN fields), checks for error information
    @Test
    public void test100_VerifyInvalidStringsSubmitErrors(){
        homePage.load();
        homePage.toBuySection();
        buyPage.invalidCharsSubmit();
        String expectedError = "Invalid format.";
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.forenameError, buyPage.forenameError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.surnameError, buyPage.surnameError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.phoneError, buyPage.phoneError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.postcodeError, buyPage.postcodeError.contentEquals(expectedError));
    }

    //Reloads page and goes to payment, submits '0' char in these 3 fields: FORENAME, SURNAME and POSTCODE, checks for error information
    @Test
    public void test101_Verify0InForenameSurnamePostcode(){
        homePage.load();
        homePage.toBuySection();
        buyPage.zeroNamePostcodeSubmit();
        String expectedError = "Invalid format.";
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.forenameError, buyPage.forenameError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.surnameError, buyPage.surnameError.contentEquals(expectedError));
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.postcodeError, buyPage.postcodeError.contentEquals(expectedError));
    }

    //Reloads page and goes to payment, submits '0' char in EMAIL field only, checks for error information
    @Test
    public void test_102_Verify0InEmail(){
        homePage.load();
        homePage.toBuySection();
        buyPage.zeroEmailSubmit();
        String expectedError = "This is not a valid email.";
        assertTrue("Expected error: " + expectedError + ", was " + buyPage.emailError, buyPage.emailError.contentEquals(expectedError));
    }
}