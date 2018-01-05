package com.artwellhk.alertsystem.core.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.artwellhk.alertsystem.core.SampleOrder;


public class AlertTypeRetrieverImplTest {

	AlertTypeRetrieverImpl alertTypeRetriever;
	 
	/**
	 * 
	 */
	@Test
	public void testRetrieveList() {
		alertTypeRetriever=new AlertTypeRetrieverImpl();
		alertTypeRetriever.isGongYiSend=false;
		List<SampleOrder> sampleOrderList=new ArrayList<SampleOrder>();
		sampleOrderList.add(new SampleOrder(123456, "sp-123456"));
		alertTypeRetriever.retrieveList(sampleOrderList);
	}
	
	
}
