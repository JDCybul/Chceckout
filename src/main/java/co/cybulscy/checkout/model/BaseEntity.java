package co.cybulscy.checkout.model;

import lombok.Data;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	LocalDateTime createDate = LocalDateTime.now();
}
