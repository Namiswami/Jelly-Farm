package org.meijer.jelly.jellyFarmService.exception;

import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;

import java.util.List;

public class NewCageCannotBeOldCageException extends RuntimeException {
    public NewCageCannotBeOldCageException(List<JellyEntity> erroneousJellies) {
        super(String.format("Jellies are already in the cage specified, not jellies were moved. Ids: %s", getIdsFromList(erroneousJellies)));
    }

    private static String getIdsFromList(List<JellyEntity> erroneousJellies) {
        String ids = "";
        boolean firstId = true;

        for(JellyEntity jelly: erroneousJellies) {
            if(firstId) {
                ids = ids.concat(jelly.getId().toString());
                firstId = false;
            } else ids = ids.concat(", " + jelly.getId().toString());
        }

        return ids;
    }
}
