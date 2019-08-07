package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Scientist;

@Component
@Transactional
public class ScientistToStringConverter implements Converter<Scientist, String> {

	@Override
	public String convert(final Scientist scientist) {
		String result;

		if (scientist == null)
			result = null;
		else
			result = String.valueOf(scientist.getId());
		return result;
	}
}
