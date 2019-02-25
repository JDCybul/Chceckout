package co.cybulscy.checkout.discount;

import co.cybulscy.checkout.model.Basket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class DiscountService {

	private final List<Discount> discounts;

	public Basket includeDiscount(Basket basket) {
		discounts.stream()
				.filter(discount -> discount.hasRight(basket))
				.forEach(discount -> discount.includeDiscount(basket));
		return basket;
	}
}
