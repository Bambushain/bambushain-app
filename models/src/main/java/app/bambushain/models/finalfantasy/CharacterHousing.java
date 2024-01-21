package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterHousing implements Serializable {
    private Integer id;
    private HousingDistrict district;
    private HousingType housingType;
    private Integer ward;
    private Integer plot;
    private Integer characterId;
}
