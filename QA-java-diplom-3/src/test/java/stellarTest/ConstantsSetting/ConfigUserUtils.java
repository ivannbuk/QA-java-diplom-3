package stellarTest.ConstantsSetting;

import stellarTest.PojoObjects.RequestUser;
import org.apache.commons.lang3.RandomStringUtils;

public class ConfigUserUtils {
    public static RequestUser getUniqueUser() {
        return new RequestUser(
                RandomStringUtils.randomAlphanumeric(10) + "@testing.ru",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10)
        );
    }
}
