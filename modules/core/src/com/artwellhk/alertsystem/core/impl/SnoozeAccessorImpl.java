package com.artwellhk.alertsystem.core.impl;

import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.artwellhk.alertsystem.core.SnoozeAccessor;
import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.artwellhk.alertsystem.entity.AlertType;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

@Component(SnoozeAccessor.NAME)
public class SnoozeAccessorImpl extends SnoozeAccessor {
	@Inject
	private Persistence persistence;

	@Override
	public AlertSnooze getSnooze(int sampleOrderId, int alertTypeId) {
		AlertSnooze alertSnooze = new AlertSnooze();
		/*try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();
			alertSnooze = (AlertSnooze) em
					.createQuery("select a from alertsystem$AlertSnooze a JOIN FETCH a.alertType t where"
							+ " a.sampleOrderId= :sampleOrderId and t.id = :alertTypeId order by a.createTime desc")
					.setParameter("sampleOrderId", sampleOrderId).setParameter("alertTypeId", alertTypeId)
					.getFirstResult();
			tx.commit();
		}*/
		return alertSnooze;
	}

	@Override
	public String insert(int sampleOrderId, int alertTypeId) {
		// TODO Auto-generated method stub
		String result="success";
		return result;
	}

}
