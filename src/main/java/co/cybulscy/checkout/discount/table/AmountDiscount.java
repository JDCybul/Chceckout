package co.cybulscy.checkout.discount.table;

import co.cybulscy.checkout.discount.Discount;
import co.cybulscy.checkout.model.Basket;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AmountDiscount implements Discount {
	private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(10);
	@Override
	public boolean hasRight(Basket basket) {
		long productWithMoreThen1Quantity = basket.getItems()
				.stream()
				.filter(item -> item.getQuantity() > 0)
				.count();
		return productWithMoreThen1Quantity > 0;
	}

	@Override
	public Basket includeDiscount(Basket basket) {
		basket.getItems().stream().filter(item -> item.getQuantity() > 0)
				.forEach(item -> item.setDiscountPercent(item.getDiscountPercent()
						.add(DISCOUNT_PERCENTAGE)));
		basket.recalculatePrice();
		return basket;
	}
}
