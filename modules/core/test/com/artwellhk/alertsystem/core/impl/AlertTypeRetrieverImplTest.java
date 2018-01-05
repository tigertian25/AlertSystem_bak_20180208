package com.artwellhk.alertsystem.core.impl;

import static org.junit.Assert.*;

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
		List<SampleOrder> sampleOrderList=null;
		alertTypeRetriever.retrieveList(sampleOrderList);
	}
	
	/**
	 * 
	 */
	@Test
	public void testRetrieveList2() {
		
	}

}
