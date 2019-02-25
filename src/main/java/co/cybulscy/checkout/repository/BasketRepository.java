package co.cybulscy.checkout.repository;

import co.cybulscy.checkout.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
