package com.artwellhk.alertsystem.web.alert;

import java.util.Map;

import javax.inject.Inject;

import com.artwellhk.alertsystem.web.myDatasource.AlertListDatasource;
import com.haulmont.cuba.gui.components.AbstractWindow;
public class AlertBrowse extends AbstractWindow {
	@Inject
    private AlertListDatasource alertsDs;
	@Override
    public void init(Map<String, Object> params) {
//		alertsDs.setMaxResults(50);//一页显示多少条记录
    }
	
}