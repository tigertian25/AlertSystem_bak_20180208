package com.artwellhk.alertsystem.core.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artwellhk.alertsystem.AlertsystemTestContainerERPDB;
import com.artwellhk.alertsystem.entity.*;
import com.artwellhk.alertsystem.entity.Process;
import com.artwellhk.alertsystem.service.AlertServiceBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;

public class AlertCalculatorImplTest extends AlertServiceBean {
	
	 @ClassRule
   public static AlertsystemTestContainerERPDB contERPDB = AlertsystemTestContainerERPDB.Common.INSTANCE;
	 private Metadata metadata;
	    private Persistence persistence;
	 @Before
	    public void setUp() throws Exception {
	       
	        metadata = contERPDB.metadata();
	        persistence = contERPDB.persistence();
	    }

	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertCalculatorImplTest.class);
	@Override
	protected boolean isOverTime(){return true;}
	@Override
	protected boolean isSetSnoozeTime(){return true;}
	@Override
	protected boolean isOverSnoozeTime(){return true;}
	@Override
	protected List<Alert> getAlertList(){
		List<Alert> alertList=new ArrayList<Alert>();
		AlertType alertType = new AlertType(1, new Process(2, "工艺发出"), new Process(3, "工艺收回"), 60 * 30,1,2);
		Alert alert = new Alert(alertType, DateUtils.addSeconds(new Date(), -60 * 30 - 20), new SampleOrder(123456, "sp-123456","L123456"),"张三");
		alertList.add(alert);
		return alertList;
	}
	@Test
	public void testCalculateAlertList() {
		 List<Alert> alertList=calculateAlertList();
		 String alertListString = gson.toJson(alertList);
		 log.debug("alertList：" + alertListString);
	}
	@Test
	public void testgetAlertList() {
	List<Alert> alertList = new ArrayList<Alert>();
	List<SampleOrder> sampleOrderList = null;
	//查询所有未完成且工艺发出未收回的版单
	System.out.println("select style star-----"+new Date());
	try (Transaction tx = persistence.createTransaction()){
		SqlSession sqlSession = AppBeans.get("sqlSession");
		sampleOrderList = sqlSession.selectList("ERPDBMapper.getAllStyleOfGOngYiSend");
		tx.commit();
	} catch (NoResultException e) {
		 
	}
	System.out.println("select style end-----"+new Date());
	System.out.println("sampleOrderList"+sampleOrderList);
		
	}
}
