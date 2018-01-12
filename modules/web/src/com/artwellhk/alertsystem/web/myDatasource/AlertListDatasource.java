package com.artwellhk.alertsystem.web.myDatasource;

import com.artwellhk.alertsystem.entity.Alert;
import com.artwellhk.alertsystem.service.AlertService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CustomGroupDatasource;

import java.util.Collection;
import java.util.Map;


public class AlertListDatasource extends CustomGroupDatasource<Alert, Integer> {

	 private AlertService someService = AppBeans.get(AlertService.NAME);

	    @Override
	    protected Collection<Alert> getEntities(Map<String, Object> params) {
	        return someService.calculateAlertList();
	    }
}