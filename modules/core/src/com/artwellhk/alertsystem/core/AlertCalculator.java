package com.artwellhk.alertsystem.core;

import java.util.List;

public abstract class AlertCalculator {
	public static final String NAME = "AlertCalculator";
	public abstract  List<Alert> calculateAlertList();
	
}
