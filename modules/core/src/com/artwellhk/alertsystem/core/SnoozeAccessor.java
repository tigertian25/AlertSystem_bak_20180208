package com.artwellhk.alertsystem.core;

import com.artwellhk.alertsystem.entity.AlertSnooze;

public abstract class SnoozeAccessor {
	public static final String NAME = "SnoozeAccessor";
	public abstract AlertSnooze getDuration(int sampleOrderId,int alertTypeId);
	public abstract String update(int sampleOrderId,int alertTypeId);
	
}
