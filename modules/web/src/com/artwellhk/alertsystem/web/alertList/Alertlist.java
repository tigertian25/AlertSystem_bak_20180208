package com.artwellhk.alertsystem.web.alertList;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.artwellhk.alertsystem.entity.Alert;
import com.artwellhk.alertsystem.service.AlertService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haulmont.cuba.gui.components.AbstractWindow;

public class Alertlist extends AbstractWindow {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

	@Inject
	private AlertService alertCalculator;

	public void init(Map<String, Object> params) {
		List<Alert> alertList = alertCalculator.calculateAlertList();
		System.out.println(gson.toJson(alertList));
	}
}