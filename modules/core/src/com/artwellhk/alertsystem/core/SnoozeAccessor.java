package com.artwellhk.alertsystem.core;

import com.artwellhk.alertsystem.entity.AlertSnooze;

public abstract class SnoozeAccessor {
	public static final String NAME = "SnoozeAccessor";
	public abstract AlertSnooze getSnooze(int sampleOrderId,int alertTypeId);
	public abstract String insert(int sampleOrderId,int alertTypeId,int duration);
	
}
