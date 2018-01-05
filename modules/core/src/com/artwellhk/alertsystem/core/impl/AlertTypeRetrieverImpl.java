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
public class AlertTypeRetrieverImpl implements AlertTypeRetriever {


	@Override
	public List<Alert> retrieveList(List<SampleOrder> sampleOrderList) {
		List<Alert> alertList=new ArrayList<Alert>();
		if(sampleOrderList.size()>0) {
		//循环sampleOrderList
		for (SampleOrder sampleOrder : sampleOrderList) {
			
			//判断是否工艺发出
			if (!isGongYiSend()) {
				alertList.add(gongYiSend(sampleOrder));
				continue;
			}
			//判断是否工艺收回
			if(!isGongYiReceive()) {
				alertList.add(gongYiReceive(sampleOrder));
				continue;
			}
			//判断是否画花发出
			if(!isHuaHuaSend()) {
				alertList.add(huaHuaSend(sampleOrder));
				continue;
			}
			//判断是否画花收回
			if(!isHuaHuaReceive()) {
				alertList.add(huaHuaReceive(sampleOrder));
				continue;
			}
			//判断是否电机发出
			if(!isDianJiSend()) {
				alertList.add(dianJiSend(sampleOrder));
				continue;
			}
			//判断是否电机收回
			if(!isDianJiReceive()) {
				alertList.add(dianJiReceive(sampleOrder));
				continue;
			}
			//判断是否套口发出
			if(!isTaoKouSend()) {
				alertList.add(taoKouSend(sampleOrder));
				continue;
			}
			//判断是否套口收回
			if(!isTaoKouReceive()) {
				alertList.add(taoKouReceive(sampleOrder));
				continue;
			}
			//判断是否手缝发出
			if(!isShouFengSend()) {
				alertList.add(shouFengSend(sampleOrder));
				continue;
			}
			//判断是否手缝收回
			if(!isShouFengReceive()) {
				alertList.add(shouFengReceive(sampleOrder));
				continue;
			}
			//判断是否洗水发出
			if(!isXiShuiSend()) {
				alertList.add(xiShuiSend(sampleOrder));
				continue;
			}
			//判断是否洗水收回
			if(!isXiShuiReceive()) {
				alertList.add(xiShuiReceive(sampleOrder));
				continue;
			}
			//判断是否烫衣发出
			if(!isTangYiSend()) {
				alertList.add(tangYiSend(sampleOrder));
				continue;
			}
			//判断是否烫衣收回
			if(!isTangYiReceive()) {
				alertList.add(tangYiReceive(sampleOrder));
				continue;
			}
			//判断是否平车发出
			if(!isPingCheSend()) {
				alertList.add(pingCheSend(sampleOrder));
				continue;
			}
			//判断是否平车收回
			if(!isPingCheReceive()) {
				alertList.add(pingCheReceive(sampleOrder));
				continue;
			}
			
			
		}
		}
		return alertList;
	}
	
	protected Boolean isGongYiSend() {return true;}
	protected Boolean isGongYiReceive() {return true;}
	
	protected Boolean isHuaHuaSend() {return true;}
	protected Boolean isHuaHuaReceive() {return true;}

	protected Boolean isDianJiSend() {return true;}
	protected Boolean isDianJiReceive() {return true;}
	
	protected Boolean isTaoKouSend() {return true;}
	protected Boolean isTaoKouReceive() {return true;}
	
	protected Boolean isShouFengSend() {return true;}
	protected Boolean isShouFengReceive() {return true;}
	
	protected Boolean isXiShuiSend() {return true;}
	protected Boolean isXiShuiReceive() {return true;}
	
	protected Boolean isTangYiSend() {return true;}
	protected Boolean isTangYiReceive() {return true;}
	
	protected Boolean isPingCheSend() {return true;}
	protected Boolean isPingCheReceive() {return true;}
	

	protected Alert gongYiSend(SampleOrder sampleOrder){return null;}
	protected Alert gongYiReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert huaHuaSend(SampleOrder sampleOrder){return null;}
	protected Alert huaHuaReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert dianJiSend(SampleOrder sampleOrder){return null;}
	protected Alert dianJiReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert taoKouSend(SampleOrder sampleOrder){return null;}
	protected Alert taoKouReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert shouFengSend(SampleOrder sampleOrder){return null;}
	protected Alert shouFengReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert xiShuiSend(SampleOrder sampleOrder){return null;}
	protected Alert xiShuiReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert tangYiSend(SampleOrder sampleOrder){return null;}
	protected Alert tangYiReceive(SampleOrder sampleOrder){return null;}
	
	protected Alert pingCheSend(SampleOrder sampleOrder){return null;}
	protected Alert pingCheReceive(SampleOrder sampleOrder){return null;}
			
		
	
	
	
	
}
