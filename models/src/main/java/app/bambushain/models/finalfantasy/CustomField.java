package app.bambushain.models.finalfantasy;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomField {
    @Getter
    @Setter
    private String label;
    @Getter
    private Integer position = 0;
    @Getter
    @Setter
    private Set<String> values;

    public CustomField(String label, Set<String> values) {
        this.label = label;
        this.values = values;
    }
}
