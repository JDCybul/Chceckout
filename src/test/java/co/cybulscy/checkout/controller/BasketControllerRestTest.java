package co.cybulscy.checkout.controller;

import co.cybulscy.checkout.model.Basket;
import co.cybulscy.checkout.model.Item;
import co.cybulscy.checkout.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestTemplate;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.client.HttpClientErrorException.BadRequest;

@Disabled("Disabled cuz you need to start application before")
class BasketControllerRestTest {

	private static String BASKET_REST_URL = "http://localhost:8080/v1.0/basket";

	private RestTemplate restTemplate;

	@BeforeEach
	void init() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		int TIMEOUT = 1000;
		requestFactory.setConnectTimeout(TIMEOUT);
		requestFactory.setReadTimeout(TIMEOUT);
		restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(requestFactory);
	}

	@Test
	void openNewBasket1() {
		ResponseEntity<Basket> response = restTemplate.exchange(BASKET_REST_URL, POST, null, Basket.class);

		assertEquals(SC_OK, response.getStatusCodeValue());
	}

	@Test
	void scanItems() {
		long basketId = 5L;
		HttpEntity<Item> item = buildScanItemEmptyParam();
		ResponseEntity<Object> response = restTemplate.exchange(createScanItemUrl(basketId), PATCH, item, Object.class);

		assertEquals(SC_OK, response.getStatusCodeValue());
	}

	@Test
	void scanItem2() {
		long basketId = 1231312L;
		HttpEntity<Item> item = buildScanItemEmptyParam();

		assertThrows(NotFound.class, () -> restTemplate.exchange(createScanItemUrl(basketId), PATCH, item, Object.class));
	}

	@Test
	void scanItems3() {
		long basketId = 5L;
		HttpEntity<Item> item = new HttpEntity<>(new Item());

		assertThrows(BadRequest.class, () -> restTemplate.exchange(createScanItemUrl( basketId ), PATCH, item, Object.class));
	}

	private String createScanItemUrl(long basketId) {
		return BASKET_REST_URL + "/" + basketId;
	}

	private HttpEntity<Item> buildScanItemEmptyParam() {
		Item item = new Item();
		Product product = new Product();
		product.setId(1L);
		product.setName("");
		item.setProduct(product);
		item.setId(0L);
		item.setQuantity(1);
		return new HttpEntity<>(item);
	}
}