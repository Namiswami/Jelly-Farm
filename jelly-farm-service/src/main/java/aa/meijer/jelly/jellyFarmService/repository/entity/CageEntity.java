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
    @Id
    @Column(name = "cage_number")
    private Integer cageNumber;

    @Column(name = "habitat_name")
    private String habitatName;
}
