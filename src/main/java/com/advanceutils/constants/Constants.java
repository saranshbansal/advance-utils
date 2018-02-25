package com.advanceutils.constants;

public class Constants {
	public final static String SHEET_NM = "OSI";
	public final static String REPORT_NM = "OSI_Analysis";
	public static final String EMPTY = "";
	public static final String NOVAL_IDENTIFIER = "_";
	public static final String DEFAULT_FONT = "Calibri (Body)";	
	
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
