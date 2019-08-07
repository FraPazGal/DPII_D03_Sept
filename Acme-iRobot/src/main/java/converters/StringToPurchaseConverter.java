package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.PurchaseRepository;
import domain.Purchase;

public class StringToPurchaseConverter implements Converter<String, Purchase> {

	@Autowired
	PurchaseRepository purchaseRepository;

	@Override
	public Purchase convert(final String text) {
		Purchase result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.purchaseRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}