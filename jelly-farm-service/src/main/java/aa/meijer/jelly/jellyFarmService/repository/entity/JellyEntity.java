package aa.meijer.jelly.jellyFarmService.repository.entity;

import aa.meijer.jelly.jellyFarmService.model.jelly.Jelly;
import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "jelly", name = "jelly_farm")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JellyEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "date_time_sold")
    private LocalDateTime dateTimeSold;

    @Column(name = "cage_number")
    private int cageNumber;

    public JellyEntity(Jelly jelly) {
        color = jelly.getColor();
        gender = jelly.getGender();
        cageNumber = jelly.getCageNumber();
    }
}
