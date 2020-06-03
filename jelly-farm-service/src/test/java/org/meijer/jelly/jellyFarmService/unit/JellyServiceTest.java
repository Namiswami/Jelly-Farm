package org.meijer.jelly.jellyFarmService.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.ObjectHelper;
import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.exception.JellyNotFoundException;
import org.meijer.jelly.jellyFarmService.exception.NewCageCannotBeOldCageException;
import org.meijer.jelly.jellyFarmService.exception.NotEnoughRoomInCageException;
import org.meijer.jelly.jellyFarmService.model.adoption.FreeJellyRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.RecageRequestDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.meijer.jelly.jellyFarmService.service.CageService;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.meijer.jelly.jellyFarmService.ObjectHelper.*;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.MALE;
import static org.mockito.ArgumentMatchers.any;
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

    @Before
    public void init() {
        ReflectionTestUtils.setField(jellyService, "cageLimit", 20);
    }

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
    public void getJellyOverviewReturnsJellyOverview() {
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
    public void getCageOverviewReturnsCageOverview() {
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
    public void getSingleCageOverviewReturnsSingleCageOverview() {
        //given
        CageOverviewDTO overview = getCageOverviewDTO(1L);
        overview.setJellyOverview(null);
        when(cageService.getSingleCageOverview(1L)).thenReturn(overview);
        when(jellyStockRepository.findByCageNumberUnsold(1L)).thenReturn(getJellyEntityList());

        //when
        CageOverviewDTO overviewDTO = jellyService.getCageOverview(1L);

        //then
        CageDTO cage = overviewDTO.getCage();
        assertEquals(1L, cage.getCageNumber());
        assertEquals("White Room", cage.getHabitatName());

        JellyOverviewDTO jellyOverview = overviewDTO.getJellyOverview();
        assertEquals(3, jellyOverview.getBlue());
        assertEquals(0, jellyOverview.getYellow());
        assertEquals(0, jellyOverview.getRed());
        assertEquals(0, jellyOverview.getPurple());
        assertEquals(0, jellyOverview.getGreen());
        assertEquals(0, jellyOverview.getOrange());
        assertEquals(0, jellyOverview.getBrown());
    }


    @Test
    public void getJelliesFiltersOutFreedJellies() {
        //given
        List<JellyEntity> list = getJellyEntityList();
        JellyEntity freedJelly = getJellyEntity(UUID.randomUUID());
        freedJelly.setDateTimeFreed(LocalDateTime.now());
        list.add(freedJelly);

        when(cageService.existsById(1L)).thenReturn(true);
        when(jellyStockRepository.findAll(any(Example.class))).thenReturn(list);

        //when
        List<JellyDTO> jellies = jellyService.getJellies(1L, BLUE, MALE);

        assertEquals(3, jellies.size());

        for(JellyDTO jelly : jellies) {
            assertEquals(1L, jelly.getCageNumber());
            assertEquals(MALE, jelly.getGender());
            assertEquals(BLUE, jelly.getColor());
            assertNull(jelly.getDateTimeFreed());
        }
    }

    @Test(expected = CageNotFoundException.class)
    public void getJelliesThrowsExceptionIfCageNotFound() {
        //given
        when(cageService.existsById(1L)).thenReturn(false);

        //when
        jellyService.getJellies(1L, BLUE, MALE);

    }

    @Test
    public void adoptJellyReturnsJellyDTO() {
        //given
        when(jellyStockRepository.findByCageNumberUnsold(1L)).thenReturn(getJellyEntityList());
        when(cageService.getSingleCageOverview(1L)).thenReturn(getCageOverviewDTO(1L));
        when(jellyStockRepository.save(any())).thenReturn(getJellyEntity(UUID.randomUUID()));

        //when
        JellyDTO jelly = jellyService.adoptJelly(getAdoptionRequest());

        //then
        assertEquals(1L, jelly.getCageNumber());
        assertEquals(BLUE, jelly.getColor());
        assertEquals(MALE, jelly.getGender());
        assertNull(jelly.getDateTimeFreed());
    }

    @Test(expected = NotEnoughRoomInCageException.class)
    public void adoptJellyThrowsExceptionIfNotEnoughRoomInCage() {
        //given
        when(jellyStockRepository.findByCageNumberUnsold(1L)).thenReturn(getJellyEntityListForFullCage());
        when(cageService.getSingleCageOverview(1L)).thenReturn(getCageOverviewDTO(1L));

        //when
        jellyService.adoptJelly(getAdoptionRequest());
    }

    @Test
    public void freeJelliesReturnsListOfJelliesWithAddedDateTimeFreed() {
        //given
        UUID id = UUID.randomUUID();
        FreeJellyRequestDTO freeRequest = new FreeJellyRequestDTO(singletonList(id));

        JellyEntity entity = getJellyEntity(id);
        when(jellyStockRepository.findAllById(any())).thenReturn(singletonList(entity));

        LocalDateTime now = LocalDateTime.now();
        JellyEntity updatedEntity = getJellyEntity(id);
        updatedEntity.setDateTimeFreed(now);
        when(jellyStockRepository.saveAll(any())).thenReturn(singletonList(updatedEntity));

        //when
        List<JellyDTO> jellies = jellyService.freeJellies(freeRequest);

        //then
        assertEquals(1, jellies.size());

        JellyDTO jelly = jellies.get(0);

        assertEquals(id, jelly.getId());
        assertEquals(BLUE, jelly.getColor());
        assertEquals(MALE, jelly.getGender());
        assertEquals(1L, jelly.getCageNumber());
        assertEquals(now, jelly.getDateTimeFreed());

    }

    @Test
    public void recageJelliesReturnsJellyWithNewCageNumber() {
        //given
        UUID id = UUID.randomUUID();
        when(jellyStockRepository.findByCageNumberUnsold(2L))
                .thenReturn(singletonList(getJellyEntity(id)));

        when(jellyStockRepository.findAllById(any()))
                .thenReturn(getJellyEntityList());

        when(cageService.getSingleCageOverview(2L))
                .thenReturn(getCageOverviewDTO(2L));

        JellyEntity recagedEntity = getJellyEntity(id);
        recagedEntity.setCageNumber(2L);
        when(jellyStockRepository.saveAll(any()))
                .thenReturn(singletonList(recagedEntity));
        List<UUID> ids = singletonList(id);

        //when
        List<JellyDTO> jellies = jellyService.recageJellies(new RecageRequestDTO(ids, 2L));

        //then
        assertEquals(1, jellies.size());

        JellyDTO jelly = jellies.get(0);
        assertEquals(2L, jelly.getCageNumber());

    }

    @Test(expected = NotEnoughRoomInCageException.class)
    public void recageJelliesThrowsExceptionWhenNotEnoughRoomInCage() {
        //given
        when(jellyStockRepository.findByCageNumberUnsold(2L)).thenReturn(getJellyEntityListForFullCage());
        when(cageService.getSingleCageOverview(2L)).thenReturn(getCageOverviewDTO(2L));
        List<UUID> ids = singletonList(UUID.randomUUID());

        //when
        jellyService.recageJellies(new RecageRequestDTO(ids, 2L));
    }

    @Test(expected = NewCageCannotBeOldCageException.class)
    public void recageJelliesThrowsExceptionWhenCageNumberForJellyIsSameAsInRequest() {
        //given
        when(jellyStockRepository.findByCageNumberUnsold(1L)).thenReturn(getJellyEntityList());
        when(jellyStockRepository.findAllById(any())).thenReturn(getJellyEntityList());
        when(cageService.getSingleCageOverview(1L)).thenReturn(getCageOverviewDTO(1L));
        List<UUID> ids = singletonList(UUID.randomUUID());

        //when
        jellyService.recageJellies(new RecageRequestDTO(ids, 1L));
    }
}
