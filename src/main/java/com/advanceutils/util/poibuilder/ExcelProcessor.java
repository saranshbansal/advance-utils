package com.advanceutils.util.poibuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelProcessor {

	private XSSFWorkbook workbook;
	private int sizeLimit;

	/**
	 * Loading workbook into processor
	 * @param workbook
	 * @param sizeLimit
	 */
	public ExcelProcessor(XSSFWorkbook workbook, int sizeLimit) {
		this.workbook = workbook;
		this.sizeLimit = sizeLimit;
	}
	
	/**
	 * File based workbook generation
	 * @param file
	 * @param sizeLimit
	 * @throws IOException
	 */
	public ExcelProcessor(MultipartFile file, int sizeLimit) throws IOException {
		InputStream inputStream = file.getInputStream();
		this.workbook = new XSSFWorkbook(inputStream);
		this.sizeLimit = sizeLimit;
	}

	/**
	 * Sample request processor. Customize it as per your request object.
	 * 
	 * @return
	 */
	public List<RequestPojo> processRequests() {
		List<RequestPojo> requests = new ArrayList<RequestPojo>();
		Sheet curSheet = workbook.getSheetAt(0);
		// find maximum size
		int totalSize = curSheet.getLastRowNum();
		int maxSize = sizeLimit > 0 ? totalSize > sizeLimit ? totalSize - sizeLimit : totalSize : totalSize;
		for (int rowNum = curSheet.getFirstRowNum() + 1; rowNum <= maxSize; rowNum++) {
			Row r = curSheet.getRow(rowNum);
			if (!checkIfRowIsEmpty(r)) {
				RequestPojo request = new RequestPojo();
				for (int cNum = 0; cNum < r.getLastCellNum(); cNum++) {
					Cell c = r.getCell(cNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c != null) {
						switch (cNum) {
						case 0:
							request.setParam1(new Integer((int) c.getNumericCellValue()));
							break;
						case 1:
							request.setParam2(c.getStringCellValue());
							break;
						case 2:
							request.setParam3(c.getDateCellValue());
							break;
						case 3:
							request.setParam4(c.getStringCellValue());
							break;
						case 4:
							request.setParam5(c.getStringCellValue());
							break;
						}
					}
				}
				requests.add(request);
			}
		}
		return requests;
	}

	/**
	 * @param row
	 * @return
	 */
	private boolean checkIfRowIsEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellTypeEnum().equals(CellType.BLANK) && StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

}
