package aa.meijer.jellyfarm;

import aa.meijer.jellyfarm.repository.JellyStockRepository;
import aa.meijer.jellyfarm.service.JellyOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JellyFarmApplicationTest {

	@Autowired
	private JellyStockRepository jellyStockRepository;

	@Autowired
	private JellyOrderService jellyOrderService;

	@BeforeEach
	public void init() {
		jellyStockRepository.deleteAll();
	}

	@Test
	public void kafkaTest() {

		jellyOrderService.testKafka();
	}

}
