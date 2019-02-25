package co.cybulscy.checkout;

import co.cybulscy.checkout.bootstrap.Loader;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "co.cybulscy.checkout.repository")
@AllArgsConstructor
public class CheckoutApplication implements CommandLineRunner {

	private Loader loader;

	public static void main(String[] args) {
		SpringApplication.run(CheckoutApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		loader.loadBasketToDb();
	}
}
