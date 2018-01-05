package com.artwellhk.alertsystem.core;

import java.util.List;

public interface  AlertTypeRetriever {
	public static final String NAME = "AlertTypeRetriever";
	//筛选出工序未完成的版单
    public List<Alert> retrieveList(List<SampleOrder> SampleOrderList) ;

}
