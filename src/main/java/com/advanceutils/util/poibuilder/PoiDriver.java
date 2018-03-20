package com.advanceutils.util.poibuilder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.advanceutils.util.misc.DateConversions;

public class PoiDriver {
	private static Logger log = Logger.getLogger(PoiDriver.class);

	/**
	 * Using generic and faster excel builder.
	 * @param records
	 * @return
	 */
	public Workbook buildReport(List<RequestPojo> records) {
		Workbook report = null;
		try {
			if (CollectionUtils.isNotEmpty(records)) {
				log.debug("No of records to be filled in the report: " + records.size());
				ExcelBuilder excelBuilder = new ExcelBuilder();
				excelBuilder.startSheet(Constants.SHEET_NM);
				// 1. Create custom styles
				CellStyle headerStyleAttrs = excelBuilder.createStyle(StyleAttribute.BOLD, StyleAttribute.FONT_ARIAL,
						StyleAttribute.FONT_SIZE10, StyleAttribute.BLACK_FONT, StyleAttribute.GREY_25_PERCENT_BACKGROUND,
						StyleAttribute.FILL_BORDER_MEDIUM);
				CellStyle cellStyleAttrs = excelBuilder.createStyle(StyleAttribute.FONT_ARIAL,
						StyleAttribute.FONT_SIZE10, StyleAttribute.BLACK_FONT, StyleAttribute.WORD_WRAP);

				// 2. Build table header
				excelBuilder.buildCustomRows(Constants.ReportConstants.FAILURE_REPORT_HEADERS, headerStyleAttrs);

				// 3. Build table data rows
				for (RequestPojo data : records) {
					excelBuilder.startRow(null);
					excelBuilder.buildCells(data.getParam1() + "", cellStyleAttrs).setCellHighlight(data.getErrorMap().containsKey("param1"), StyleAttribute.YELLOW_BACKGROUND);
					excelBuilder.buildCells(data.getParam2(), cellStyleAttrs).setCellHighlight(data.getErrorMap().containsKey("param2"), StyleAttribute.YELLOW_BACKGROUND);
					excelBuilder.buildCells(DateConversions.getStringFormatddMMMyyyy(data.getParam3()), cellStyleAttrs).setCellHighlight(data.getErrorMap().containsKey("param3"), StyleAttribute.YELLOW_BACKGROUND);
					excelBuilder.buildCells(data.getParam4(), cellStyleAttrs).setCellHighlight(data.getErrorMap().containsKey("param4"), StyleAttribute.YELLOW_BACKGROUND);
					excelBuilder.buildCells(data.getParam5(), cellStyleAttrs).setCellHighlight(data.getErrorMap().containsKey("param5"), StyleAttribute.YELLOW_BACKGROUND);
				}

				// 5. Generate final Excel report
				report = excelBuilder.generateWorkBook();
			}
		} catch (Exception e) {
			log.error("Exception occured while generating excel", e);
		}
		return report;
	}
	
	public static void main(String[] args) {
		PoiDriver driver = new PoiDriver();
		List<RequestPojo> failedRecords = new ArrayList<>();
		// add records...
		driver.buildReport(failedRecords);
	}
}
