package com.artwellhk.alertsystem.core;

import java.util.List;

public abstract class AlertTypeRetriever {
	public static final String NAME = "AlertTypeRetriever";
	//ɸѡ������ʱ�İ浥
    public abstract List<Alert> retrieveList(List<SampleOrder> SampleOrderList) ;

}
