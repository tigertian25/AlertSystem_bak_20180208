package com.artwellhk.alertsystem.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.artwellhk.alertsystem.core.util;
import com.artwellhk.alertsystem.entity.Alert;
import com.artwellhk.alertsystem.entity.AlertType;
import com.artwellhk.alertsystem.entity.AlertTypeID;
import com.artwellhk.alertsystem.entity.Process;
import com.artwellhk.alertsystem.entity.SampleOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

@Service(AlertTypeRetrieverService.NAME)
public class AlertTypeRetrieverBean implements AlertTypeRetrieverService {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	@Inject
	private Persistence persistence;

	@Override
	public List<Alert> retrieveList(List<SampleOrder> sampleOrderList) {
		List<Alert> alertList = new ArrayList<>();
		if (sampleOrderList.size() > 0) {
			// 瀛樻斁涓存椂鏁版嵁

			Object obj = null;// 褰撳墠宸ュ簭
			Object objNext = null;// 涓嬩竴宸ュ簭
			// 寰幆sampleOrderList
			for (SampleOrder sampleOrder : sampleOrderList) {
				// 宸ヨ壓鏈彂鍑�
				obj = null;

				Map<String, Object> parameter = new HashMap<>();
				// parameter.put("styleID", 531410628);
				parameter.put("styleID", sampleOrder.getId());

				// 宸ヨ壓鏈敹鍥�
				if (sampleOrder.getIsReceive() == 1) {
					Alert alert = calculatorAlert(sampleOrder, null, AlertTypeID.GongYiNoReceive.getId(), 1);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				}
				// 鐢昏姳鏈彂鍑�
				// obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaSend", parameter);
				if (null == objNext) {
					Alert alert = calculatorAlert(sampleOrder, null, AlertTypeID.HuaHuaNoSend.getId(), 2);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				}
				// 鍒ゆ柇鏄惁鐢昏姳鏀跺洖
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaReceive", parameter);
				if (null == objNext) {
					Alert alert = calculatorAlert(sampleOrder, (Map<String, Object>) obj,
							AlertTypeID.HuaHuaNoReceive.getId(), 3);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				}
				
				/**
				 * 鏌ヨ鐢垫満閮ㄥ強鍚庨潰宸ュ簭鐨勫彂鍑鸿褰�(鏌ヨ鏈�鏂颁竴鏉℃敹鍥炶褰�) 锛宨f(iss.*涓虹┖){琛ㄧず鐢垫満閮ㄦ湭鍙戝嚭}else
				 * if(issbc.aStatus=1){鏄湭鏀跺洖}else if(issbc.aStatus=3){宸叉敹鍥烇紝
				 * if(鏀跺洖鐨勫伐搴忎笉鏄渶鍚庡伐搴�){杩斿洖鏀跺洖鏃堕棿}}
				 */
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getIssue60ByStyleID", parameter);// 鏈�杩戜竴涓彂鍑虹殑宸ュ簭
				if (null == objNext) {// 琛ㄧず鐢昏姳鏀跺洖鍚庢病宸ュ簭鍙戝嚭鍗� 鐢垫満鏈彂鍑�
					Alert alert = calculatorAlert(sampleOrder, (Map<String, Object>) obj,
							AlertTypeID.DianJiNoSend.getId(), 3);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				} else {// 鏈�杩戝彂鍑虹殑宸ュ簭
					obj = objNext;
					Map<String, Object> map = (Map<String, Object>) obj;// 鏈�杩戝彂鍑虹殑宸ュ簭
					Process process = null;
					try (Transaction tx = persistence.createTransaction()) {// 鏌ヨ鏈�杩戝彂鍑虹殑宸ュ簭鏄粈涔堝伐搴�
						EntityManager em = persistence.getEntityManager();
						process = (Process) em
								.createQuery(
										"select o from alertsystem$Process o where o.zt_working_id = :zt_working_id")
								.setParameter("zt_working_id", map.get("sendWorkId")).getSingleResult();
						tx.commit();
					} catch (NoResultException e) {
						e.printStackTrace();
						return null;
					}
					if (null != process && "1".equals(map.get("aStatus").toString())) {// aStatus=1琛ㄧず褰撳墠宸ュ簭鍙戝嚭鏈敹鍥�

						// 鍒ゆ柇褰撳墠宸ュ簭鏄粈涔�
						AlertType alertType = null;
						try (Transaction tx = persistence.createTransaction()) {
							EntityManager em = persistence.getEntityManager();
							alertType = (AlertType) em.createQuery(
									"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where"
											+ " f.id= :id")
									.setParameter("id", process.getId()).getSingleResult();
							tx.commit();
						} catch (NoResultException e) {
							return null;
						}
						if (null != alertType) {
							Alert alert = new Alert(alertType, util.stringToDate(map.get("sendTime").toString()),
									sampleOrder, map.get("sendEmployee").toString());
							alertList.add(alert);
						}
						continue;
					}
					if (null != process && "3".equals(map.get("aStatus").toString())) {// aStatus=3琛ㄧず褰撳墠宸ュ簭宸叉敹鍥烇紝涓嬩竴宸ュ簭鏈彂鍑�
						// 鍒ゆ柇褰撳墠宸ュ簭鏄惁鏈渶鍚庡伐搴�
						AlertType alertType = null;
						try (Transaction tx = persistence.createTransaction()) {
							EntityManager em = persistence.getEntityManager();
							alertType = (AlertType) em.createQuery(
									"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t "
											+ "  order by a.id desc")
									.getFirstResult();// 鑾峰彇鏈�鍚庝竴涓猘lertType
							tx.commit();
						} catch (NoResultException e) {
							return null;
						}
						// 姣旇緝鏈�鍚庝竴涓猘lertType鐨勬敹鍥炲伐搴忎笌褰撳墠鏀跺洖宸ュ簭鏄惁鐩哥瓑
						if (null != alertType && alertType.getFromProcess().getId() != process.getId()) {// 琛ㄧず褰撳墠宸ュ簭涓嶆槸鏈�鍚庡伐搴�
							try (Transaction tx = persistence.createTransaction()) {
								EntityManager em = persistence.getEntityManager();
								alertType = (AlertType) em.createQuery(
										"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where"
												+ " f.id= :id order by a.id desc")
										.setParameter("id", process.getId()).getFirstResult();
								tx.commit();
							} catch (NoResultException e) {
								return null;
							}
							if (null != alertType) {
								Alert alert = new Alert(alertType, util.stringToDate(map.get("receiveTime").toString()),
										sampleOrder, map.get("receiveName").toString());
								alertList.add(alert);
							}
							continue;
						}
					}
				}

			}
		}
		//System.out.println("--retrieveList:"+gson.toJson(alertList));
		return alertList;
	}

	/*
	 * dataType:1宸ヨ壓鏈敹鍥烇紝2鐢昏姳鏈彂鍑猴紝3鍏朵粬
	 */
	private Alert calculatorAlert(SampleOrder sampleOrder, Map<String, Object> map, int alertTypeID, int dataType) {
		
		AlertType alertType = null;
		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();
			alertType = (AlertType) em.createQuery(
					"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where"
							+ " a.id= :id")
					.setParameter("id", alertTypeID).getSingleResult();
			tx.commit();
		} catch (NoResultException e) {
			return null;
		}
		
		Alert alert = null;
		if (null != alertType) {
			if (dataType == 1) {// 宸ヨ壓鏈敹鍥�
				alert = new Alert(alertType, sampleOrder);
				alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
				alert.setFromTimestamp(sampleOrder.getGonYiSendTime());
			} else if (dataType == 2) {// 鐢昏姳鏈彂鍑�
				alert = new Alert(alertType, sampleOrder);
				alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
				alert.setFromTimestamp(sampleOrder.getGongYiReceiveTime());
			} else {
				alert = new Alert(alertType, util.stringToDate(map.get("fromTimestamp").toString()), sampleOrder,
						map.get("employeeName").toString());
			}
		}
		return alert;

	}

	/**
	 * 
	 * @param sqlname:mybatis鐨凪apper涓搴旂殑sql鐨勫悕绉帮紙namespace銆俰d锛�
	 * @param obj:鍙傛暟鍊�
	 * @param db锛氶渶瑕佹煡璇㈢殑鏁版嵁搴撳悕锛岄粯璁ゆ暟鎹簱浼犵┖鍊�
	 * @return锛氳繑鍥濷bject绫诲瀷
	 */
	public Object selectOneMapByOParameter(String sqlname, Map<String, Object> parameter) {
		Object obj = null;

		Transaction tx = persistence.createTransaction("ERPDB");
		try {
			SqlSession sqlSession = AppBeans.get("sqlSession_ERPDB");
			obj = sqlSession.selectOne(sqlname, parameter);
			tx.commit();

		} catch (NoResultException e) {
			return null;
		} finally {
			tx.end();
		}
		return obj;
	}

	protected Boolean isGongYiSend() {
		return true;
	}

	protected Boolean isGongYiReceive() {
		return true;
	}

	protected Boolean isHuaHuaSend() {
		return true;
	}

	protected Boolean isHuaHuaReceive() {
		return true;
	}

	protected Boolean isDianJiSend() {
		return true;
	}

	protected Boolean isDianJiReceive() {
		return true;
	}

	protected Boolean isTaoKouSend() {
		return true;
	}

	protected Boolean isTaoKouReceive() {
		return true;
	}

	protected Boolean isShouFengSend() {
		return true;
	}

	protected Boolean isShouFengReceive() {
		return true;
	}

	protected Boolean isXiShuiSend() {
		return true;
	}

	protected Boolean isXiShuiReceive() {
		return true;
	}

	protected Boolean isTangYiSend() {
		return true;
	}

	protected Boolean isTangYiReceive() {
		return true;
	}

	protected Boolean isPingCheSend() {
		return true;
	}

	protected Boolean isPingCheReceive() {
		return true;
	}

	protected Alert gongYiSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert gongYiReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert huaHuaSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert huaHuaReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert dianJiSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert dianJiReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert taoKouSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert taoKouReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert shouFengSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert shouFengReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert xiShuiSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert xiShuiReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert tangYiSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert tangYiReceive(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert pingCheSend(SampleOrder sampleOrder) {
		return null;
	}

	protected Alert pingCheReceive(SampleOrder sampleOrder) {
		return null;
	}

}
