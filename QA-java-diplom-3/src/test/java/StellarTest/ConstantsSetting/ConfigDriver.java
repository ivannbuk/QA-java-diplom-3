package StellarTest.ConstantsSetting;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import static StellarTest.ConstantsSetting.ConstBrowser.CHROME;
import static StellarTest.ConstantsSetting.ConstBrowser.YANDEX;

public class ConfigDriver {
    public static WebDriver getDriver(ConstBrowser browser) {
//        if (CHROME.equals(browser)) {
//            WebDriverManager.chromedriver().setup();
//            return new ChromeDriver();
//        } else if (YANDEX.equals(browser)) {
//            System.setProperty("webdriver.chrome.driver",
//                    "src/test/java/StellarTest/YandexDriver/yandexdriver.exe");
//
//            return new ChromeDriver();
//        }
//        throw new EnumConstantNotPresentException(CHROME.getDeclaringClass(), "BROWSER");
        switch (browser) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();

            case YANDEX:
                System.setProperty("webdriver.chrome.driver",
                        "src/test/java/StellarTest/YandexDriver/yandexdriver.exe");
                return new ChromeDriver();

            default:
                throw new EnumConstantNotPresentException(ConstBrowser.class, "BROWSER");
    }
}
}
