package com.advanceutils.validator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

public class Validator {
	private static Logger log = Logger.getLogger(Validator.class);

	/**
	 * @param Requests
	 * @return
	 */
	public static Map<Boolean, List<RequestPojo>> verifyRecords(List<RequestPojo> Requests) {
		Map<Boolean, List<RequestPojo>> processedRecords = Requests.stream().map(request -> verify(request))
				.collect(Collectors.partitioningBy(request -> filterPassedRequest(request)));
		return  processedRecords;
	}

	/**
	 * @param req
	 * @return
	 */
	private static boolean filterPassedRequest(RequestPojo req) {
		log.debug("Error in Request :: " + req.getErrorMap());
		return MapUtils.isEmpty(req.getErrorMap());
	}

	/**
	 * @param request
	 * @return
	 */
	private static RequestPojo verify(RequestPojo request) {
		Map<String, String> errorMap = new HashMap<>();
		Field[] fields = RequestPojo.class.getDeclaredFields();
		Arrays.stream(fields).forEach(field -> {
			field.setAccessible(true);
			String fieldName = field.getName();
			List<Rule> rules = RulesFactory.get(fieldName);
			if(CollectionUtils.isNotEmpty(rules)) {
				int count = 0;
				StringBuilder errmsg = new StringBuilder();
				for(Rule r: rules) {
					try {
						Object fieldValue = field.get(request);
						if (r instanceof EmptinessValidationRule) {
							if (!((EmptinessValidationRule) r).validate(fieldName, fieldValue)) {
								++count;
								errmsg.append(" Empty field |");
							}
						}
						if(r instanceof LengthValidationRule) {
							if(!((LengthValidationRule) r).validate(fieldName, fieldValue)) {
								++count;
								errmsg.append(" Length exceeds |");
							}
						}
						if(r instanceof ExistenceValidationRule) {
							if(!((ExistenceValidationRule) r).validate(fieldName, fieldValue)) {
								++count;
								errmsg.append(" Invalid key |");
							}
						}
						if(r instanceof DateValidationRule) {
							if(!(Date.class.isAssignableFrom(field.getType()) && ((DateValidationRule) r).validate(fieldName, fieldValue))) {
								++count;
								errmsg.append(" Invalid date or format |");
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						log.error("Error while accessing field :: " + e, e);
					}
				}
				if(count > 0) {
					errorMap.put(fieldName, errmsg.toString());
				}
			}
		});
		request.setErrorMap(errorMap);
		return request;
	}

}
