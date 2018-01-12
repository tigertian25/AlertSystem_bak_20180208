package com.artwellhk.alertsystem.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseIntegerIdEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s %s %s|id,styleNo,styleNumber")
@MetaClass(name = "alertsystem$SampleOrder")
public class SampleOrder extends BaseIntegerIdEntity {
    private static final long serialVersionUID = -7648971970972141497L;

    @MetaProperty
    protected String styleNumber;

    @MetaProperty
    protected String styleNo;

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNo(String styleNo) {
        this.styleNo = styleNo;
    }

    public String getStyleNo() {
        return styleNo;
    }
    public SampleOrder() {}
	public SampleOrder(int styleID, String styleNumber,String styleNo) {
		this.id = styleID;
		this.styleNumber = styleNumber;
		this.styleNo=styleNo;
	}

	@Override
	public String toString() {
		return "SampleOrder{" + "styleID='" + id + '\'' + ", styleNumber=" + styleNumber + '}';
	}

}