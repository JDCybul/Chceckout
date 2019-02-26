package co.cybulscy.checkout.bootstrap;

import co.cybulscy.checkout.exepction.BasketClosedException;
import co.cybulscy.checkout.model.Basket;
import co.cybulscy.checkout.model.Item;
import co.cybulscy.checkout.model.Product;
import co.cybulscy.checkout.repository.BasketRepository;
import co.cybulscy.checkout.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Component
@AllArgsConstructor
@Slf4j
public class Loader {

	private final BasketRepository basketRepository;

	private final ProductRepository productRepository;

	@Transactional
	@Transient
	public void loadBasketToDb() throws BasketClosedException {

		Product mango = createProduct("Mango", BigDecimal.valueOf(4.5));
		Product kiwi = createProduct("Kiwi", BigDecimal.valueOf(3.5));
		Product carrot = createProduct("Carrot", BigDecimal.valueOf(10));
		Product milk = createProduct("Milk", BigDecimal.valueOf(42.5));

		productRepository.save(kiwi);
		productRepository.save(mango);
		productRepository.save(carrot);
		productRepository.save(milk);

		Basket basket = new Basket();
		basket.addItem(Item.createItem(carrot, 3));
		basket.addItem(Item.createItem(milk, 1));
		basket.addItem(Item.createItemWithDiscountPercent(mango, 11, BigDecimal.valueOf(55)));
		basket.addItem(Item.createItem(carrot, 3));
		basketRepository.save(basket);
		log.info("Loader method was used");
	}

	private Product createProduct(String name, BigDecimal price) {
		Product product = Product.builder().name(name).price(price).build();
		return productRepository.save(product);
	}
}
