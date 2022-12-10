package StellarTest.JobTest;

import StellarTest.ApiSteps.UserSteps;
import StellarTest.ConstantsSetting.ConfigDriver;
import StellarTest.ConstantsSetting.ConfigFileReader;
import StellarTest.ConstantsSetting.ConfigUserUtils;
import StellarTest.ConstantsSetting.ConstBrowser;
import StellarTest.PageObjects.*;
import StellarTest.PojoObjects.RequestSign;
import StellarTest.PojoObjects.RequestUser;
import StellarTest.PojoObjects.ResponseSign;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

@RunWith(Parameterized.class)
public class LoginTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    ConstBrowser browserEnum;
    ConfigFileReader configFileReader = new ConfigFileReader();

    RequestUser testUser;
    String accessToken;
    RequestSign signInRequest;

    public LoginTest(ConstBrowser browserEnum) {
        this.browserEnum = browserEnum;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {ConstBrowser.CHROME},
                {ConstBrowser.YANDEX}
        };
    }

    @Before
    public void init() {
        testUser = ConfigUserUtils.getUniqueUser();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        ResponseSign signUpResponse = UserSteps.createUniqueUser(testUser)
                .then()
                .statusCode(200)
                .extract()
                .as(ResponseSign.class);
        accessToken = signUpResponse.getAccessToken();

        signInRequest = new RequestSign(testUser.getEmail(), testUser.getPassword());

        this.driver = ConfigDriver.getDriver(browserEnum);

        driver.get(configFileReader.getApplicationUrl());
        this.mainPage = new MainPage(driver);
        this.loginPage = new LoginPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    @After
    public void closeDriver() {
        driver.quit();
        UserSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Вход по кнопке Войти в аккаунт на главной")
    public void signInWithValidDataSuccess() {
        mainPage.clickSignInButton();
        loginPage.loginWithData(signInRequest);
        mainPage.clickAccountButton();
        UserPage accountPage = new UserPage(driver);

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }

    @Test
    @DisplayName("Вход через кнопку Личный кабинет")
    public void signInWithValidDataWithAccountButtonSuccess() {
        mainPage.clickAccountButton();
        loginPage.loginWithData(signInRequest);
        mainPage.clickAccountButton();
        UserPage accountPage = new UserPage(driver);

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void signInWithValidDataFromSignUpFormSuccess() {
        mainPage.clickSignInButton();
        loginPage.clickSignUpButton();
        SignPage signUpPage = new SignPage(driver);
        signUpPage.clickSignInButton();
        loginPage.loginWithData(signInRequest);
        mainPage.clickAccountButton();
        UserPage accountPage = new UserPage(driver);

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public void signInWithValidDataFromPasswordRecoverFormSuccess() {
        mainPage.clickSignInButton();
        loginPage.clickRecoverPasswordButton();
        PasswordPage forgotPasswordPage = new PasswordPage(driver);
        forgotPasswordPage.clickSignInButton();
        loginPage.loginWithData(signInRequest);
        mainPage.clickAccountButton();
        UserPage accountPage = new UserPage(driver);

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }
}
