package stellarTest.JobTest;

import stellarTest.ApiSteps.UserSteps;
import stellarTest.ConstantsSetting.ConfigDriver;
import stellarTest.ConstantsSetting.ConfigFileReader;
import stellarTest.ConstantsSetting.ConfigUserUtils;
import stellarTest.ConstantsSetting.ConstBrowser;
import stellarTest.PageObjects.LoginPage;
import stellarTest.PageObjects.MainPage;
import stellarTest.PageObjects.UserPage;
import stellarTest.PojoObjects.RequestSign;
import stellarTest.PojoObjects.RequestUser;
import stellarTest.PojoObjects.ResponseSign;
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
public class TransitionTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    UserPage accountPage;
    ConstBrowser browserEnum;

    public TransitionTest(ConstBrowser browserEnum) {

        this.browserEnum = browserEnum;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {ConstBrowser.CHROME}
//                {ConstBrowser.YANDEX}
        };
    }
    @Before
    public void init() {
        driver = ConfigDriver.getDriver(browserEnum);

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        accountPage = new UserPage(driver);
        driver.get(new ConfigFileReader().getApplicationUrl());
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    @DisplayName("Успешный переход в конструктор из аккаунта")
    public void transitionConstructorFromAccountSuccess() {
        RequestUser user = ConfigUserUtils.getUniqueUser();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        ResponseSign signUpResponse = UserSteps.createUniqueUser(user)
                .then()
                .statusCode(200)
                .extract()
                .as(ResponseSign.class);

        mainPage.clickAccountButton();
        loginPage.loginWithData(new RequestSign(user.getEmail(), user.getPassword()));
        mainPage.clickAccountButton();
        accountPage.clickGoToConstructorButton();

        boolean displayed = mainPage.getBurgerConstructorHeader().isDisplayed();
        Assert.assertTrue("Конструктор не открыт",displayed);

        UserSteps.deleteUser(signUpResponse.getAccessToken());
    }
}
