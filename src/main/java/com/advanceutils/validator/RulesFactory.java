package com.advanceutils.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author sbansal
 *
 */
@FunctionalInterface
interface Rule {
	public boolean validate(String fieldName, Object value);
}
class EmptinessValidationRule implements Rule {

	@SuppressWarnings("rawtypes")
	public boolean validate(String fieldName, Object value) {
		boolean flg = false;
		if (value instanceof String) {
			flg = null != value && StringUtils.isNotBlank(value.toString());
		} else if (value instanceof Long || value instanceof Integer) {
			flg = null != value;
		} else if (value instanceof List) {
			flg = CollectionUtils.isNotEmpty((List) value);
		} else if (value instanceof Date) {
			flg = null != value;
		}
		return flg;
	}

}

class LengthValidationRule implements Rule {

	private static Map<String, Integer> lengthMap = new HashMap<>();

	static {
		lengthMap.put("param1", 8);
		lengthMap.put("param2", 500);
		lengthMap.put("param3", 500);
		lengthMap.put("param4", 2000);
		lengthMap.put("param5", 2000);
	}

	public boolean validate(String fieldName, Object value) {
		if (null != value) {
			int maxLen = lengthMap.get(fieldName);
			return value.toString().length() <= maxLen;
		}
		return true;
	}

}

class MappedValidationRule implements Rule {

	@SuppressWarnings("rawtypes")
	private static Map<String, List> existencehMap = new HashMap<>();

	static {
		existencehMap.put("param1", Arrays.asList("A", "B", "C", "D"));
		existencehMap.put("param2", Arrays.asList("A", "B", "C", "D"));
		existencehMap.put("param3", Arrays.asList("A", "B", "C", "D"));
		existencehMap.put("param4", Arrays.asList("A", "B", "C", "D"));
		existencehMap.put("param5", Arrays.asList("A", "B", "C", "D"));
		existencehMap.put("param6", Arrays.asList("A", "B", "C", "D"));
	}

	@SuppressWarnings("unchecked")
	public boolean validate(String fieldName, Object value) {
		if (null != value) {
			List<String> trueValues = existencehMap.get(fieldName);
			String valideEnum = trueValues.stream().filter(obj -> obj.equalsIgnoreCase(value.toString())).findAny()
					.orElse(null);
			return null != valideEnum;
		}
		return true;
	}

}

class DateFormatValidationRule implements Rule {

	private String format;

	@Override
	public boolean validate(String fieldName, Object value) {
		if (null != value) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				LocalDate date = LocalDate.parse(value.toString(), formatter);
				date.format(formatter);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public boolean validate(String fieldName, Object value, String format) {
		this.format = format;
		return validate(fieldName, value);
	}
}

class TypeValidationRule implements Rule {

	@SuppressWarnings("rawtypes")
	private Class clazz;

	@Override
	public boolean validate(String fieldName, Object value) {
		if (null != value) {
			try {
				if (Integer.class.getName() == clazz.getName()) {
					Integer.parseInt(value.toString());
				}
				if (Long.class.getName() == clazz.getName()) {
					Long.parseLong(value.toString());
				}
				if (String.class.getName() == clazz.getName()) {
					value.toString();
				}
				if (Date.class.getName() == clazz.getName()) {
					// better to use date format validator 
					// here because of the specific format provided by user
					// which simplifies validations.
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public boolean validate(String fieldName, Object fieldValue, Class clazz) {
		this.clazz = clazz;
		return validate(fieldName, fieldValue);
	}

}

/**
 * @author sbansal 
 * 
 * Customize [...params] as per your request object
 */
public class RulesFactory {

	public static List<Rule> get(String attr) {
		if (StringUtils.equalsIgnoreCase(attr, "param1")) {
			return Arrays.asList(new EmptinessValidationRule(), new LengthValidationRule(), new TypeValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param2")) {
			return Arrays.asList(new EmptinessValidationRule(), new LengthValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param3")) {
			return Arrays.asList(new EmptinessValidationRule(), new DateFormatValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param4")) {
			return Arrays.asList(new EmptinessValidationRule(), new MappedValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param5")) {
			return Arrays.asList(new EmptinessValidationRule(), new MappedValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param6")) {
			return Arrays.asList(new LengthValidationRule());
		}
		return null;
	}

}
