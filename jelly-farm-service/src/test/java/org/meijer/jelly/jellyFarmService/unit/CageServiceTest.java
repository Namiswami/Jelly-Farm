package org.meijer.jelly.jellyFarmService.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.repository.CageRepository;
import org.meijer.jelly.jellyFarmService.service.CageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.meijer.jelly.jellyFarmService.ObjectHelper.getCageEntity;
import static org.meijer.jelly.jellyFarmService.ObjectHelper.getCageEntityList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CageServiceTest {
    @Mock
    private CageRepository cageRepository;

    @InjectMocks
    private CageService cageService;

    @Test
    public void existsByIdReturnsTrueIfIdIsFound() {
        //given
        when(cageRepository.existsById(1L)).thenReturn(true);

        //when-then
        assertTrue(cageService.existsById(1L));
    }

    @Test
    public void existsByIdReturnsFalseIfIdIsNotFound() {
        //given
        when(cageRepository.existsById(1L)).thenReturn(false);

        //when-then
        assertFalse(cageService.existsById(1L));
    }

    @Test
    public void getAllCagesReturnsCageListDTO() {
        //given
        when(cageRepository.findAll()).thenReturn(getCageEntityList());

        //when
        CageListDTO list = cageService.getAllCages();

        //tthen
        assertEquals(3, list.getCageDTOList().size());

        for(CageDTO cage : list.getCageDTOList()) {
            assertEquals(1L, cage.getCageNumber());
            assertEquals("Cloudy Bay", cage.getHabitatName());
        }
    }

    @Test
    public void getAllCagesReturnsEmptyCageListDTOWhenNoCages() {
        //given
        when(cageRepository.findAll()).thenReturn((new ArrayList<>()));

        //when
        CageListDTO list = cageService.getAllCages();

        //then
        assertEquals(0, list.getCageDTOList().size());
    }

    @Test
    public void getSingleCageOverviewReturnsCageOverviewWithJellyOverviewNull() {
        //given
        when(cageRepository.findById(1L)).thenReturn(Optional.of(getCageEntity()));

        //when
        CageOverviewDTO overview = cageService.getSingleCageOverview(1L);

        //then
        assertNull(overview.getJellyOverview());
        assertEquals(1L, overview.getCage().getCageNumber());
        assertEquals("Cloudy Bay", overview.getCage().getHabitatName());
    }

    @Test(expected = CageNotFoundException.class)
    public void getSingleCageOverviewThrowsErrorWhenCageNotFound() {
        //when
        cageService.getSingleCageOverview(1L);

    }

    @Test
    public void getCageOverviewReturnsListOfOverviewsWithJellyOverviewNull() {
        //given
        when(cageRepository.findAll()).thenReturn(getCageEntityList());

        //when
        List<CageOverviewDTO> overviews = cageService.getCageOverview();

        //then
        assertEquals(3, overviews.size());

        for(CageOverviewDTO overview : overviews) {
            assertNull(overview.getJellyOverview());
            assertEquals(1L, overview.getCage().getCageNumber());
            assertEquals("Cloudy Bay", overview.getCage().getHabitatName());
        }
    }

    @Test
    public void getCageOverviewReturnsEmptyListWhenNoResultsFound() {
        //given
        when(cageRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<CageOverviewDTO> overviews = cageService.getCageOverview();

        //then
        assertEquals(0, overviews.size());
    }
}
