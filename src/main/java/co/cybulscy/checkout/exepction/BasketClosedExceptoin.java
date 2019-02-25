package co.cybulscy.checkout.exepction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class BasketClosedExceptoin extends Exception {

	public BasketClosedExceptoin(String message) {
		super(message);
	}
}
