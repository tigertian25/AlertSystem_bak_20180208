package com.artwellhk.alertsystem.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseIntegerIdEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import java.util.Date;

@NamePattern("%s %s %s|id,styleNo,styleNumber")
@MetaClass(name = "alertsystem$SampleOrder")
public class SampleOrder extends BaseIntegerIdEntity {
    private static final long serialVersionUID = -7648971970972141497L;

    @MetaProperty
    protected String styleNumber;

    @MetaProperty
    protected String styleNo;

    @MetaProperty
    protected Integer isReceive;

    @MetaProperty
    protected String gongYiSendEmpl;

    @MetaProperty
    protected Date gonYiSendTime;

    @MetaProperty
    protected Date gongYiReceiveTime;

    public Date getGonYiSendTime() {
        return gonYiSendTime;
    }

    public void setGonYiSendTime(Date gonYiSendTime) {
        this.gonYiSendTime = gonYiSendTime;
    }


    public Date getGongYiReceiveTime() {
        return gongYiReceiveTime;
    }

    public void setGongYiReceiveTime(Date gongYiReceiveTime) {
        this.gongYiReceiveTime = gongYiReceiveTime;
    }




    public void setGongYiSendEmpl(String gongYiSendEmpl) {
        this.gongYiSendEmpl = gongYiSendEmpl;
    }

    public String getGongYiSendEmpl() {
        return gongYiSendEmpl;
    }


    public void setIsReceive(Integer isReceive) {
        this.isReceive = isReceive;
    }

    public Integer getIsReceive() {
        return isReceive;
    }


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