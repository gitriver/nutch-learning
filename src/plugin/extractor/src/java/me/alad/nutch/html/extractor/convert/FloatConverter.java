package me.alad.nutch.html.extractor.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloatConverter implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FloatConverter.class);
	public static final float DEFAULT_VALUE = 0L;

	@Override
	public Object convert(String value) {

		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			LOGGER.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
