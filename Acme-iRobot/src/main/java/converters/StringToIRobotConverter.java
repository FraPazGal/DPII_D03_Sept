package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.IRobotRepository;

import domain.IRobot;

public class StringToIRobotConverter implements
		Converter<String, IRobot> {

	@Autowired
	IRobotRepository iRobotRepository;

	@Override
	public IRobot convert(final String text) {
		IRobot result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.iRobotRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}