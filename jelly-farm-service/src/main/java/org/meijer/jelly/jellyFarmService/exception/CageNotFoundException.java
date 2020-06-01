package org.meijer.jelly.jellyFarmService.exception;

public class CageNotFoundException extends RuntimeException{
    public CageNotFoundException(Long cageNumber) {
        super(String.format("Cage with number: %s does not exist", cageNumber));
    }
}
