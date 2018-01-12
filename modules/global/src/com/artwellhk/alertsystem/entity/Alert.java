package com.artwellhk.alertsystem.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;

import java.io.Serializable;
import java.util.Date;
import com.haulmont.cuba.core.entity.BaseIntegerIdEntity;
import javax.validation.constraints.NotNull;
import com.haulmont.chile.core.annotations.NamePattern;
import java.util.UUID;
import com.haulmont.cuba.core.entity.HasUuid;

@NamePattern("%s %s %s %s %s %s|sampleOrder,alertType,employeeName,fromTimestamp,lastTimestamp,timeDifference")
@MetaClass(name = "alertsystem$Alert")
public class Alert extends BaseIntegerIdEntity implements HasUuid {
    private static final long serialVersionUID = 4543700162506865893L;

    @NotNull
    @MetaProperty(mandatory = true)
    protected AlertType alertType;

    @MetaProperty
    protected SampleOrder sampleOrder;

    @MetaProperty
    protected Date fromTimestamp;

    @MetaProperty
    protected Date lastTimestamp;

    @MetaProperty
    protected String timeDifference;

    @MetaProperty
    protected String employeeName;

    @MetaProperty
    protected UUID uuid;

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }


    public void setSampleOrder(SampleOrder sampleOrder) {
        this.sampleOrder = sampleOrder;
    }

    public SampleOrder getSampleOrder() {
        return sampleOrder;
    }

    public void setTimeDifference(String timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getTimeDifference() {
        return timeDifference;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }


    public void setLastTimestamp(Date lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public Date getLastTimestamp() {
        return lastTimestamp;
    }


    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }


    public AlertType getAlertType() {
        return alertType;
    }

    public void setFromTimestamp(Date fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public Date getFromTimestamp() {
        return fromTimestamp;
    }
public Alert() {}
	
	public Alert(AlertType alertType,Date fromTimestamp,SampleOrder sampleOrder,String employeeName) {
		this.alertType=alertType;this.fromTimestamp=fromTimestamp;this.sampleOrder=sampleOrder;this.employeeName=employeeName;
	}
	public Alert(AlertType alertType,SampleOrder sampleOrder) {
		this.alertType=alertType;this.sampleOrder=sampleOrder;
	}

}