package app.bambushain.models.authentication;

import app.bambushain.models.bamboo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * LoginResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse implements Serializable {
    private User user;
    private String token;
}
