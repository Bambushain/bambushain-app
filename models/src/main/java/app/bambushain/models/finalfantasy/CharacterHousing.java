package app.bambushain.models.finalfantasy;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
