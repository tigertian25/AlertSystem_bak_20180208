package com.artwellhk.alertsystem.core.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.artwellhk.alertsystem.core.Alert;
import com.artwellhk.alertsystem.core.AlertTypeRetriever;
import com.artwellhk.alertsystem.core.SampleOrder;

@Component(AlertTypeRetriever.NAME)
public class AlertTypeRetrieverImpl extends AlertTypeRetriever {

	@Override
	public List<Alert> retrieveList(List<SampleOrder> SampleOrderList) {
		// TODO Auto-generated method stub
		return null;
	}

}
