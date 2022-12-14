package stellarTest.JobTest;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import stellarTest.ConstantsSetting.ConfigDriver;
import stellarTest.ConstantsSetting.ConfigFileReader;
import stellarTest.ConstantsSetting.ConstBrowser;
import stellarTest.PageObjects.MainPage;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class ConstructorTest {
    WebDriver driver;
    MainPage mainPage;
    ConstBrowser browserEnum;
    ConfigFileReader configFileReader = new ConfigFileReader();

    public ConstructorTest(ConstBrowser browserEnum) {
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
    public void init()
    {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.driver = ConfigDriver.getDriver(browserEnum);

        driver.get(configFileReader.getApplicationUrl());
        this.mainPage = new MainPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    public void clickBunsSectionButtonAutoScroll() {
        mainPage.clickFillingsSectionButton();
        mainPage.clickBunsSectionButton();
        boolean isSelected = mainPage.isSectionButtonSelected(mainPage.getBunsSectionButton());
        Assert.assertTrue("Переход к разделу Булки не выполнен", isSelected);
    }

    @Test
    public void clickSousesSectionButtonAutoScroll() {
        mainPage.clickSousesSectionButton();
        boolean isSelected = mainPage.isSectionButtonSelected(mainPage.getSousesSectionButton());
        Assert.assertTrue("Переход к разделу Соусы не выполнен", isSelected);
    }

    @Test
    public void clickFillingsSectionButtonAutoScroll() {
        mainPage.clickFillingsSectionButton();
        boolean isSelected = mainPage.isSectionButtonSelected(mainPage.getFillingsSectionButton());
        Assert.assertTrue("Переход к разделу Начинки не выполнен", isSelected);
    }
}
