package com.artwellhk.alertsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.artwellhk.alertsystem.entity.ProcessIdEnum;
import com.artwellhk.alertsystem.entity.ProcessType;
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
		List<Alert> alertList =  new LinkedList<Alert>();
		if (sampleOrderList.size() > 0) {
			

			Object obj = null;// 当前工序
			Object objNext = null;// 下一工序
			// 循环sampleOrderList
			Long stratTime =null; 
//			Long endTime = System.currentTimeMillis();
//			int i=1;
			Long stratTime_for = System.currentTimeMillis();
		
			for (SampleOrder sampleOrder : sampleOrderList) {
				
//				stratTime= endTime;
//				endTime= System.currentTimeMillis();
//				
//				System.out.println("第"+i+"次循环耗时："+(endTime - stratTime));
//				i+=1;
				// 当前工序为空
				obj = null;

				Map<String, Object> parameter = new HashMap<>();
				parameter.put("styleID", sampleOrder.getId());

				// 工艺未收回：isReceive=1表示未收回，2表示已收回
				if (sampleOrder.getIsReceive() == 1) {
//					Alert alert = calculatorAlert(sampleOrder, null, AlertTypeID.GongYiNoReceive.getId(), 1);
//					
//					if (null != alert) {
//						alertList.add(alert);
//					}
					AlertType alertType=getAlertType(ProcessIdEnum.GongYi.getId(), ProcessType.send.getId());
					if(null!=alertType) {
						Alert alert = new Alert(alertType, sampleOrder);
						alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
						alert.setFromTimestamp(sampleOrder.getGonYiSendTime());
						alertList.add(alert);
					}
					continue;
				}
				// 判断画花的发出和收回
				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaSend", parameter);
				if (null == objNext) {//画花未发出
//					Alert alert = calculatorAlert(sampleOrder, null, AlertTypeID.HuaHuaNoSend.getId(), 2);
//					if (null != alert) {
//						alertList.add(alert);
//					}
					AlertType alertType=getAlertType(ProcessIdEnum.GongYi.getId(), ProcessType.receive.getId());
					if(null!=alertType) {
						Alert alert = new Alert(alertType, sampleOrder);
						alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
						alert.setFromTimestamp(sampleOrder.getGongYiReceiveTime());
						alertList.add(alert);
					}
					continue;
				}else {//画花已发出
					obj = objNext;//当前工序为画花已发出
					Map<String, Object> huahua=(Map<String, Object>) obj;
					int huahuaIsReceive=Integer.parseInt(huahua.get("isReceive").toString());
					if(huahuaIsReceive==1) {//isReceive=1表示未收回，2表示已收回
//						Alert alert = calculatorAlert(sampleOrder, (Map<String, Object>) obj,
//								AlertTypeID.HuaHuaNoReceive.getId(), 3);
//						if (null != alert) {
//							alertList.add(alert);
//						}
						AlertType alertType=getAlertType(ProcessIdEnum.HuaHua.getId(), ProcessType.send.getId());
						if(null!=alertType) {
							Alert alert = new Alert(alertType, sampleOrder);
							alert = new Alert(alertType, (Date)huahua.get("fromTimestamp"), sampleOrder,
									huahua.get("employeeName").toString());
							alertList.add(alert);
						}
						continue;
					}
					//画花已收回，toTimestamp变成fromTimestamp
//					huahua.put("fromTimestamp", huahua.get("toTimestamp").toString());
//					obj=huahua;
//				}
//				// 鍒ゆ柇鏄惁鐢昏姳鏀跺洖
//				obj = objNext;
//				objNext = selectOneMapByOParameter("ERPDBMapper.getHuaHuaReceive", parameter);
//				if (null == objNext) {
//					Alert alert = calculatorAlert(sampleOrder, (Map<String, Object>) obj,
//							AlertTypeID.HuaHuaNoReceive.getId(), 3);
//					if (null != alert) {
//						alertList.add(alert);
//					}
//					continue;
//				}
				
				/**
				 * 鏌ヨ鐢垫満閮ㄥ強鍚庨潰宸ュ簭鐨勫彂鍑鸿褰�(鏌ヨ鏈�鏂颁竴鏉℃敹鍥炶褰�) 锛宨f(iss.*涓虹┖){琛ㄧず鐢垫満閮ㄦ湭鍙戝嚭}else
				 * if(issbc.aStatus=1){鏄湭鏀跺洖}else if(issbc.aStatus=3){宸叉敹鍥烇紝
				 * if(鏀跺洖鐨勫伐搴忎笉鏄渶鍚庡伐搴�){杩斿洖鏀跺洖鏃堕棿}}
				 */
				//obj = objNext;
				objNext = selectOneMapByOParameter("ERPDBMapper.getIssue60ByStyleID", parameter);// 查询最后收发时间
				if (null == objNext) {// 表示电机未发出
//					Alert alert = calculatorAlert(sampleOrder, (Map<String, Object>) obj,
//							AlertTypeID.DianJiNoSend.getId(), 3);
//					if (null != alert) {
//						alertList.add(alert);
//					}
					AlertType alertType=getAlertType(ProcessIdEnum.HuaHua.getId(), ProcessType.receive.getId());
					if(null!=alertType) {
						Alert alert = new Alert(alertType, sampleOrder);
						alert = new Alert(alertType, (Date) huahua.get("toTimestamp"), sampleOrder,
								huahua.get("employeeName").toString());
						alertList.add(alert);
					}
					continue;
				} else {// 有工序发出
					obj = objNext;
					Map<String, Object> map = (Map<String, Object>) obj;// 当前工序
					Process process = null;
					try (Transaction tx = persistence.createTransaction()) {// 根据deptId查出当前发出工序是什么
						EntityManager em = persistence.getEntityManager();
						process = (Process) em.createQuery(
										"select o from alertsystem$Process o where o.deptId = :deptId")
								.setParameter("deptId", map.get("deptId")).getFirstResult();
						tx.commit();
					} catch (NoResultException e) {
						e.printStackTrace();
						return null;
					}
					if (null != process && "1".equals(map.get("aStatus").toString())) {// aStatus=1当前工序发出未收回

						// 根据当前工序的process.getId()查询AlertType
						AlertType alertType = null;
						try (Transaction tx = persistence.createTransaction()) {
							EntityManager em = persistence.getEntityManager();
							alertType = (AlertType) em.createQuery(
									"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where a.fromProcessType="+ProcessType.send.getId()
											+ " and f.id= :id")
									.setParameter("id", process.getId()).getFirstResult();
							tx.commit();
						} catch (NoResultException e) {
							return null;
						}
						
						if (null != alertType) {
							Alert alert = new Alert(alertType, (Date)map.get("sendTime"),
									sampleOrder, map.get("sendEmployee").toString());
							alertList.add(alert);
						}
						continue;
					}
					if (null != process && "3".equals(map.get("aStatus").toString())) {// aStatus=3当前工序发出并已经收回
						// 查询下一工序
						AlertType alertType = null;
//						try (Transaction tx = persistence.createTransaction()) {
//							EntityManager em = persistence.getEntityManager();
//							alertType = (AlertType) em.createQuery(
//									"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t "
//											+ "  order by a.id desc")
//									.getFirstResult();// 鑾峰彇鏈�鍚庝竴涓猘lertType
//							tx.commit();
//						} catch (NoResultException e) {
//							return null;
//						}
						// 查询下一工序
//						if (null != alertType && alertType.getFromProcess().getId() != process.getId()) {// 琛ㄧず褰撳墠宸ュ簭涓嶆槸鏈�鍚庡伐搴�
						System.out.println(gson.toJson(process));
							try (Transaction tx = persistence.createTransaction()) {
								EntityManager em = persistence.getEntityManager();
								alertType = (AlertType) em.createQuery(
										"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where a.fromProcessType="+ProcessType.receive.getId()
												+ " and f.id= :id order by a.id desc")
										.setParameter("id", process.getId()).getFirstResult();
								tx.commit();
							} catch (NoResultException e) {
								return null;
							}
							if (null != alertType) {//表示有下一工序，即当前工序不是最后工序
								Alert alert = new Alert(alertType, (Date)map.get("receiveTime"),
										sampleOrder, map.get("receiveName").toString());
								alertList.add(alert);
							}
							continue;
//						}
					}
				}
			}
				
			}
			Long endTime_for = System.currentTimeMillis();
			System.out.println("forTime："+(endTime_for - stratTime_for));
			
		}
		//System.out.println("--retrieveList:"+gson.toJson(alertList));
		return alertList;
	}
	private AlertType getAlertType(int fromProcessId,int fromProcessType) {
		AlertType alertType = null;
		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();
			alertType = (AlertType) em.createQuery(
					"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where"
							+ " f.id= :fromProcessId"
							+ " and a.fromProcessType=:fromProcessType"
							)
					.setParameter("fromProcessId", fromProcessId)
					.setParameter("fromProcessType", fromProcessType)
					.getSingleResult();
			tx.commit();
		} catch (NoResultException e) {
			return null;
		}
		return alertType;
		
	}
	

	/*
	 * dataType:1宸ヨ壓鏈敹鍥烇紝2鐢昏姳鏈彂鍑猴紝3鍏朵粬
	 */
	/*private Alert calculatorAlert(SampleOrder sampleOrder, Map<String, Object> map, int alertTypeID, int dataType) {
		
		AlertType alertType = null;
		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();
			alertType = (AlertType) em.createQuery(
					"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t where"
							+ " f.id= :id"
							+ "and fromProcessType="
							)
					.setParameter("id", alertTypeID).getSingleResult();
			tx.commit();
		} catch (NoResultException e) {
			return null;
		}
		
		Alert alert = null;
		if (null != alertType) {
			
			if (dataType == 1) {//工艺未收回
				alert = new Alert(alertType, sampleOrder);
				alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
				alert.setFromTimestamp(sampleOrder.getGonYiSendTime());
			} else if (dataType == 2) {//画花未发出
				alert = new Alert(alertType, sampleOrder);
				alert.setEmployeeName(sampleOrder.getGongYiSendEmpl());
				alert.setFromTimestamp(sampleOrder.getGongYiReceiveTime());
			} 
//			else if (dataType == 3) {//画花未收回
//				alert = new Alert(alertType, sampleOrder);
//				alert.setEmployeeName(map.get("employeeName").toString());
//				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				try {
//					alert.setFromTimestamp(sdf.parse(map.get("fromTimestamp").toString()));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			else {
				alert = new Alert(alertType, util.stringToDate(map.get("fromTimestamp").toString()), sampleOrder,
						map.get("employeeName").toString());
			}
		}
		return alert;

	}*/

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
