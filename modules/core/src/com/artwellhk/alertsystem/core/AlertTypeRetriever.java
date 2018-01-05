package com.artwellhk.alertsystem.core;

import java.util.List;

public abstract class AlertTypeRetriever {
	public static final String NAME = "AlertTypeRetriever";
	//筛选出工序未完成的版单
    public abstract List<Alert> retrieveList(List<SampleOrder> SampleOrderList) ;

}
