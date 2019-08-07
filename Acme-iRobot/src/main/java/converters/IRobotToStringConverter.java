package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.IRobot;

@Component
@Transactional
public class IRobotToStringConverter implements
		Converter<IRobot, String> {

	@Override
	public String convert(final IRobot iRobot) {
		String result;

		if (iRobot == null)
			result = null;
		else
			result = String.valueOf(iRobot.getId());
		return result;
	}
}
