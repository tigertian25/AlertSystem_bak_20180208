package com.artwellhk.alertsystem.service;


import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.artwellhk.alertsystem.entity.AlertSnooze;
import com.artwellhk.alertsystem.entity.AlertType;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;

@Service(SnoozeAccessorService.NAME)
public class SnoozeAccessorServiceBean implements SnoozeAccessorService {
	@Inject
	private Persistence persistence;
	@Inject
	private Metadata metadata;
	@Inject
    private DataManager dataManager;
	

	@Override
	public AlertSnooze getSnooze(int sampleOrderId, int alertTypeId) {
		AlertSnooze alertSnooze = new AlertSnooze();
		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();
			alertSnooze = (AlertSnooze) em
					.createQuery("select a from alertsystem$AlertSnooze a JOIN FETCH a.alertType t where"
							+ " a.sampleOrderId= :sampleOrderId and t.id = :alertTypeId order by a.createTs desc")
					.setParameter("sampleOrderId", sampleOrderId).setParameter("alertTypeId", alertTypeId)
					.getFirstResult();
			tx.commit();
		}
		return alertSnooze;
	}

	@Override
	public String insert(int sampleOrderId, int alertTypeId,int duration) {
		String result="error";
		AlertType alertType = new AlertType();
    	try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();
			alertType = (AlertType) em.createQuery(
					"select distinct a from alertsystem$AlertType a JOIN FETCH a.fromProcess f JOIN FETCH a.toProcess t "
							+ " where a.id=:id order by a.id desc")
					.setParameter("id", alertTypeId)
					.getFirstResult();
			tx.commit();
		} catch (NoResultException e) {
			return null;
		}
    	AlertSnooze alertSnooze = metadata.create(AlertSnooze.class);
		alertSnooze.setAlertType(alertType);
		alertSnooze.setSampleOrderId(sampleOrderId);
		alertSnooze.setDuration(duration);
		dataManager.commit(new CommitContext(alertSnooze));
		result = "succese";
		return result;
	}

}
