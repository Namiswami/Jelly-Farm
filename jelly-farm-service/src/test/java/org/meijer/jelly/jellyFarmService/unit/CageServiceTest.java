package org.meijer.jelly.jellyFarmService.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.repository.CageRepository;
import org.meijer.jelly.jellyFarmService.service.CageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CageServiceTest {
    @Mock
    private CageRepository cageRepository;

    @InjectMocks
    private CageService cageService;

    @Test
    public void existsById() {
    }

    @Test
    public void getAllCages() {
    }

    @Test
    public void getSingleCageOverview() {
    }

    @Test
    public void getCageOverview() {
    }
}
