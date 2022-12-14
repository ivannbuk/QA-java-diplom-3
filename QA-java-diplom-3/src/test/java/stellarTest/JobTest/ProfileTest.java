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
public class ProfileTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    UserPage accountPage;
    ConstBrowser browserEnum;
    ConfigFileReader configFileReader = new ConfigFileReader();

    public ProfileTest(ConstBrowser browserEnum) {
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
        driver.get(configFileReader.getApplicationUrl());
        mainPage = new MainPage(driver);
        accountPage = new UserPage(driver);
        loginPage = new LoginPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    @DisplayName("Успешный переход по клику на Личный кабинет")
    public void accountButtonSuccess() {
        RequestUser user = ConfigUserUtils.getUniqueUser();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        String accessToken = UserSteps.createUniqueUser(user)
                .then()
                .statusCode(200)
                .extract()
                .as(ResponseSign.class)
                .getAccessToken();

        mainPage.clickAccountButton();
        loginPage.loginWithData(new RequestSign(user.getEmail(), user.getPassword()));
        mainPage.clickAccountButton();

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Личный кабинет не открыт", displayed);

        UserSteps.deleteUser(accessToken);
    }
}
