package co.cybulscy.checkout.exepction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class BasketClosedException extends Exception {

	public BasketClosedException(String message) {
		super(message);
	}
}
