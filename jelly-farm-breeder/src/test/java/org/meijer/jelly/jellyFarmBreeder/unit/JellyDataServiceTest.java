package org.meijer.jelly.jellyFarmBreeder.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmBreeder.service.JellyDataService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class JellyDataServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JellyDataService jellyDataService;

}
