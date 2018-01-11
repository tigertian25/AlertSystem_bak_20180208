package com.artwellhk.alertsystem.core.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artwellhk.alertsystem.entity.*;
import com.artwellhk.alertsystem.entity.Process;
import com.artwellhk.alertsystem.service.AlertServiceBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlertCalculatorImplTest extends AlertServiceBean {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertCalculatorImplTest.class);
	@Override
	protected boolean isOverTime(){return true;}
	@Override
	protected boolean isSetSnoozeTime(){return true;}
	@Override
	protected boolean isOverSnoozeTime(){return true;}
	@Override
	protected List<Alert> getAlertList(){
		List<Alert> alertList=new ArrayList<Alert>();
		AlertType alertType = new AlertType(1, new Process(2, "工艺发出"), new Process(3, "工艺收回"), 60 * 30,1,2);
		Alert alert = new Alert(alertType, DateUtils.addSeconds(new Date(), -60 * 30 - 20), new SampleOrder(123456, "sp-123456","L123456"),"张三");
		alertList.add(alert);
		return alertList;
	}
	@Test
	public void testCalculateAlertList() {
		 List<Alert> alertList=calculateAlertList();
		 String alertListString = gson.toJson(alertList);
		 log.debug("alertList：" + alertListString);
	}

}
