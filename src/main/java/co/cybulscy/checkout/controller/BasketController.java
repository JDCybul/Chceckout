package co.cybulscy.checkout.controller;

import co.cybulscy.checkout.discount.DiscountService;
import co.cybulscy.checkout.exepction.BasketClosedExceptoin;
import co.cybulscy.checkout.exepction.ResourceNotFoundException;
import co.cybulscy.checkout.model.Basket;
import co.cybulscy.checkout.model.Item;
import co.cybulscy.checkout.repository.BasketRepository;
import co.cybulscy.checkout.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/v1.0/basket")
public class BasketController {

	private final BasketRepository basketRepository;
	private final ProductRepository productRepository;
	private final DiscountService discountService;

	@PostMapping
	public Basket openNewBasket() {
		return basketRepository.save(new Basket());
	}

	@PatchMapping("/{basketId}")
	public void scanItems(@RequestBody @Valid Item item, @PathVariable("basketId") Long basketId) throws BasketClosedExceptoin {
		item.setProduct(productRepository.findById(item.getProduct().getId())
				.orElseThrow(() -> new ResourceNotFoundException("There is no basket with id: " + basketId)));
		Basket basket = getBasket(basketId);
		basket.addItem(item);
		basketRepository.save(basket);
	}

	@DeleteMapping("/{basketId}")
	public BigDecimal closeBasket(@PathVariable("basketId") Long basketId) throws BasketClosedExceptoin {
		Basket basket = getBasket(basketId);
		if (basket.isClosed()) {
			throw new BasketClosedExceptoin("Basket is already closed.");
		}
		basket.close();
		basket = discountService.includeDiscount(basket);
		basketRepository.save(basket);
		return basket.getTotalPrice();
	}

	private Basket getBasket(Long basketId) {
		return basketRepository.findById(basketId)
				.orElseThrow(() -> new ResourceNotFoundException("There is no basket with id: " + basketId));
	}
}
