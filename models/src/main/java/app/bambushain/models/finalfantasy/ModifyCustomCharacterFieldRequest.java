package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ModifyCustomCharacterFieldRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCustomCharacterFieldRequest {
    private List<String> values = new ArrayList<>();
    private String label;
    private Integer position;
}
