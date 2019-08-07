package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.ScientistRepository;

import domain.Scientist;

public class StringToScientistConverter implements Converter<String, Scientist> {

	@Autowired
	ScientistRepository scientistRepository;

	@Override
	public Scientist convert(final String text) {
		Scientist result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.scientistRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}