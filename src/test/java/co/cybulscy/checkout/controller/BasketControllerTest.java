package co.cybulscy.checkout.controller;

import co.cybulscy.checkout.exepction.BasketClosedException;
import co.cybulscy.checkout.exepction.ResourceNotFoundException;
import co.cybulscy.checkout.model.Basket;
import co.cybulscy.checkout.model.Item;
import co.cybulscy.checkout.model.Product;
import co.cybulscy.checkout.repository.BasketRepository;
import co.cybulscy.checkout.repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback
@Slf4j
@NoArgsConstructor
class BasketControllerTest {

	@Autowired
	private BasketController basketController;

	@Autowired
	private BasketRepository basketRepository;

	@Autowired
	private ProductRepository productRepository;

	private Basket basket;

	@BeforeEach
	void init() {
		log.info("test started {}", LocalDateTime.now() );
		basket = basketController.openNewBasket();
	}

	@Test
	void openNewBasket1() {
		Basket basket = basketController.openNewBasket();

		assertNotNull( basket );
		assertTrue( basket.getId() > 0 );
		assertTrue( basketRepository.findById( basket.getId() ).isPresent() );

	}

	@Test
	void openNewBasket2() {
		Basket basket = basketController.openNewBasket();

		assertTrue( !basket.isClosed() );
	}

	@Test
	void openNewBasket3() {
		Basket basket = basketController.openNewBasket();

		assertTrue( basket.getId() > 0 );
	}

	@Test
	void scanItems() {
		Item item = new Item();
		item.setProduct( new Product( "citron", BigDecimal.ONE ) );

		assertThrows( ResourceNotFoundException.class, () -> basketController.scanItems( item, basket.getId() ) );
	}

	@Test
	void scanItems2() {
		Item item = new Item();
		item.setProduct( new Product() );
		item.getProduct().setId( 1L );
		basket.close();

		assertThrows( BasketClosedException.class, () -> basketController.scanItems( item, basket.getId() ) );
	}

	@Test
	void scanItems3() throws BasketClosedException {
		Item item = new Item();
		Product product = new Product();
		product.setId( 1L );
		item.setProduct( product );
		item.setQuantity( 4 );
		basketController.scanItems( item, basket.getId() );
		Basket basketFromDB = basketRepository.findById( basket.getId() ).get();

		assertFalse( basketFromDB.getItems().isEmpty() );
		assertEquals( 1L, basketFromDB.getItems().size() );
		assertEquals( 1L, basketFromDB.getItems().get( 0 ).getProduct().getId() );
		assertEquals( 4, basketFromDB.getItems().get( 0 ).getQuantity() );
	}

	@Test
	void scanItems4() throws BasketClosedException {
		Item item1 = new Item();
		Product product = new Product();
		product.setId( 1L );
		item1.setProduct( product );
		item1.setQuantity( 4 );
		basketController.scanItems( item1, basket.getId() );

		Item item2 = new Item();
		product = new Product();
		product.setId( 1L );
		item2.setProduct( product );
		item2.setQuantity( 6 );
		basketController.scanItems( item2, basket.getId() );
		Basket basketFromDB = basketRepository.findById( basket.getId() ).get();

		assertFalse( basketFromDB.getItems().isEmpty() );
		assertEquals( 1, basketFromDB.getItems().size() );
		assertEquals( 1L, basketFromDB.getItems().get( 0 ).getProduct().getId() );
		assertEquals( 10, basketFromDB.getItems().get( 0 ).getQuantity() );
	}

	@Test
	void scanItems5() throws BasketClosedException {
		final Item item1 = new Item();
		Product product = new Product();
		product.setId( 1L );
		item1.setProduct( product );
		item1.setQuantity( 4 );
		basketController.scanItems( item1, basket.getId() );

		Item item2 = new Item();
		product = new Product();
		product.setId( 2L );
		item2.setProduct( product );
		item2.setQuantity( 55 );
		basketController.scanItems( item2, basket.getId() );
		Basket basketFromDB = basketRepository.findById( basket.getId() ).get();
		Item item1FromDB = basketFromDB.getItems().stream().filter( item -> item.getProduct().equals( item1.getProduct() ) ).findFirst().get();
		Item item2FromDB = basketFromDB.getItems().stream().filter( item -> item.getProduct().equals( item2.getProduct() ) ).findFirst().get();

		assertEquals( 1L, item1FromDB.getProduct().getId() );
		assertEquals( 2L, item2FromDB.getProduct().getId() );
		assertEquals( 4, item1FromDB.getQuantity() );
		assertEquals( 55, item2FromDB.getQuantity() );
		assertFalse( basketFromDB.getItems().isEmpty() );
		assertEquals( 2, basketFromDB.getItems().size() );
	}

	@Test
	void closeBasketSuccessful() throws BasketClosedException {
		basketController.closeBasket( basket.getId() );

		assertTrue( basket.isClosed() );
	}

	@Test
	void baskedAlreadyClosed() {
		basket.close();

		assertThrows( BasketClosedException.class, () -> basketController.closeBasket( basket.getId() ) );
	}

	@Test
	void closeBasketNotExist() {

		assertThrows( ResourceNotFoundException.class, () -> basketController.closeBasket( 888L ) );

	}

	@Test
	void addingItem() throws BasketClosedException {
		Product mango = createProduct( "mango", BigDecimal.valueOf( 4.40 ) );
		Product milk = createProduct( "milk", BigDecimal.valueOf( 2.05 ) );
		Item item = new Item();
		item.setProduct( mango );
		item.setQuantity( 1 );
		item.setDiscountPercent( BigDecimal.valueOf( 10 ) );
		Basket basket = new Basket();
		basket.recalculatePrice();
		basket.addItem( Item.createItemWithDiscountPercent( mango, 1, BigDecimal.valueOf( 10 ) ) );
		basket.addItem( Item.createItemWithDiscountPercent( milk, 10, BigDecimal.valueOf( 10 ) ) );
		basket.addItem( Item.createItem( mango, 1 ) );
		basket.addItem( Item.createItem( milk, 1 ) );
		basket.recalculatePrice();

		assertEquals( BigDecimal.valueOf( 28.19 ), basket.getTotalPrice() );
	}

	@Test
	void calculatingDiscount() throws BasketClosedException {
		Product mango = createProduct( "mango", BigDecimal.valueOf( 4.40 ) );
		Product milk = createProduct( "milk", BigDecimal.valueOf( 2.05 ) );
		Item item = new Item();
		item.setProduct( mango );
		item.setQuantity( 1 );
		item.setDiscountPercent( BigDecimal.valueOf( 10 ) );
		Basket basket = new Basket();
		basket.recalculatePrice();
		basket.addItem( Item.createItemWithDiscountPercent( mango, 1, BigDecimal.valueOf( 10 ) ) );
		basket.addItem( Item.createItemWithDiscountPercent( milk, 10, BigDecimal.valueOf( 10 ) ) );
		basket.recalculatePrice();

		assertEquals( BigDecimal.valueOf( 22.45 ), basket.getTotalPrice() );
	}

	@Test
	void calculatingDiscountWhenIncludeDiscountWasUsed() throws BasketClosedException {
		Product milk = createProduct( "milk", BigDecimal.valueOf( 5 ) );
		Item item = new Item();
		item.setQuantity( 5 );
		Basket basket = new Basket();
		basket.addItem( Item.createItem( milk, 50 ) );
		basket.includeDiscount( BigDecimal.valueOf( 250 ) );

		assertEquals( BigDecimal.valueOf( 0 ), basket.getTotalPrice() );
	}

	private Product createProduct(String name, BigDecimal price) {
		Product product = Product.builder().name( name ).price( price ).build();
		return productRepository.save( product );
	}
}