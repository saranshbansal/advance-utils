package com.advanceutils.validator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean validate(String fieldName, Object value) {
		boolean flg = false;
		if (value instanceof String) {
			flg = null != value && StringUtils.isNotBlank(value.toString());
		} else if (value instanceof Long || value instanceof Integer) {
			flg = null != value;
		} else if (value instanceof List) {
			// flg = work under progress!;
		} else if (value instanceof Date) {
			flg = null != value;
		}
		return flg;
	}

}

class LengthValidationRule implements Rule {

	private static Map<String, Integer> lengthMap = new HashMap<>();

	static {
		lengthMap.put("accountCode", 8);
		lengthMap.put("accountNm", 500);
		lengthMap.put("seatholderNm", 500);
		lengthMap.put("stakeholderNm", 2000);
		lengthMap.put("requesterDescription", 2000);
		lengthMap.put("otherComments", 2000);
	}

	public boolean validate(String fieldName, Object value) {
		int maxLen = lengthMap.get(fieldName);
		return null == value || StringUtils.isBlank(value.toString()) || value.toString().length() <= maxLen;
	}

}

class ExistenceValidationRule implements Rule {

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
		if (null == value)
			return false;
		List<String> trueValues = existencehMap.get(fieldName);
		String valideEnum = trueValues.stream().filter(obj -> obj.equalsIgnoreCase(value.toString())).findAny()
				.orElse(null);
		return StringUtils.isNotBlank(valideEnum);
	}

}

class DateValidationRule implements Rule {

	@Override
	public boolean validate(String fieldName, Object value) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		try {
			cal.setTime((Date) value);
			cal.getTime();
		} catch (Exception e) {
			return false;
		}
		return true;
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
			return Arrays.asList(new EmptinessValidationRule(), new LengthValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param2")) {
			return Arrays.asList(new EmptinessValidationRule(), new LengthValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param3")) {
			return Arrays.asList(new EmptinessValidationRule(), new DateValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param4")) {
			return Arrays.asList(new EmptinessValidationRule(), new ExistenceValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param5")) {
			return Arrays.asList(new EmptinessValidationRule(), new ExistenceValidationRule());
		} else if (StringUtils.equalsIgnoreCase(attr, "param6")) {
			return Arrays.asList(new LengthValidationRule());
		}
		return null;
	}

}
