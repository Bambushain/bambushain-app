package app.bambushain.models.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TwoFactorRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorRequest {
    private String email;
    private String password;
}