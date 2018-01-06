package com.artwellhk.alertsystem.core.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.SampleOrder;
import com.artwellhk.alertsystem.entity.AlertType;
import com.artwellhk.alertsystem.entity.Process;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlertTypeRetrieverImplTest extends AlertTypeRetrieverImpl {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertTypeRetrieverImplTest.class);

	/**
	 * 工艺未发出
	 */

	@Override
	protected Boolean isGongYiSend() {
		return false;
	}

	@Override
	protected Alert gongYiSend(SampleOrder sampleOrder) {
		AlertType alertType = new AlertType(1, new Process(1, "板房"), new Process(2, "工艺发出"), 60 * 30);
		Alert alert = new Alert(alertType, DateUtils.addSeconds(new Date(), -60 * 30 - 20), sampleOrder);

		return alert;
	}

	@Test
	public void testRetrieveList_isGongYiSend() {

		List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
		sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
		List<Alert> alertList = retrieveList(sampleOrderList);

		String alertListString = gson.toJson(alertList);
		log.debug("工艺未发出：" + alertListString);

	}

//	/**
//	 * 工艺未收回
//	 */
//
//	@Override
//	protected Boolean isGongYiReceive() {
//		return false;
//	}
//
//	@Override
//	protected Alert gongYiReceive(SampleOrder sampleOrder) {
//		AlertType alertType = new AlertType(1, new Process(2, "工艺发出"), new Process(3, "工艺收回"), 60 * 30);
//		Alert alert = new Alert(alertType, DateUtils.addSeconds(new Date(), -60 * 30 - 20), sampleOrder);
//
//		return alert;
//	}
//
//	@Test
//	public void testRetrieveList_isGongYiReceive() {
//
//		List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
//		sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
//		List<Alert> alertList = retrieveList(sampleOrderList);
//
//		String alertListString = gson.toJson(alertList);
//		log.debug("工艺未收回：" + alertListString);
//	}

	// /**
	// * 画花未发出
	// */
	// @Test
	// public void testRetrieveList_isHuaHuaSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("画花未发出：" + alertListString);
	// }
	//
	// /**
	// * 画花未收回
	// */
	// @Test
	// public void testRetrieveList_isHuaHuaReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("画花未收回：" + alertListString);
	// }
	//
	// /**
	// * 电机未发出
	// */
	// @Test
	// public void testRetrieveList_isDianJiSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("电机未发出：" + alertListString);
	// }
	//
	// /**
	// * 电机未收回
	// */
	// @Test
	// public void testRetrieveList_isDianJiReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("电机未收回：" + alertListString);
	// }
	//
	// /**
	// * 套口未发出
	// */
	// @Test
	// public void testRetrieveList_isTaoKouSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("套口未发出：" + alertListString);
	// }
	//
	// /**
	// * 套口未收回
	// */
	// @Test
	// public void testRetrieveList_isTaoKouReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("套口未收回：" + alertListString);
	// }
	//
	// /**
	// * 手缝未发出
	// */
	// @Test
	// public void testRetrieveList_isShouFengSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("手缝未发出：" + alertListString);
	// }
	//
	// /**
	// * 手缝未收回
	// */
	// @Test
	// public void testRetrieveList_isShouFengReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("手缝未收回：" + alertListString);
	// }
	//
	// /**
	// * 洗水未发出
	// */
	// @Test
	// public void testRetrieveList_isXiShuiSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("洗水未发出：" + alertListString);
	// }
	//
	// /**
	// * 洗水未收回
	// */
	// @Test
	// public void testRetrieveList_isXiShuiReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("洗水未收回：" + alertListString);
	// }
	//
	// /**
	// * 烫衣未发出
	// */
	// @Test
	// public void testRetrieveList_isTangYiSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("烫衣未发出：" + alertListString);
	// }
	//
	// /**
	// * 烫衣未收回
	// */
	// @Test
	// public void testRetrieveList_isTangYiReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("烫衣未收回：" + alertListString);
	// }
	//
	// /**
	// * 平车未发出
	// */
	// @Test
	// public void testRetrieveList_isPingCheSend() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("平车未发出：" + alertListString);
	// }
	//
	// /**
	// * 平车未收回
	// */
	// @Test
	// public void testRetrieveList_isPingCheReceive() {
	//
	// List<SampleOrder> sampleOrderList = new ArrayList<SampleOrder>();
	// sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
	// List<Alert> alertList = retrieveList(sampleOrderList);
	//
	// String alertListString = gson.toJson(alertList);
	// log.debug("平车未收回：" + alertListString);
	// }

}
