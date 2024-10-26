package app.bambushain.models.finalfantasy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ModifyCustomCharacterFieldRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCustomCharacterFieldRequest implements Serializable {
    private List<String> values = new ArrayList<>();
    private String label;
    private Integer position;
}
