package co.cybulscy.checkout.model;

import co.cybulscy.checkout.exepction.BasketClosedException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Slf4j
public class Basket extends BaseEntity {

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL)
	private List<Item> items = new ArrayList<>();

	@Setter(AccessLevel.NONE)
	private BigDecimal totalPrice = BigDecimal.ZERO;

	@Setter(AccessLevel.NONE)
	private boolean closed;

	public void addItem(Item item) throws BasketClosedException {
		if (closed) {
			throw new BasketClosedException("Basket is closed. Cannot add items");
		}
		items.stream().filter(item1 -> isSameItem(item, item1)).forEach(item1 -> item1.setQuantity(item.getQuantity() + item1.getQuantity()));
		if (items.stream().noneMatch(item1 -> isSameItem(item, item1))) {
			items.add(item);
		}
		recalculatePrice();
	}

	public void recalculatePrice() {
		totalPrice = items.stream().map(Item::getTotalPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public void close() {
		closed = true;
	}

	public void includeDiscount(BigDecimal discount) {
		totalPrice = totalPrice.subtract(discount);
	}

	private boolean isSameItem(Item item, Item item1) {
		return item.getProduct().equals(item1.getProduct());
	}
}
