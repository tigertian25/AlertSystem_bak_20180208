package com.artwellhk.alertsystem.core;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component(SampleOrder.NAME)
public class SampleOrder implements Serializable {
	public static final String NAME = "SampleOrder";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int styleID;
	private String styleNumber;
	private String styleNo;

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public int getStyleID() {
		return styleID;
	}

	public void setStyleID(int styleID) {
		this.styleID = styleID;
	}

	public String getStyleNumber() {
		return styleNumber;
	}

	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}
	public SampleOrder() {}
	public SampleOrder(int styleID, String styleNumber,String styleNo) {
		this.styleID = styleID;
		this.styleNumber = styleNumber;
		this.styleNo=styleNo;
	}

	@Override
	public String toString() {
		return "SampleOrder{" + "styleID='" + styleID + '\'' + ", styleNumber=" + styleNumber + '}';
	}

}
