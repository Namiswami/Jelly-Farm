package aa.meijer.jelly.jellyFarmService.model.jelly;

import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import aa.meijer.jelly.jellyFarmService.repository.entity.JellyEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JellyOverview {
    @JsonProperty("blue")
    private long blue;

    @JsonProperty("yellow")
    private long yellow;

    @JsonProperty("red")
    private long red;

    @JsonProperty("green")
    private long green;

    @JsonProperty("orange")
    private long orange;

    @JsonProperty("purple")
    private long purple;

    @JsonProperty("brown")
    private long brown;

    public JellyOverview(List<JellyEntity> jellies) {
        blue = countJellies(Color.BLUE, jellies);
        yellow = countJellies(Color.YELLOW, jellies);
        red = countJellies(Color.RED, jellies);
        green = countJellies(Color.GREEN, jellies);
        orange = countJellies(Color.ORANGE, jellies);
        purple = countJellies(Color.PURPLE, jellies);
        brown = countJellies(Color.BROWN, jellies);
    }

    public long getTotal() {
        return blue + yellow + red + green + orange + purple + brown;
    }

    private long countJellies(Color color, List<JellyEntity> jellies) {
        return jellies.stream()
                .filter(j -> j.getColor() == color)
                .count();
    }
}
