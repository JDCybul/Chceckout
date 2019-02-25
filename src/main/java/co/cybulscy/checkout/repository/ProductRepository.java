package co.cybulscy.checkout.repository;

import co.cybulscy.checkout.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
