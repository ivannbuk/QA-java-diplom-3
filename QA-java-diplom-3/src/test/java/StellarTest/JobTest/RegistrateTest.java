package StellarTest.JobTest;

import StellarTest.ApiSteps.UserSteps;
import StellarTest.ConstantsSetting.ConfigDriver;
import StellarTest.ConstantsSetting.ConfigFileReader;
import StellarTest.ConstantsSetting.ConstBrowser;
import StellarTest.PageObjects.LoginPage;
import StellarTest.PageObjects.MainPage;
import StellarTest.PageObjects.SignPage;
import StellarTest.PojoObjects.RequestSign;
import StellarTest.PojoObjects.ResponseSign;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

@RunWith(Parameterized.class)
public class RegistrateTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    SignPage signUpPage;
    ConstBrowser browserEnum;
    public RegistrateTest(ConstBrowser browserEnum) {

        this.browserEnum = browserEnum;
    }

    @Before
    public void init() {
        driver = ConfigDriver.getDriver(browserEnum);
        driver.get(new ConfigFileReader().getApplicationUrl());
        this.mainPage = new MainPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {ConstBrowser.CHROME},
                {ConstBrowser.YANDEX}
        };
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @DisplayName("Успешная регистрация с корректными данными")
    @Test
    public void registrateWithValidDataSuccess() {
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphanumeric(10, 12) + "@test.com";
        String password = RandomStringUtils.randomAlphanumeric(6, 12);

        mainPage.clickSignInButton();
        loginPage = new LoginPage(driver);
        loginPage.clickSignUpButton();
        signUpPage = new SignPage(driver);
        signUpPage.enterName(name);
        signUpPage.enterEmail(email);
        signUpPage.enterPassword(password);
        signUpPage.clickSignUpButton();

        boolean displayed = loginPage.getSignInButton().isDisplayed();
        Assert.assertTrue("Регистрация не выполнена", displayed);

        Response response = UserSteps.signInWithSignInRequest(new RequestSign(email, password));

        String accessToken = response
                .then()
                .statusCode(200)
                .extract()
                .as(ResponseSign.class).getAccessToken();

        UserSteps.deleteUser(accessToken);
    }

    @DisplayName("Ошибка при регистрации - пароль меньше 6 символов")
    @Test
    public void registrateWithInvalidPasswordFails() {
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphanumeric(10, 12) + "@test.com";
        String password = RandomStringUtils.randomAlphanumeric(5);

        mainPage.clickSignInButton();
        loginPage = new LoginPage(driver);
        loginPage.clickSignUpButton();
        signUpPage = new SignPage(driver);
        signUpPage.enterName(name);
        signUpPage.enterEmail(email);
        signUpPage.enterPassword(password);
        signUpPage.clickSignUpButton();

        boolean displayed = signUpPage.getPasswordErrorMessage().isDisplayed();

        if (!displayed) {
            Response response = UserSteps.signInWithSignInRequest(new RequestSign(email, password));

            if (response.getStatusCode() == 200) {
                String accessToken = response.then().extract().as(ResponseSign.class).getAccessToken();
                UserSteps.deleteUser(accessToken);
            }
        }
        Assert.assertTrue("Не выведена ошибка о некорректном пароле", displayed);
    }
}
