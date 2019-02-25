package co.cybulscy.checkout.discount.table;

import co.cybulscy.checkout.discount.Discount;
import co.cybulscy.checkout.model.Basket;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TotalPriceDiscount implements Discount {
	@Override
	public boolean hasRight(Basket basket) {
		return greaterThan100(basket.getTotalPrice());
	}

	private boolean greaterThan100(BigDecimal amount) {
		return amount.compareTo(BigDecimal.valueOf(100))>0;
	}

	@Override
	public Basket includeDiscount(Basket basket) {
		BigDecimal discount = BigDecimal.valueOf(20);
		basket.includeDiscount(discount);
		return basket;
	}
}
