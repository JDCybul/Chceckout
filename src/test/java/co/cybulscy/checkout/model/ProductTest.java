package co.cybulscy.checkout.model;

import co.cybulscy.checkout.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
     void isEqualTest(){
		Product product1 = productRepository.findById(3L).get();
		Product product2 = new Product();
		product2.setId(3L);
		assertEquals(product1, product2);
	}
	@Test
	void isNotEqualWhenDifferentIdTest(){
		Product product1 = productRepository.findById(3L).get();
		Product product2 = new Product();
		product2.setId(4L);
		assertNotEquals(product1, product2);
	}

	@Test
	void isNotEqualTest(){
		Product product = productRepository.findById(3L).get();
		assertNotEquals(product, null);
	}

}