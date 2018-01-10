package com.artwellhk.alertsystem.core.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertCalculator;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SampleOrder;
import com.artwellhk.alertsystem.core.SnoozeAccessor;
import com.artwellhk.alertsystem.core.util;
import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

@Component(AlertCalculator.NAME)
public class AlertCalculatorImpl extends AlertCalculator {
	@Inject
	private Persistence persistence;
	@Inject
	private AlertTypeRetriever alertTypeRetriever;
	@Inject
	private SnoozeAccessor snoozeAccessor;

	protected boolean isOverTime() {
		return false;
	}

	protected boolean isSetSnoozeTime() {
		return false;
	}

	protected boolean isOverSnoozeTime() {
		return false;
	}

	@Override
	public List<Alert> calculateAlertList() {
		List<Alert> returnAlertList = new ArrayList<Alert>();
		List<Alert> alertList = getAlertList();
		if (alertList.size() > 0) {
//			for (Alert alert : alertList) {
//				if (isOverTime()) {// 是否超时
//					if (isSetSnoozeTime()) {// 是否设置睡眠
//						if (isOverSnoozeTime()) {// 是否超过睡眠时间
//							returnAlertList.add(alert);
//						}
//
//					} else {
//						returnAlertList.add(alert);
//					}
//				}
//			}
			for (Alert alert : alertList) {// 循环计算超时的数据
				try {
					String timeDifference = "";// 此字段用于显示超时多少时间
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = df.parse(df.format(new Date()));// 当前时间
					// 超出规定时间
					int allowedDuration = alert.getAlertType().getAllowedDuration();// 规定时限
					Date fromTimestamp = alert.getFromTimestamp();// 当前工序完成时间
					Date lastTimestamp = DateUtils.addSeconds(fromTimestamp, allowedDuration);//规定完成时间
					alert.setLastTimestamp(lastTimestamp);
					if (now.getTime() > lastTimestamp.getTime()) {// 当前时间大于规定时间表示超时
						AlertSnooze alertSnooze = snoozeAccessor.getSnooze(alert.getSampleOrder().getStyleID(),
								alert.getAlertType().getId());

						if ( alertSnooze != null&&alertSnooze.getId()>0) {// 设置了睡眠

							Date durationDate = alertSnooze.getCreateTs();
							
							Date snoozeTime = DateUtils.addSeconds(durationDate, allowedDuration);
							
							if (now.getTime() > snoozeTime.getTime()) {// 当前时间大于睡眠后的时间
								timeDifference = util.dateUtil(now, snoozeTime);
								alert.setTimeDifference(timeDifference);
								returnAlertList.add(alert);
							}

						} else {

							timeDifference = util.dateUtil(now, lastTimestamp);
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

	}

	protected List<Alert> getAlertList() {
		List<Alert> alertList = new ArrayList<Alert>();
		List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
		//查询所有未完成的版单
		try (Transaction tx = persistence.createTransaction("ERPDB")) {
			SqlSession sqlSession = AppBeans.get("sqlSession");
			sampleOrderList = sqlSession.selectList("ERPDBMapper.getAllStyle");
			tx.commit();
		} catch (NoResultException e) {
			return null;
		}
		if(sampleOrderList!=null&&sampleOrderList.size()>0) {
			alertList=alertTypeRetriever.retrieveList(sampleOrderList);
		}
		return alertList;
	}
}
