package com.artwellhk.alertsystem.core.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SampleOrder;
import com.artwellhk.alertsystem.entity.AlertType;
import com.artwellhk.alertsystem.entity.Process;

@Component(AlertTypeRetriever.NAME)
public class AlertTypeRetrieverImpl extends AlertTypeRetriever {


	@Override
	public List<Alert> retrieveList(List<SampleOrder> sampleOrderList) {
		List<Alert> alertList=new ArrayList<Alert>();
		if(sampleOrderList.size()>0) {
		//循环sampleOrderList
		for (SampleOrder sampleOrder : sampleOrderList) {
			
			//判断是否工艺发出
			if (!isGongYiSend) {
				alertList.add(ProcessUndone(sampleOrder,1));
				continue;
			}
			//判断是否工艺收回
			if(!isGongYiReceive) {
				alertList.add(ProcessUndone(sampleOrder,11));
				continue;
			}
			//判断是否画花发出
			if(!isHuaHuaSend) {
				alertList.add(ProcessUndone(sampleOrder,2));
				continue;
			}
			//判断是否画花收回
			if(!isHuaHuaReceive) {
				alertList.add(ProcessUndone(sampleOrder,22));
				continue;
			}
			//判断是否电机发出
			if(!isDianJiSend) {
				alertList.add(ProcessUndone(sampleOrder,3));
				continue;
			}
			//判断是否电机收回
			if(!isDianJiReceive) {
				alertList.add(ProcessUndone(sampleOrder,33));
				continue;
			}
			//判断是否套口发出
			if(!isTaoKouSend) {
				alertList.add(ProcessUndone(sampleOrder,4));
				continue;
			}
			//判断是否套口收回
			if(!isTaoKouReceive) {
				alertList.add(ProcessUndone(sampleOrder,44));
				continue;
			}
			//判断是否手缝发出
			if(!isHuaHuaSend) {
				alertList.add(ProcessUndone(sampleOrder,5));
				continue;
			}
			//判断是否手缝收回
			if(!isShouFengReceive) {
				alertList.add(ProcessUndone(sampleOrder,55));
				continue;
			}
			//判断是否洗水发出
			if(!isXiShuiSend) {
				alertList.add(ProcessUndone(sampleOrder,6));
				continue;
			}
			//判断是否洗水收回
			if(!isXiShuiReceive) {
				alertList.add(ProcessUndone(sampleOrder,66));
				continue;
			}
			//判断是否烫衣发出
			if(!isTangYiSend) {
				alertList.add(ProcessUndone(sampleOrder,7));
				continue;
			}
			//判断是否烫衣收回
			if(!isTangYiReceive) {
				alertList.add(ProcessUndone(sampleOrder,77));
				continue;
			}
			//判断是否平车发出
			if(!isPingCheSend) {
				alertList.add(ProcessUndone(sampleOrder,8));
				continue;
			}
			//判断是否平车收回
			if(!isPingCheReceive) {
				alertList.add(ProcessUndone(sampleOrder,88));
				continue;
			}
			
			
		}
		}
		return alertList;
	}
	
	protected Boolean isGongYiSend=false;
	protected Boolean isGongYiReceive=false;
	
	protected Boolean isHuaHuaSend=false;
	protected Boolean isHuaHuaReceive=false;

	protected Boolean isDianJiSend=false;
	protected Boolean isDianJiReceive=false;
	
	protected Boolean isTaoKouSend=false;
	protected Boolean isTaoKouReceive=false;
	
	protected Boolean isShouFengSend=false;
	protected Boolean isShouFengReceive=false;
	
	protected Boolean isXiShuiSend=false;
	protected Boolean isXiShuiReceive=false;
	
	protected Boolean isTangYiSend=false;
	protected Boolean isTangYiReceive=false;
	
	protected Boolean isPingCheSend=false;
	protected Boolean isPingCheReceive=false;
	
	protected Alert ProcessUndone(SampleOrder sampleOrder,int type) {
		Alert alert=new Alert();
		switch (type) {
		case 1:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(10, "板房"), new Process(1, "工艺发出"), 60*20));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*20-10));
			break;
		case 11:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(1, "工艺发出"), new Process(11, "工艺收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 2:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(11, "工艺收回"), new Process(2, "画花发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 22:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(2, "画花发出"), new Process(22, "画花收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 3:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(22, "画花收回"), new Process(3, "电机发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 33:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(3, "电机发出"), new Process(33, "电机收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 4:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(33, "电机收回"), new Process(4, "套口发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 44:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(44, "套口发出"), new Process(5, "套口收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 5:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(5, "套口收回"), new Process(55, "手缝发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 55:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(55, "手缝发出"), new Process(6, "手缝收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 6:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(6, "手缝收回"), new Process(66, "洗水发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 66:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(66, "洗水发出"), new Process(7, "洗水收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 7:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(7, "洗水收回"), new Process(77, "烫衣发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 77:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(77, "烫衣发出"), new Process(8, "烫衣收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 8:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(8, "烫衣收回"), new Process(88, "平车发出"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		case 88:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType(1, new Process(88, "平车发出"), new Process(9, "平车收回"), 60*30));
			alert.setFromTimestamp(DateUtils.addSeconds(new Date(), -60*30-15));
			break;
		default:
			break;
		}
		return alert;
	}
	
	
	
	
	
}
