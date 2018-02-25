package com.advanceutils.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advanceutils.constants.Constants;


/**
 * @author sbansal
 *
 */
public class BatchQueryHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 * @param list
	 * @return dataList
	 * This method is used to break the bigger list into sub-lists so that we can pass it in IN Clause as separate separate list
	 * 
	 */
	
	public List<List<Long>> batchUtils(List<Long> list) {
		logger.debug("Come Inside the method of batchUtils");
		int batchSize = Constants.THOUSAND;
		int incrementedValueByOne = 1;
		int startedValueZero = 0;
		List<List<Long>> dataList = new ArrayList<List<Long>>();
		List<Long> arr = null;
			 int size = 0;
				if (list!=null && list.size() > batchSize) {
					size = list.size() / batchSize + incrementedValueByOne;
				} else {
					size = incrementedValueByOne;
				}
				for (int i = 0; i < size; i++) {
					arr = new ArrayList<Long>();
					dataList.add(arr);
				}
				int cnt = startedValueZero;
				int j = startedValueZero;
				for (Long tt : list) {
					if (cnt < batchSize) {
						cnt = cnt + incrementedValueByOne;
						dataList.get(j).add(tt);
					}
					if (cnt == batchSize) {
						cnt = startedValueZero;
						j = j + incrementedValueByOne;
					}
				}
		 
		return dataList;
	}

}
