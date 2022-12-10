package StellarTest.PojoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSign {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private UserInfo user;
}
