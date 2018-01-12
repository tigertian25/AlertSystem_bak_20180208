package com.artwellhk.alertsystem.web.alert;

import java.util.Map;

import javax.inject.Inject;

import com.artwellhk.alertsystem.entity.Alert;
import com.artwellhk.alertsystem.web.myDatasource.AlertListDatasource;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.impl.CustomGroupDatasource;

public class AlertBrowse extends AbstractLookup {
	
	@Override
	public void init(Map<String, Object> params) {
		/*GroupDatasource ds = new DsBuilder(getDsContext())
		        .setJavaClass(Alert.class)
		        .setDsClass(AlertListDatasource.class)
		        .setId("alertsDs")
		        .buildGroupDatasource();*/
		new AlertListDatasource();
	}
}