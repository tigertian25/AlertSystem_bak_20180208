package com.artwellhk.alertsystem.core.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SnoozeAccessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlertCalculatorImplTest {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertCalculatorImplTest.class);
	
	AlertCalculatorImpl AlertCalculator;
	List<Alert> alertList;
	 @Before
	 public void setUp() {
		 AlertCalculator=new AlertCalculatorImpl();
		 Alert alert=new Alert();
		 alertList.add(alert);
	 }
	@Test
	public void testCalculateAlertList() {
		AlertCalculator.calculateAlertList();
	}

}
