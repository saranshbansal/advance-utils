package com.advanceutils.util.poibuilder;

public class Constants {

	// POI Builder helpers
	public final static String SHEET_NM = "Sheet1";
	public final static String REPORT_NM = "Sample_Sheet";
	public static final String EMPTY = "";
	public static final String NOVAL_IDENTIFIER = "_";
	public static final String DEFAULT_FONT = "Calibri (Body)";
	public static final int THOUSAND = 1000;

	public enum ExportFormats {
		XLS(".xls"), XLSX(".xlsx");

		private String val;

		ExportFormats(String val) {
			this.val = val;
		}

		public String val() {
			return val;
		}
	}

	public enum ColumnWidths {
		MAX_WIDTH(70), DEF_WIDTH(50);

		private int val;

		ColumnWidths(int val) {
			this.val = val;
		}

		public int val() {
			return val;
		}
	}
}