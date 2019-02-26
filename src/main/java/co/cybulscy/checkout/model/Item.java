package co.cybulscy.checkout.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Item extends BaseEntity {

	@ManyToOne(cascade = CascadeType.REFRESH)
	private Product product;
	@Min(0)
	@Max(100)
	private BigDecimal discountPercent = BigDecimal.ZERO;
	@Min(1)
	private int quantity;

	 BigDecimal getTotalPrice(){
		BigDecimal totalPrice =  product.getPrice().multiply(BigDecimal.valueOf(quantity));
		BigDecimal discount = totalPrice.multiply(discountPercent).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
		return totalPrice.subtract(discount);
	}
	@Transient
	public static Item createItem(Product product, int quantity){
		BigDecimal discount = BigDecimal.ZERO;
		return createItemWithDiscountPercent(product, quantity, discount);
	}
	@Transient
	public static Item createItemWithDiscountPercent(Product product, int quantity, BigDecimal discountPercent){
		Item item = new Item();
		item.discountPercent = discountPercent;
		item.product = product;
		item.quantity = quantity;
		return item;
	}
}
