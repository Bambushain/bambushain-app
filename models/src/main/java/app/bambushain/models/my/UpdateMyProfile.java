package app.bambushain.models.my;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
