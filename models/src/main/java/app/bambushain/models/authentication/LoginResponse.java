package app.bambushain.models.authentication;

import java.io.Serializable;

import app.bambushain.models.bamboo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
