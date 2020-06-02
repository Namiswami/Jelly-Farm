package org.meijer.jelly.jellyFarmService.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.ObjectHelper;
import org.meijer.jelly.jellyFarmService.exception.JellyNotFoundException;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.meijer.jelly.jellyFarmService.service.CageService;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.meijer.jelly.jellyFarmService.ObjectHelper.*;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.MALE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JellyServiceTest {
    @Mock
    private JellyStockRepository jellyStockRepository;

    @Mock
    private CageService cageService;

    @InjectMocks
    private JellyService jellyService;

    @Test
    public void getJellyReturnsJellyIfFound() {
        //given
        UUID uuid = UUID.randomUUID();
        when(jellyStockRepository.findById(uuid))
                .thenReturn(Optional.of(getJellyEntity(uuid)));

        //when
        JellyDTO jellyDTO = jellyService.getJelly(uuid);

        //then
        assertEquals(1L, jellyDTO.getCageNumber());
        assertEquals(BLUE, jellyDTO.getColor());
        assertEquals(MALE, jellyDTO.getGender());
        assertEquals(uuid, jellyDTO.getId());
        assertNull(jellyDTO.getDateTimeFreed());
    }

    @Test(expected = JellyNotFoundException.class)
    public void getJellyThrowsExceptionIfNotFound() {
        //given
        UUID uuid = UUID.randomUUID();
        when(jellyStockRepository.findById(uuid))
                .thenReturn(Optional.empty());

        //when
        JellyDTO jellyDTO = jellyService.getJelly(uuid);
    }

    @Test
    public void getJellyOverview() {
        //given
        when(jellyStockRepository.findAll()).thenReturn(getJellyEntityList());

        //when
        JellyOverviewDTO overview = jellyService.getJellyOverview();

        //then
        assertEquals(3, overview.getTotal());
        assertEquals(3, overview.getBlue());
        assertEquals(0, overview.getYellow());
        assertEquals(0, overview.getRed());
        assertEquals(0, overview.getPurple());
        assertEquals(0, overview.getGreen());
        assertEquals(0, overview.getOrange());
        assertEquals(0, overview.getBrown());
    }

    @Test
    public void getCageOverview() {
        //given
        when(cageService.getCageOverview()).thenReturn(getCageOverviewList());
        when(jellyStockRepository.findByCageNumberUnsold(anyLong()))
                .thenReturn(getJellyEntityList());

        //when
        List<CageOverviewDTO> overviews = jellyService.getCageOverview();

        assertEquals(2, overviews.size());

        //then
        for(int i = 0; i < overviews.size() ; i++) {
            CageOverviewDTO cageOverview = overviews.get(i);
            assertEquals( i + 1, cageOverview.getCage().getCageNumber());
            assertEquals( "White Room", cageOverview.getCage().getHabitatName());

            JellyOverviewDTO overview = cageOverview.getJellyOverview();
            assertEquals(3, overview.getBlue());
            assertEquals(0, overview.getYellow());
            assertEquals(0, overview.getRed());
            assertEquals(0, overview.getPurple());
            assertEquals(0, overview.getGreen());
            assertEquals(0, overview.getOrange());
            assertEquals(0, overview.getBrown());
        }

    }

    @Test
    public void testGetCageOverview() {
    }

    @Test
    public void getJellies() {
    }

    @Test
    public void adoptJelly() {
    }

    @Test
    public void freeJellies() {
    }

    @Test
    public void recageJellies() {
    }
}
