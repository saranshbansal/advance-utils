package com.advanceutils.util.misc;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sbansal
 *
 */
public class BatchMaker {

	/**
	 * 
	 * @param list
	 * @param batchSize
	 * @return dataList
	 * This method is used to break the bigger list into sub-lists so that we can pass it in (let's say) IN Clause as separate separate list
	 * 
	 */
	
	public List<List<Long>> batch(List<Long> list, int batchSize) {
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
