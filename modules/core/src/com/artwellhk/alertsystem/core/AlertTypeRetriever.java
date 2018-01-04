package com.artwellhk.alertsystem.core;

import java.util.List;

public abstract class AlertTypeRetriever {
	public static final String NAME = "AlertTypeRetriever";
	//筛选出工序超时的版单
    public abstract List<Alert> retrieveList(List<SampleOrder> SampleOrderList) ;

}
