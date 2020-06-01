package org.meijer.jelly.jellyFarmService.model.jelly.dto;

import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JellyOverviewDTO {
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

    public JellyOverviewDTO(List<JellyEntity> jellies) {
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
