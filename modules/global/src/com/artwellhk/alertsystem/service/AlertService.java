package com.artwellhk.alertsystem.service;

import java.util.List;

import com.artwellhk.alertsystem.entity.Alert;

public interface AlertService {
	public static final String NAME = "alertsystem_AlertService";
    public abstract  List<Alert> calculateAlertList();
}