package app.bambushain.models.pandas;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfile implements Serializable {
    private String email;
    private String displayName;
    private String discordName;
}
