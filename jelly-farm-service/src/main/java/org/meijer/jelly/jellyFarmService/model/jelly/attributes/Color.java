package org.meijer.jelly.jellyFarmService.model.jelly.attributes;

public enum Color {
    RED, YELLOW, BLUE, GREEN, PURPLE, ORANGE, BROWN;

    public static Color mixColors(Color colorOne, Color colorTwo) {
        if (colorOne == colorTwo) return colorOne;
        else if (isCombinationOf(colorOne, colorTwo, BLUE, RED)) return PURPLE;
        else if (isCombinationOf(colorOne, colorTwo, BLUE, YELLOW)) return GREEN;
        else if (isCombinationOf(colorOne, colorTwo, YELLOW, RED)) return ORANGE;
        else return BROWN;
    }

    private static boolean isCombinationOf(Color colorOne, Color colorTwo, Color combi1, Color combi2) {
        return (colorOne == combi1 && colorTwo == combi2) || (colorTwo == combi1 && colorOne == combi2);
    }
}
