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
			// 存放临时数据

			Object obj = null;// 当前工序
			Object objNext = null;// 下一工序
			// 循环sampleOrderList
			for (SampleOrder sampleOrder : sampleOrderList) {
				// 工艺未发出
				obj = null;

				Map<String, Object> parameter = new HashMap<>();
				// parameter.put("styleID", 531410628);
				parameter.put("styleID", sampleOrder.getId());

				// 工艺未收回
				if (sampleOrder.getIsReceive() == 1) {
					Alert alert = calculatorAlert(sampleOrder, null, AlertTypeID.GongYiNoReceive.getId(), 1);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				}
				// 画花未发出
				// obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaSend", parameter);
				if (null == objNext) {
					Alert alert = calculatorAlert(sampleOrder, null, AlertTypeID.HuaHuaNoSend.getId(), 2);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				}
				// 判断是否画花收回
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
				 * 查询电机部及后面工序的发出记录(查询最新一条收回记录) ，if(iss.*为空){表示电机部未发出}else
				 * if(issbc.aStatus=1){是未收回}else if(issbc.aStatus=3){已收回，
				 * if(收回的工序不是最后工序){返回收回时间}}
				 */
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getIssue60ByStyleID", parameter);// 最近一个发出的工序
				if (null == objNext) {// 表示画花收回后没工序发出即 电机未发出
					Alert alert = calculatorAlert(sampleOrder, (Map<String, Object>) obj,
							AlertTypeID.DianJiNoSend.getId(), 3);
					if (null != alert) {
						alertList.add(alert);
					}
					continue;
				} else {// 最近发出的工序
					obj = objNext;
					Map<String, Object> map = (Map<String, Object>) obj;// 最近发出的工序
					Process process = null;
					try (Transaction tx = persistence.createTransaction()) {// 查询最近发出的工序是什么工序
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
					if (null != process && "1".equals(map.get("aStatus").toString())) {// aStatus=1表示当前工序发出未收回

						// 判断当前工序是什么
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
					if (null != process && "3".equals(map.get("aStatus").toString())) {// aStatus=3表示当前工序已收回，下一工序未发出
						// 判断当前工序是否未最后工序
						AlertType alertType = null;
						try (Transaction tx = persistence.createTransaction()) {
							EntityManager em = persistence.getEntityManager();
							alertType = (AlertType) em.createQuery(
									"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t "
											+ "  order by a.id desc")
									.getFirstResult();// 获取最后一个alertType
							tx.commit();
						} catch (NoResultException e) {
							return null;
						}
						// 比较最后一个alertType的收回工序与当前收回工序是否相等
						if (null != alertType && alertType.getFromProcess().getId() != process.getId()) {// 表示当前工序不是最后工序
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
	 * dataType:1工艺未收回，2画花未发出，3其他
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
			if (dataType == 1) {// 工艺未收回
				alert = new Alert(alertType, sampleOrder);
				alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
				alert.setFromTimestamp(sampleOrder.getGonYiSendTime());
			} else if (dataType == 2) {// 画花未发出
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
	 * @param sqlname:mybatis的Mapper中对应的sql的名称（namespace。id）
	 * @param obj:参数值
	 * @param db：需要查询的数据库名，默认数据库传空值
	 * @return：返回Object类型
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
