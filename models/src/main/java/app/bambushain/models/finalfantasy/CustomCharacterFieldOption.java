package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * CustomCharacterFieldOption
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCharacterFieldOption implements Serializable {
    private Integer id = 0;
    private String label;
}
