package com.artwellhk.alertsystem.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component(Alert.NAME)
public class Alert implements Serializable{
	public static final String NAME = "Alert";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AlertType alertType;
	private Date fromTimestamp;
	private Date lastTimestamp;
	private SampleOrder sampleOrder;
	private String timeDifference;
	private String employeeName;
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Alert() {}
	
	public Alert(AlertType alertType,Date fromTimestamp,SampleOrder sampleOrder,String employeeName) {
		this.alertType=alertType;this.fromTimestamp=fromTimestamp;this.sampleOrder=sampleOrder;this.employeeName=employeeName;
	}
	public Alert(AlertType alertType,SampleOrder sampleOrder) {
		this.alertType=alertType;this.sampleOrder=sampleOrder;
	}

	public String getTimeDifference() {
		return timeDifference;
	}

	public void setTimeDifference(String timeDifference) {
		this.timeDifference = timeDifference;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(AlertType alerttype) {
		this.alertType = alerttype;
	}

	public Date getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Date fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public Date getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(Date lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public SampleOrder getSampleOrder() {
		return sampleOrder;
	}

	public void setSampleOrder(SampleOrder sampleOrder) {
		this.sampleOrder = sampleOrder;
	}
}
