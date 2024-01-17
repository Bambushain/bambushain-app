package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FreeCompany
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeCompany {
    private Integer id = 0;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
