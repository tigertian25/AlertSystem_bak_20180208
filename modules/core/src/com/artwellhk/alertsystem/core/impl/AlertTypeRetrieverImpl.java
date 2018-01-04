package com.artwellhk.alertsystem.core.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SampleOrder;
import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.artwellhk.alertsystem.entity.AlertType;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

@Component(AlertTypeRetriever.NAME)
public class AlertTypeRetrieverImpl extends AlertTypeRetriever {

	@Inject
	private Persistence persistence;

	@Override
	public List<Alert> retrieveList(List<SampleOrder> sampleOrderList) {
		List<Alert> alertList=null;
		//循环sampleOrderList
		for (SampleOrder sampleOrder : sampleOrderList) {
			//判断是否工艺发出
			if (!isGongYiSend()) {
				alertList.add(ProcessUndone(sampleOrder,1));
				continue;
			}
			//判断是否工艺收回
			if(!isGongYiReceive()) {
				alertList.add(ProcessUndone(sampleOrder,11));
				continue;
			}
			//判断是否画花发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,2));
				continue;
			}
			//判断是否画花收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,22));
				continue;
			}
			//判断是否电机发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,3));
				continue;
			}
			//判断是否电机收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,33));
				continue;
			}
			//判断是否套口发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,4));
				continue;
			}
			//判断是否套口收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,44));
				continue;
			}
			//判断是否手缝发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,5));
				continue;
			}
			//判断是否手缝收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,55));
				continue;
			}
			//判断是否洗水发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,6));
				continue;
			}
			//判断是否洗水收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,66));
				continue;
			}
			//判断是否烫衣发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,7));
				continue;
			}
			//判断是否烫衣收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,77));
				continue;
			}
			//判断是否平车发出
			if(!isHuaHuaSend()) {
				alertList.add(ProcessUndone(sampleOrder,8));
				continue;
			}
			//判断是否平车收回
			if(!isHuaHuaReceive()) {
				alertList.add(ProcessUndone(sampleOrder,88));
				continue;
			}
			
			
		}
			
		return null;
	}
	
	protected Boolean isGongYiSend() {return false;}
	protected Boolean isGongYiReceive() {return false;}
	
	protected Boolean isHuaHuaSend() {return false;}
	protected Boolean isHuaHuaReceive() {return false;}
	
	protected Boolean isDianJiSend() {return false;}
	protected Boolean isDianJiReceive() {return false;}
	
	protected Boolean isTaoKouSend() {return false;}
	protected Boolean isTaoKouReceive() {return false;}
	
	protected Boolean isShouFengSend() {return false;}
	protected Boolean isShouFengReceive() {return false;}
	
	protected Boolean isXiShuiSend() {return false;}
	protected Boolean isXiShuiReceive() {return false;}
	
	protected Boolean isTangYiSend() {return false;}
	protected Boolean isTangYiReceive() {return false;}
	
	protected Boolean isPingCheSend() {return false;}
	protected Boolean isPingCheReceive() {return false;}
	
	protected Alert ProcessUndone(SampleOrder sampleOrder,int type) {
		Alert alert=null;
		switch (type) {
		case 1:
			alert.setSampleOrder(sampleOrder);
			alert.setAlertType(new AlertType());
			break;
		case 11:
			
			break;
		case 2:
			
			break;
		case 22:
			
			break;
		case 3:
			
			break;
		case 33:
			
			break;
		case 4:
			
			break;
		case 44:
			
			break;
		case 5:
			
			break;
		case 55:
			
			break;
		case 6:
			
			break;
		case 66:
			
			break;
		case 7:
			
			break;
		case 77:
			
			break;
		case 8:
			
			break;
		case 88:
			
			break;
		default:
			break;
		}
		return alert;
	}
	
	
	
	
	
}
