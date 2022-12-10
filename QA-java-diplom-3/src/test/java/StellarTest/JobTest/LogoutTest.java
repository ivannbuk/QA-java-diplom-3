package StellarTest.JobTest;

import StellarTest.ApiSteps.UserSteps;
import StellarTest.ConstantsSetting.ConfigDriver;
import StellarTest.ConstantsSetting.ConfigFileReader;
import StellarTest.ConstantsSetting.ConfigUserUtils;
import StellarTest.ConstantsSetting.ConstBrowser;
import StellarTest.PageObjects.LoginPage;
import StellarTest.PageObjects.MainPage;
import StellarTest.PageObjects.UserPage;
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
public class LogoutTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    UserPage accountPage;
    ConstBrowser browserEnum;
    RequestUser testUser;
    String accessToken;
    RequestSign signInRequest;

    public LogoutTest(ConstBrowser browserEnum) {
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

        driver = ConfigDriver.getDriver(browserEnum);
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        accountPage = new UserPage(driver);
        driver.get(new ConfigFileReader().getApplicationUrl() + "/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    @DisplayName("Выход по кнопке Выйти в личном кабинете")
    public void logoutButtonSuccess() {
        loginPage.loginWithData(signInRequest);
        mainPage.clickAccountButton();
        accountPage.clickLogoutButton();
        mainPage.clickAccountButton();

        boolean displayed = loginPage.getSignInButton().isDisplayed();
        Assert.assertTrue("Выход из личного кабинета не выполнен", displayed);
    }
}
