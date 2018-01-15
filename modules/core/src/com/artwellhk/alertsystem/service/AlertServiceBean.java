package com.artwellhk.alertsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.artwellhk.alertsystem.core.util;
import com.artwellhk.alertsystem.entity.Alert;
import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.artwellhk.alertsystem.entity.SampleOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

@Service(AlertService.NAME)
public class AlertServiceBean implements AlertService {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertServiceBean.class);
	@Inject
	private Persistence persistence;
	@Inject
	private AlertTypeRetrieverService alertTypeRetriever;
	@Inject
	private SnoozeAccessorService snoozeAccessor;

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
		System.out.println("enter calculateAlertList!!!!!!!!!! ");
		List<Alert> returnAlertList = new ArrayList<Alert>();
		List<Alert> alertList = getAlertList();

		if (null != alertList && alertList.size() > 0) {
			// for (Alert alert : alertList) {
			// if (isOverTime()) {// 是否超时
			// if (isSetSnoozeTime()) {// 是否设置睡眠
			// if (isOverSnoozeTime()) {// 是否超过睡眠时间
			// returnAlertList.add(alert);
			// }
			//
			// } else {
			// returnAlertList.add(alert);
			// }
			// }
			// }
			for (Alert alert : alertList) {// 循环计算超时的数据
				try {
					String timeDifference = "";// 此字段用于显示超时多少时间
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = df.parse(df.format(new Date()));// 当前时间
					// 超出规定时间
					int allowedDuration = alert.getAlertType().getAllowedDuration();// 规定时限
					Date fromTimestamp = alert.getFromTimestamp();// 当前工序完成时间
					Date lastTimestamp = DateUtils.addSeconds(fromTimestamp, allowedDuration);// 规定完成时间
					alert.setLastTimestamp(lastTimestamp);
					if (now.getTime() > lastTimestamp.getTime()) {// 当前时间大于规定时间表示超时
						AlertSnooze alertSnooze = snoozeAccessor.getSnooze(alert.getSampleOrder().getId(),
								alert.getAlertType().getId());

						if (alertSnooze != null && alertSnooze.getId() > 0) {// 设置了睡眠

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
		log.debug("returnAlertList:"+gson.toJson(returnAlertList));
		return returnAlertList;

	}

	protected List<Alert> getAlertList() {
		List<Alert> alertList = new ArrayList<Alert>();
		List<SampleOrder> sampleOrderList = new ArrayList<>();
		// 查询所有未完成且工艺发出未收回的版单
		
		Transaction tx = persistence.createTransaction("ERPDB");
		try {
			SqlSession sqlSession = AppBeans.get("sqlSession_ERPDB");
			sampleOrderList = sqlSession.selectList("ERPDBMapper.getAllStyleOfGOngYiSend");

			tx.commit();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		alertList = alertTypeRetriever.retrieveList(sampleOrderList);
		
		return alertList;

	}
}