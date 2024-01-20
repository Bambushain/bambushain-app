package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * FreeCompany
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeCompany implements Serializable {
    private Integer id = 0;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
