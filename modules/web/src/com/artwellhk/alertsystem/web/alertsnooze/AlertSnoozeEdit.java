package com.artwellhk.alertsystem.web.alertsnooze;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Window;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.artwellhk.alertsystem.web.alert.AlertBrowse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlertSnoozeEdit extends AbstractEditor<AlertSnooze> {
	/*Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
	private Logger log = LoggerFactory.getLogger(AlertBrowse.class);
	private Map<String, Object> SnoozeParams=new HashMap<>();
	@Inject
	private AlertSnooze alertSnooze;
	@Inject
	
	@Override
	public void init(Map<String, Object> params) {
		System.out.println(gson.toJson(params.get("ITEM")));
		alertSnooze=(AlertSnooze) params.get("ITEM");
	}
	
	
    public void onEditStatusButtonClick() {
    	//SnoozeParams.get("sampleOrderId");
    	System.out.println(gson.toJson(alertSnooze));
    	 close(Window.COMMIT_ACTION_ID);
    }

    public void onButton_1Click() {
    	 close(Window.CLOSE_ACTION_ID);
    }*/
}