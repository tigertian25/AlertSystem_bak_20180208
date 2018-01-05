package com.artwellhk.alertsystem.core.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertCalculator;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SampleOrder;
import com.artwellhk.alertsystem.core.SnoozeAccessor;
import com.artwellhk.alertsystem.entity.AlertSnooze;

@Component(AlertCalculator.NAME)
public class AlertCalculatorImpl extends AlertCalculator {
	
		
		
		protected boolean isOverTime(){return false;}
		protected boolean isSetSnoozeTime(){return false;}
		protected boolean isOverSnoozeTime(){return false;}
		@Override
		public List<Alert> calculateAlertList() {
			List<Alert> returnAlertList = new ArrayList<Alert>();
			List<Alert> alertList=getAlertList();
			if(alertList.size()>0) {
				for (Alert alert : alertList) {
					if(isOverTime()) {//是否超时
						if(isSetSnoozeTime()) {//是否设置睡眠
							if(isOverSnoozeTime()) {//是否超过睡眠时间
								returnAlertList.add(alert);
							}
							
						}else {
							returnAlertList.add(alert);
						}
					}
				}
			}
			return returnAlertList;
			
		}
		protected List<Alert> getAlertList(){
			return null;
		}
}
