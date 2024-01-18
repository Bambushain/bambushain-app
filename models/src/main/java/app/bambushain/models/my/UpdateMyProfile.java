package app.bambushain.models.my;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UpdateMyProfile
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMyProfile implements Serializable {
    private String email;
    private String displayName;
    private String discordName;
}
