package org.meijer.jelly.jellyFarmService.model.jelly.entity;

import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
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

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "date_time_freed")
    private LocalDateTime dateTimeFreed;

    @Column(name = "cage_number")
    private long cageNumber;

    public JellyEntity(JellyDTO jellyDTO) {
        color = jellyDTO.getColor();
        gender = jellyDTO.getGender();
        cageNumber = jellyDTO.getCageNumber();
    }
}
