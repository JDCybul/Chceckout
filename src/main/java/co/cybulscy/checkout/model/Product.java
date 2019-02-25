package co.cybulscy.checkout.model;

import lombok.*;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

	@NonNull
	private String name;
	@Min(1)
	private BigDecimal price;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Product)) return false;
		Product product = (Product) o;
		return id == product.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id);
	}
}
