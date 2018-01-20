package com.artwellhk.alertsystem.web.alerttype;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.artwellhk.alertsystem.entity.AlertType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haulmont.cuba.gui.components.EntityCombinedScreen;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class AlertTypeBrowse extends EntityCombinedScreen {
	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertTypeBrowse.class);
	@Inject
	private GroupDatasource<AlertType,Integer> alertTypesDs;

	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		for (AlertType alertType: alertTypesDs.getItems()) {
			System.out.println("getAllowedDuration:"+alertType.getAllowedDuration());
		}
	}
}