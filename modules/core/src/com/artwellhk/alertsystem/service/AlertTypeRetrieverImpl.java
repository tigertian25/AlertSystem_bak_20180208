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

@Component(AlertTypeRetriever.NAME)
public class AlertTypeRetrieverImpl implements AlertTypeRetriever {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	@Inject
	private Persistence persistence;

	@Override
	public List<Alert> retrieveList(List<SampleOrder> sampleOrderList) {
		List<Alert> alertList = new ArrayList<Alert>();
		if (sampleOrderList.size() > 0) {
			// 存放临时数据
			Map<String, Object> parameter = new HashMap<>();
			Object obj = null;// 当前工序
			Object objNext = null;// 下一工序
			// 循环sampleOrderList
			for (SampleOrder sampleOrder : sampleOrderList) {
				// 工艺未发出
				obj = null;

				parameter.clear();
//				parameter.put("styleID", 531410628);
				parameter.put("styleID", sampleOrder.getId());

				objNext = selectOneMapByOParameter("ERPDBMapper.getGongYiSend", parameter);
				if (objNext == null) {
					alertList.add(calculatorAlert(sampleOrder, null, AlertTypeID.GongYiNoSend.getId()));
					continue;
				}
				// 工艺未收回
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getGongYiReceive", parameter);
				if (objNext == null) {
					alertList.add(calculatorAlert(sampleOrder, (Map<String, Object>) obj,
							AlertTypeID.GongYiNoReceive.getId()));
					continue;
				}
				// 画花未发出
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaSend", parameter);
				if (objNext == null) {
					alertList.add(
							calculatorAlert(sampleOrder, (Map<String, Object>) obj, AlertTypeID.HuaHuaNoSend.getId()));
					continue;
				}
				// 判断是否画花收回
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaReceive", parameter);
				if (objNext == null) {
					alertList.add(calculatorAlert(sampleOrder, (Map<String, Object>) obj,
							AlertTypeID.HuaHuaNoReceive.getId()));
					continue;
				}
				// 判断是否电机发出
				/**
				 * 查询电机部及后面工序的发出记录(查询最新一条收回记录) ，if(iss.*为空){表示电机部未发出}else
				 * if(issbc.aStatus=1){是未收回}else if(issbc.aStatus=3){已收回，
				 * if(收回的工序不是最后工序){返回收回时间}}
				 */
				obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getIssue60ByStyleID", parameter);
				if (objNext == null) {// 电机未发出
					alertList.add(
							calculatorAlert(sampleOrder, (Map<String, Object>) obj, AlertTypeID.DianJiNoSend.getId()));
					continue;
				} else {
					Map<String, Object> map = (Map<String, Object>) obj;// 当前工序
					Process process = new Process();
					try (Transaction tx = persistence.createTransaction()) {
						EntityManager em = persistence.getEntityManager();
						process = (Process) em.createQuery(
										"select o from alertsystem$Process o where o.zt_working_id = :zt_working_id")
								.setParameter("zt_working_id", map.get("sendWorkId")).getSingleResult();
						tx.commit();
					} catch (NoResultException e) {
						e.printStackTrace();
						return null;
					}
					if ("1".equals(map.get("aStatus").toString())) {// aStatus=1表示当前工序发出未收回

						// 判断当前工序是什么
						AlertType alertType = new AlertType();
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
						Alert alert = new Alert(alertType, util.stringToDate(map.get("sendTime").toString()),
								sampleOrder, map.get("sendEmployee").toString());
						alertList.add(alert);
						continue;
					}
					if ("3".equals(map.get("aStatus").toString())) {// aStatus=3表示当前工序已收回，下一工序未发出
						// 判断当前工序是否未最后工序
						AlertType alertType = new AlertType();
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
						if (alertType.getFromProcess().getId() != process.getId()) {// 表示当前工序不是最后工序
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
							Alert alert = new Alert(alertType, util.stringToDate(map.get("receiveTime").toString()),
									sampleOrder, map.get("receiveName").toString());
							alertList.add(alert);
						}
					}
				}
				// 判断是否电机收回
				if (!isDianJiReceive()) {
					alertList.add(dianJiReceive(sampleOrder));
					continue;
				}
				// 判断是否套口发出
				if (!isTaoKouSend()) {
					alertList.add(taoKouSend(sampleOrder));
					continue;
				}
				// 判断是否套口收回
				if (!isTaoKouReceive()) {
					alertList.add(taoKouReceive(sampleOrder));
					continue;
				}
				// 判断是否手缝发出
				if (!isShouFengSend()) {
					alertList.add(shouFengSend(sampleOrder));
					continue;
				}
				// 判断是否手缝收回
				if (!isShouFengReceive()) {
					alertList.add(shouFengReceive(sampleOrder));
					continue;
				}
				// 判断是否洗水发出
				if (!isXiShuiSend()) {
					alertList.add(xiShuiSend(sampleOrder));
					continue;
				}
				// 判断是否洗水收回
				if (!isXiShuiReceive()) {
					alertList.add(xiShuiReceive(sampleOrder));
					continue;
				}
				// 判断是否烫衣发出
				if (!isTangYiSend()) {
					alertList.add(tangYiSend(sampleOrder));
					continue;
				}
				// 判断是否烫衣收回
				if (!isTangYiReceive()) {
					alertList.add(tangYiReceive(sampleOrder));
					continue;
				}
				// 判断是否平车发出
				if (!isPingCheSend()) {
					alertList.add(pingCheSend(sampleOrder));
					continue;
				}
				// 判断是否平车收回
				if (!isPingCheReceive()) {
					alertList.add(pingCheReceive(sampleOrder));
					continue;
				}
			}
		}
		
		return alertList;
	}

	private Alert calculatorAlert(SampleOrder sampleOrder, Map<String, Object> map, int alertTypeID) {
		AlertType alertType = new AlertType();
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
		Alert alert = new Alert();
		if (map != null) {
			alert = new Alert(alertType, util.stringToDate(map.get("fromTimestamp").toString()), sampleOrder,
					map.get("employeeName").toString());
		}else {
			alert = new Alert(alertType, sampleOrder);
		}
		//System.out.println(gson.toJson(alert));
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
			SqlSession sqlSession = AppBeans.get("sqlSession");
			obj = sqlSession.selectOne(sqlname, parameter);
			tx.commit();

		} catch (NoResultException e) {
			return null;
		}finally {
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
