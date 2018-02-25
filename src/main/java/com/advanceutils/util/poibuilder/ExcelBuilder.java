package com.advanceutils.util.poibuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

import com.advanceutils.constants.Constants;
import com.advanceutils.constants.StyleAttribute;
import com.enterprisemath.utils.ValidationUtils;

/**
 * Excel Report Generator Design: Based on Builder Design Pattern Features:
 * efficient, manageable, trackable with built in style resolver and style
 * cache. 
 * 
 * Advice:
 * 1. Avoid using auto resizing to improve performance.
 * 2. Avoid using custom row/column indexes. ie. maintain the BDP consistency at all
 * cost.
 * 3. Create reusable methods for commonly requested features.
 * 
 * @author sbansal
 *
 */
public class ExcelBuilder {
	/**
	 * Underlined workbook.
	 */
	private Workbook workbook;
	/**
	 * Current sheet.
	 */
	private Sheet sheet = null;
	/**
	 * Current row.
	 */
	private Row row = null;
	/**
	 * Next row index.
	 */
	private int nextRowIdx = 0;
	/**
	 * Next column index.
	 */
	private int nextColumnIdx = 0;

	/**
	 * Style bank to cache styles and improve performance
	 */
	private Map<Set<StyleAttribute>, CellStyle> styleBank = new HashMap<Set<StyleAttribute>, CellStyle>();

	/**
	 * Creates new instance.
	 */
	public ExcelBuilder() {
		workbook = new XSSFWorkbook();
	}

	/**
	 * Starts sheet.
	 *
	 * @param name
	 *            sheet name
	 * @return this instance
	 */
	public ExcelBuilder startSheet(String name) {
		sheet = workbook.createSheet(name);
		nextRowIdx = 0;
		nextColumnIdx = 0;
		return this;
	}

	/**
	 * Starts new row.
	 * 
	 * @param idx
	 * 
	 * @param rowAttrs
	 *
	 * @return this instance
	 */
	public ExcelBuilder startRow(CellStyle rowStyleAttrs) {
		row = sheet.createRow(nextRowIdx);
		if (null != rowStyleAttrs) {
			row.setRowStyle(rowStyleAttrs);
		}
		nextRowIdx = nextRowIdx + 1;
		nextColumnIdx = 0;
		return this;
	}

	/**
	 * Create new cell
	 * 
	 * @param index
	 * @param cellValue
	 * @param cellStyleAttrs
	 * @return
	 */
	public ExcelBuilder buildCells(String cellValue, CellStyle cellStyleAttrs) {
		Cell cell = row.createCell(nextColumnIdx);
		if (StringUtils.isNotBlank(cellValue)) {
			setColumnSize(nextColumnIdx,
					cellValue.length() > Constants.ColumnWidths.MAX_WIDTH.val() ? Constants.ColumnWidths.MAX_WIDTH.val()
							: cellValue.length()); // set max 70 characters for size
		} else {
			setColumnSize(nextColumnIdx, Constants.ColumnWidths.DEF_WIDTH.val()); // set default 30 characters for size
		}
		cell.setCellValue(StringUtils.stripToEmpty(cellValue));
		if (null != cellStyleAttrs) {
			cell.setCellStyle(cellStyleAttrs);
		}
		nextColumnIdx = nextColumnIdx + 1;
		return this;
	}

	/**
	 * Build custom rows based on row data (comma separated) & styles provided by the user.
	 * 
	 * @param rowData
	 * @param defSectionAttrs
	 * @return
	 */
	public ExcelBuilder buildCustomRows(String rowData, CellStyle defSectionAttrs) {
		startRow(null);
		String[] arr = rowData.split(", ");
		for (int i = 0; i < arr.length; i++) {
			if (StringUtils.equals(arr[i], Constants.NOVAL_IDENTIFIER)) {
				buildCells(Constants.EMPTY, null);
			} else {
				buildCells(arr[i], defSectionAttrs);
			}
		}
		return this;
	}

	/**
	 * Build custom cell styles based on inputs provided by the user.
	 * 
	 * @param rowData
	 * @param customAttrs
	 * @return
	 */
	public CellStyle createStyle(StyleAttribute... styles) {
		return getCellStyle(styles);
	}

	/**
	 * Sets auto sizing columns. WARNING: Use with caution, huge performance hit.
	 * 
	 * @param idx
	 *            column index, starting from 0
	 * @return this instance
	 */
	public ExcelBuilder setAutoSizeColumn(int idx) {
		sheet.autoSizeColumn(idx);
		return this;
	}

	/**
	 * Sets column size.
	 *
	 * @param idx
	 *            column index, starting from 0
	 * @param m
	 *            number of 'M' standard characters to use for size calculation
	 * @return this instance
	 */
	public ExcelBuilder setColumnSize(int idx, int m) {
		sheet.setColumnWidth(idx, (m + 1) * 256);
		return this;
	}

	/**
	 * Sets row height.
	 *
	 * @return this instance
	 */
	public ExcelBuilder setRowHeight(int pts) {
		ValidationUtils.guardEquals(0, nextColumnIdx, "must be called before inserting columns");
		row.setHeightInPoints(pts);
		return this;
	}

	/**
	 * Sets hyperlink on current column.
	 * @param hostName 
	 *
	 * @return this instance
	 */
	public ExcelBuilder setHyperLink(String url, String hostName) {
		String validatedUrl = url;
		CreationHelper createHelper = workbook.getCreationHelper();
		Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
		if(!ResourceUtils.isUrl(url)) {
			validatedUrl = "http://" + hostName + url;
		}
		link.setAddress(validatedUrl);
		Cell thisCell = row.getCell(nextColumnIdx - 1); // because column is incremented after building new cell.
		thisCell.setHyperlink(link);
		thisCell.setCellStyle(getCellStyle(StyleAttribute.HYPERLINK));
		return this;
	}

	/**
	 * @return
	 */
	public Workbook generateWorkBook() {
		return workbook;
	}

	/**
	 * Returns cell style.
	 *
	 * @param attrs
	 *            attributes
	 * @return cell style
	 */
	private CellStyle getCellStyle(StyleAttribute... attrs) {
		Set<StyleAttribute> allattrs = new HashSet<StyleAttribute>();
		allattrs.addAll(Arrays.asList(attrs));
		if (styleBank.containsKey(allattrs)) {
			return styleBank.get(allattrs);
		}
		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontName(Constants.DEFAULT_FONT);
		style.setFont(font);
		for (StyleAttribute attr : allattrs) {
			if (attr.equals(StyleAttribute.FONT_SIZE12)) {
				font.setFontHeightInPoints((short) 12);
			} else if (attr.equals(StyleAttribute.FONT_SIZE11)) {
				font.setFontHeightInPoints((short) 11);
			} else if (attr.equals(StyleAttribute.FONT_SIZE10)) {
				font.setFontHeightInPoints((short) 10);
			} else if (attr.equals(StyleAttribute.BOLD)) {
				font.setBold(true);
			} else if (attr.equals(StyleAttribute.THIN_BORDER_ALL)) {
				style.setBorderTop(BorderStyle.THIN);
				style.setBorderLeft(BorderStyle.THIN);
				style.setBorderRight(BorderStyle.THIN);
				style.setBorderBottom(BorderStyle.THIN);
			} else if (attr.equals(StyleAttribute.THIN_TOP_BORDER)) {
				style.setBorderTop(BorderStyle.THIN);
			} else if (attr.equals(StyleAttribute.THIN_BOTTOM_BORDER)) {
				style.setBorderBottom(BorderStyle.THIN);
			} else if (attr.equals(StyleAttribute.THICK_TOP_BORDER)) {
				style.setBorderTop(BorderStyle.THICK);
			} else if (attr.equals(StyleAttribute.THICK_BOTTOM_BORDER)) {
				style.setBorderBottom(BorderStyle.THICK);
			} else if (attr.equals(StyleAttribute.ALIGN_LEFT)) {
				style.setAlignment(HorizontalAlignment.LEFT);
			} else if (attr.equals(StyleAttribute.ALIGN_CENTER)) {
				style.setAlignment(HorizontalAlignment.CENTER);
			} else if (attr.equals(StyleAttribute.GARTNER_BLUE_BACKGROUND)) {
				style.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			} else if (attr.equals(StyleAttribute.YELLOW_BACKGROUND)) {
				style.setFillForegroundColor(IndexedColors.YELLOW.index);
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			} else if (attr.equals(StyleAttribute.WHITE_FONT)) {
				font.setColor(IndexedColors.WHITE.index);
			} else if (attr.equals(StyleAttribute.WORD_WRAP)) {
				style.setWrapText(true);
			} else if (attr.equals(StyleAttribute.HYPERLINK)) {
				font.setUnderline(Font.U_SINGLE);
				font.setColor(IndexedColors.BLUE.getIndex());
				font.setBold(false);
				style.setFont(font);
			} else {
				throw new RuntimeException("unknown cell style attribute: " + attr);
			}
		}
		styleBank.put(allattrs, style);
		return style;
	}
}
