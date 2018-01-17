package com.artwellhk.alertsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import com.artwellhk.alertsystem.entity.AlertType;
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
	public Collection<Alert> calculateAlertList() {
		System.out.println("enter calculateAlertList!!!!!!!!!! ");
		Collection<Alert> returnAlertList = new ArrayList<Alert>();
		List<Alert> alertList = getAlertList();

		if (null != alertList && alertList.size() > 0) {
			// for (Alert alert : alertList) {
			// if (isOverTime()) {// 鏄惁瓒呮椂
			// if (isSetSnoozeTime()) {// 鏄惁璁剧疆鐫＄湢
			// if (isOverSnoozeTime()) {// 鏄惁瓒呰繃鐫＄湢鏃堕棿
			// returnAlertList.add(alert);
			// }
			//
			// } else {
			// returnAlertList.add(alert);
			// }
			// }
			// }
			for (Alert alert : alertList) {// 寰幆璁＄畻瓒呮椂鐨勬暟鎹�
				try {
					String timeDifference = "";// 姝ゅ瓧娈电敤浜庢樉绀鸿秴鏃跺灏戞椂闂�
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date now = df.parse(df.format(new Date()));// 褰撳墠鏃堕棿
					// 瓒呭嚭瑙勫畾鏃堕棿
					int allowedDuration = alert.getAlertType().getAllowedDuration();// 瑙勫畾鏃堕檺
					Date fromTimestamp = alert.getFromTimestamp();// 褰撳墠宸ュ簭瀹屾垚鏃堕棿
					Date lastTimestamp = DateUtils.addSeconds(fromTimestamp, allowedDuration);// 瑙勫畾瀹屾垚鏃堕棿
					alert.setLastTimestamp(lastTimestamp);
					if (now.getTime() > lastTimestamp.getTime()&&(now.getTime()-lastTimestamp.getTime())>1000) {// 褰撳墠鏃堕棿澶т簬瑙勫畾鏃堕棿琛ㄧず瓒呮椂
						AlertSnooze alertSnooze = snoozeAccessor.getSnooze(alert.getSampleOrder().getId(),
								alert.getAlertType().getId());

						if (alertSnooze != null && null!=alertSnooze.getId()) {// 璁剧疆浜嗙潯鐪�

							Date durationDate = alertSnooze.getCreateTs();

							Date snoozeTime = DateUtils.addSeconds(durationDate, allowedDuration);

							if (now.getTime() > snoozeTime.getTime()&&(now.getTime()-snoozeTime.getTime())>1000) {// 褰撳墠鏃堕棿澶т簬鐫＄湢鍚庣殑鏃堕棿
								timeDifference = util.dateUtil(now, snoozeTime);
								alert.setTimeDifference(timeDifference);
								returnAlertList.add(alert);
								continue;
							}

						} else {

							timeDifference = util.dateUtil(now, lastTimestamp);
							alert.setTimeDifference(timeDifference);
							returnAlertList.add(alert);
							continue;
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
		// 鏌ヨ鎵�鏈夋湭瀹屾垚涓斿伐鑹哄彂鍑烘湭鏀跺洖鐨勭増鍗�
		
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