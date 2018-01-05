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
	@Inject
	AlertTypeRetriever alertTypeRetriever;
	@Inject
	SnoozeAccessor snoozeAccessor;
	
	/*@Override
	public List<Alert> calculateAlertList() {
		List<Alert> returnAlertList = new ArrayList<Alert>();
		List<SampleOrder> sampleOrderList = getSampleOrderList();// 获取未完成的订单列表
		if (sampleOrderList.size() > 0) {
			List<Alert> alertList = alertTypeRetriever.retrieveList(sampleOrderList);// 筛选出有工序未完成的列表
			for (Alert alert : alertList) {// 循环计算超时的数据
				try {
					String timeDifference = "";// 此字段用于显示超时多少时间
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = df.parse(df.format(new Date()));// 当前时间
					// 超出规定时间
					int allowedDuration = alert.getAlertType().getAllowedDuration();// 规定时限
					Date fromTimestamp = df.parse(df.format(alert.getFromTimestamp()));// 当前工序完成时间
					Date lastTimestamp = DateUtils.addSeconds(fromTimestamp, allowedDuration);//规定完成时间
					alert.setLastTimestamp(df.parse(df.format(lastTimestamp)));
					if (now.getTime() > lastTimestamp.getTime()) {// 当前时间大于规定时间表示超时
						AlertSnooze duration = snoozeAccessor.getSnooze(alert.getSampleOrder().getStyleID(),
								alert.getAlertType().getId());

						if (!duration.equals(null) && duration != null) {// 设置了睡眠

							Date durationDate = duration.getCreateTs();
							
							Date snoozeTime = DateUtils.addSeconds(durationDate, allowedDuration);
							
							if (now.getTime() > snoozeTime.getTime()) {// 当前时间大于睡眠后的时间
								timeDifference = dateUtil(df.format(now), df.format(snoozeTime.getTime()));
								alert.setTimeDifference(timeDifference);
								returnAlertList.add(alert);
							}

						} else {

							timeDifference = dateUtil(df.format(now), df.format(lastTimestamp.getTime()));
							alert.setTimeDifference(timeDifference);
							returnAlertList.add(alert);
						}

					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return returnAlertList;
	}*/
	
	// 计算时间差工具
		public String dateUtil(String date1, String date2) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeDifference = "0";
			try {
				Date d1 = df.parse(date1);
				Date d2 = df.parse(date2);
				long l = d2.getTime() - d1.getTime();
				long day = l / (24 * 60 * 60 * 1000);
				long hour = (l / (60 * 60 * 1000) - day * 24);
				long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
				long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
				if (day > 0) {
					timeDifference += day + "天";
				}
				if (hour > 0) {
					timeDifference += hour + "小时";
				}
				if (min > 0) {
					timeDifference += min + "分";
				}
				if (s > 0) {
					timeDifference += s + "秒";
				}
				System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return timeDifference;
		}

		// 获取所有未完成的版单
		private List<SampleOrder> getSampleOrderList() {
			// TODO 获取所有未完成的版单
			List<SampleOrder> SampleOrderList = new ArrayList<>();
			SampleOrderList.add(new SampleOrder(123456, "sp-123456"));
			SampleOrderList.add(new SampleOrder(123457, "sp-123457"));
			SampleOrderList.add(new SampleOrder(123458, "sp-123458"));
			return SampleOrderList;

		}
		@Inject
		List<Alert> alertList;//注入有工序未完成的列表
		
		protected boolean isOverTime(){return false;}
		protected boolean isSetSnoozeTime(){return false;}
		protected boolean isOverSnoozeTime(){return false;}
		@Override
		public List<Alert> calculateAlertList() {
			List<Alert> returnAlertList = new ArrayList<Alert>();
			if(returnAlertList.size()>0) {
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
}
