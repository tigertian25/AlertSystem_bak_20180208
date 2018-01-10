package com.artwellhk.alertsystem.core.impl;

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

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SampleOrder;
import com.artwellhk.alertsystem.core.util;
import com.artwellhk.alertsystem.entity.AlertType;
import com.artwellhk.alertsystem.entity.AlertTypeID;
import com.artwellhk.alertsystem.entity.Process;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

@Component(AlertTypeRetriever.NAME)
public class AlertTypeRetrieverImpl implements AlertTypeRetriever {
	@Inject
	private Persistence persistence;

	@Override
	public List<Alert> retrieveList(List<SampleOrder> sampleOrderList) {
		List<Alert> alertList = new ArrayList<Alert>();
		if (sampleOrderList.size() > 0) {
			Map<String, Object> map = new HashMap<>();//存放临时数据
			Map<String, Object> parameter=new HashMap<>();
			
			// 循环sampleOrderList
			for (SampleOrder sampleOrder : sampleOrderList) {

				// 工艺未发出
				map =null;
				parameter=null;
				parameter.put("styleID", sampleOrder.getStyleID());
				map = (Map<String, Object>) selectOneMapByOParameter("ERPDBMapper.getGongYiSend", parameter, "ERPDB");
				if (map.isEmpty() || map == null) {
					alertList.add(calculatorAlert(sampleOrder,map,AlertTypeID.GongYiNoSend.getId()));
					continue;
				}
				// 工艺未收回
				map =null;
				map = (Map<String, Object>) selectOneMapByOParameter("ERPDBMapper.getGongYiReceive", parameter, "ERPDB");
				if (map.isEmpty() || map == null) {
					alertList.add(calculatorAlert(sampleOrder,map,AlertTypeID.GongYiNoReceive.getId()));
					continue;
				}
				// 画花未发出
				map =null;
				map = (Map<String, Object>) selectOneMapByOParameter("ERPDBMapper.getHuaHuaSend", parameter, "ERPDB");
				if (map.isEmpty() || map == null) {
					alertList.add(calculatorAlert(sampleOrder,map,AlertTypeID.HuaHuaNoSend.getId()));
					continue;
				}
				// 判断是否画花收回
				map =null;
				map = (Map<String, Object>) selectOneMapByOParameter("ERPDBMapper.getHuaHuaReceive", parameter, "ERPDB");
				if (map.isEmpty() || map == null) {
					alertList.add(calculatorAlert(sampleOrder,map,AlertTypeID.HuaHuaNoReceive.getId()));
					continue;
				}
				// 判断是否电机发出
				/**查询电机部及后面工序的发出记录(查询最新一条收回记录) ，if(iss.*为空){表示电机部未发出}else if(issbc.aStatus=1){是未收回}else if(issbc.aStatus=3){已收回，
					if(收回的工序不是最后工序){返回收回时间}}*/
				map =null;
				map = (Map<String, Object>) selectOneMapByOParameter("ERPDBMapper.getIssue60ByStyleID", parameter, "ERPDB");
				if (map.isEmpty() || map == null) {//电机未发出
					alertList.add(calculatorAlert(sampleOrder,map,AlertTypeID.DianJiNoSend.getId()));
					continue;
				}else {
					if("1".equals(map.get("aStatus").toString())) {//aStatus=1表示当前工序发出未收回
						//判断当前工序是什么
						parameter.put("ztWorkingId", map.get("sendWorkId"));
						Process process=(Process) selectOneMapByOParameter("ERPDBMapper.getProcess", parameter, null);
						
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
						Alert alert=new Alert(alertType, util.stringToDate(map.get("sendTime").toString()), sampleOrder,map.get("sendEmployee").toString());
						alertList.add(alert);
						continue;
					}
					if("3".equals(map.get("aStatus").toString())) {//aStatus=3表示当前工序已收回，下一工序未发出
						//判断当前工序是否未最后工序
						parameter.put("ztWorkingId", map.get("receiveWorkId"));
						Process process=(Process) selectOneMapByOParameter("ERPDBMapper.getProcess", parameter, null);//收回的工序
						AlertType alertType = new AlertType();
						try (Transaction tx = persistence.createTransaction()) {
							EntityManager em = persistence.getEntityManager();
							alertType = (AlertType) em.createQuery(
									"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t "
											+ "  order by a.id desc")
									.getFirstResult();//获取最后一个alertType
							tx.commit();
						} catch (NoResultException e) {
							return null;
						}
						//比较最后一个alertType的收回工序与当前收回工序是否相等
						if(alertType.getToProcess().getId()!=process.getId()) {//表示当前工序不是最后工序
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
							Alert alert=new Alert(alertType, util.stringToDate(map.get("receiveTime").toString()), sampleOrder,map.get("receiveName").toString());
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

	private Alert calculatorAlert(SampleOrder sampleOrder, Map<String, Object> map,int alertTypeID) {
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
		Alert alert = new Alert(alertType, util.stringToDate(map.get("fromTimestamp").toString()), sampleOrder,map.get("employeeName").toString());
		return alert;
	}

	/**
	 * 
	 * @param sqlname:mybatis的Mapper中对应的sql的名称（namespace。id）
	 * @param obj:参数值
	 * @param db：需要查询的数据库名，默认数据库传空值
	 * @return：返回Map<String,Object>
	 */
	public Object selectOneMapByOParameter(String sqlname, Map<String, Object> parameter, String db) {
		Object obj = null;
		Transaction tx = null;
		if (!db.equals("") && db != null) {
			tx = persistence.createTransaction(db);
		} else {
			tx = persistence.createTransaction();
		}
		try {
			SqlSession sqlSession = AppBeans.get("sqlSession");
			obj  = sqlSession.selectOne(sqlname, parameter);
			tx.commit();
		} catch (NoResultException e) {
			return null;
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
