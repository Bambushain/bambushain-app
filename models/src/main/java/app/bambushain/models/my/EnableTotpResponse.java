package app.bambushain.models.my;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EnableTotpResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnableTotpResponse {
    private String qrCode;
    private String secret;
}
