package app.bambushain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateMyProfile
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMyProfile {
    private String email;
    private String displayName;
    private String discordName;
}
