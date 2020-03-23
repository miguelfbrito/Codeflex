package pt.codeflex.dto;

public class DateStatus {

	private long date;
	private boolean openingDate; // openingDate, completionDate
	
	public DateStatus(long date, boolean openingDate) {
		super();
		this.date = date;
		this.openingDate = openingDate;
	}
	
	public boolean isOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(boolean openingDate) {
		this.openingDate = openingDate;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	
}
