package org.meijer.jelly.jellyFarmService.exception;

public class NotEnoughRoomInCageException extends RuntimeException {
    public NotEnoughRoomInCageException(Long newCageNumber) {
        super(String.format("Not enough room in cage %s", newCageNumber));
    }
}
