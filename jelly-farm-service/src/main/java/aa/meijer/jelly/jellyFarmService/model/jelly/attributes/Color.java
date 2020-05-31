package aa.meijer.jelly.jellyFarmService.model.jelly.attributes;

public enum Color {
    RED, YELLOW, BLUE, GREEN, PURPLE, ORANGE, BROWN;

    public static Color mixColors(Color colorOne, Color colorTwo) {
        if (colorOne == colorTwo) return colorOne;
        if (colorOne == RED && colorTwo == BLUE) return PURPLE;
        if (colorOne == RED && colorTwo == YELLOW) return ORANGE;
        if (colorOne == BLUE && colorTwo == YELLOW) return GREEN;
        else return BROWN;

    }
}
