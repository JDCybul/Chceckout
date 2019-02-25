package co.cybulscy.checkout.discount;

import co.cybulscy.checkout.model.Basket;

public interface Discount {
	boolean hasRight(Basket basket);
	Basket includeDiscount(Basket basket);
}
