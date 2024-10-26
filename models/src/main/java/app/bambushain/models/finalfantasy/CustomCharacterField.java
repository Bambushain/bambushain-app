package app.bambushain.models.finalfantasy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomCharacterField
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCharacterField implements Serializable {
    private Integer id = 0;
    private String label;
    private Integer position;
    private List<CustomCharacterFieldOption> options = new ArrayList<>();
}

