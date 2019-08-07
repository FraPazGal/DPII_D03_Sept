package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Purchase;

@Component
@Transactional
public class PurchaseToStringConverter implements Converter<Purchase, String> {

	@Override
	public String convert(final Purchase purchase) {
		String result;

		if (purchase == null)
			result = null;
		else
			result = String.valueOf(purchase.getId());
		return result;
	}
}
