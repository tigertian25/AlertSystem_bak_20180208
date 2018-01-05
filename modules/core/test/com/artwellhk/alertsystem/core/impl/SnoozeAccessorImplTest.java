package com.artwellhk.alertsystem.core.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class SnoozeAccessorImplTest {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(SnoozeAccessorImplTest.class);
	
	SnoozeAccessorImpl SnoozeAccessor;
	@Test
	public void testGetSnooze() {
		SnoozeAccessor=new SnoozeAccessorImpl();
		AlertSnooze alertSnooze = SnoozeAccessor.getSnooze(1, 1);
		  String alertSnoozeString=gson.toJson(alertSnooze);
		  log.debug("AlertSnooze查询："+alertSnoozeString);
	}

	@Test
	public void testInsert() {
		SnoozeAccessor=new SnoozeAccessorImpl();
		String result=SnoozeAccessor.insert(1, 1);
		 log.debug("SnoozeAccessor新增："+result);
	}

}
