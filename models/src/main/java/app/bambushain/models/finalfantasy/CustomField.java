package app.bambushain.models.finalfantasy;

import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomField {
    @Setter
    private String label;
    private final Integer position = 0;
    @Setter
    private Set<String> values;

    public CustomField(String label, Set<String> values) {
        this.label = label;
        this.values = values;
    }
}
