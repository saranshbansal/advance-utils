package com.advanceutils.util.poibuilder;

public class Constants {
	
	public static interface ReportConstants {
		static final String[] REPORT_HEADERS = { "Request ID", "Request type", "Requester type", "Account code",
				"Account name", "Entitled/Proposed product", "Industry", "Region", "Urgent?",
				"Stage in client relationship", "Initiatives", "Initiatives Count",
				"Description quality Of initiatives", "Slide Type(s) to produce", "Notes", "Renewal Risk",
				"Level of Report", "Report From Date", "Report To Date", "Included Account Codes",
				"Additional Customizations", "Requester Description", "Desired delivery date", "Status",
				"Delivered date", "Owner", "Requester", "Decision maker", "Seatholder name", "Stakeholder name",
				"Seatholder Tenure", "Add date", "Submitted by", "Update date", "Updated by", "Other comments",
				"Opportunity name", "Keywords" };
		static final String FAILURE_REPORT_HEADERS = "Account code, Account name, Desired delivered date, Entitled/Proposed product"
				+ ", Pilot, Stakeholder Name, Seatholder/Prospect Name, Deliverables Included"
				+ ", Stage in client relationship, Requester Description, Special Instructions";
	}

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