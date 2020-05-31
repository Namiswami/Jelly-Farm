package aa.meijer.jelly.jellyFarmService.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "jelly", name = "jelly_cage")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CageEntity {
    @Column(name = "cage_number", updatable = false, nullable = false)
    private Long cageNumber;

    @Column(name = "habitat_name")
    private String habitatName;
}
