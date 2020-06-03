package org.meijer.jelly.jellyFarmService.unit;

import org.junit.Test;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;

import static org.junit.Assert.assertEquals;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.*;

public class ColorTest {
    @Test
    public void redAndBlueMakePurple() {
        assertEquals(PURPLE, Color.mixColors(RED, BLUE));
    }

    @Test
    public void redAndYellowMakeOrange() {
        assertEquals(ORANGE, Color.mixColors(RED, YELLOW));
    }

    @Test
    public void blueAndYellowMakeGreen() {
        assertEquals(GREEN, Color.mixColors(YELLOW, BLUE));
    }

    @Test
    public void sameColorsMakeMoreOfTheirOwn() {
        assertEquals(RED, Color.mixColors(RED, RED));
        assertEquals(BLUE, Color.mixColors(BLUE, BLUE));
        assertEquals(YELLOW, Color.mixColors(YELLOW, YELLOW));
        assertEquals(PURPLE, Color.mixColors(PURPLE, PURPLE));
        assertEquals(GREEN, Color.mixColors(GREEN, GREEN));
        assertEquals(ORANGE, Color.mixColors(ORANGE, ORANGE));
        assertEquals(BROWN, Color.mixColors(BROWN, BROWN));
    }

    @Test
    public void anyOtherCombinationMakesBrown() {
        assertEquals(BROWN, Color.mixColors(YELLOW, PURPLE));
        assertEquals(BROWN, Color.mixColors(ORANGE, GREEN));
        assertEquals(BROWN, Color.mixColors(BLUE, BROWN));
    }
}
