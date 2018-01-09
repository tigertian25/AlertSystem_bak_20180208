package com.artwellhk.alertsystem.core;

import com.artwellhk.alertsystem.AlertsystemTestContainer;
import com.artwellhk.alertsystem.AlertsystemTestContainerERPDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.User;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SampleIntegrationTest {

    @ClassRule
    public static AlertsystemTestContainer cont = AlertsystemTestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;
    private DataManager dataManager;
//    @ClassRule
//    public static AlertsystemTestContainerERPDB contERPDB = AlertsystemTestContainerERPDB.Common.INSTANCE;
//
//    private Metadata metadataERPDB;
//    private Persistence persistenceERPDB;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);
        
//        metadataERPDB = contERPDB.metadata();
//        persistenceERPDB = contERPDB.persistence();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoadUser() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<User> query = em.createQuery(
                    "select u from sec$User u where u.login = :userLogin", User.class);
            query.setParameter("userLogin", "admin");
            List<User> users = query.getResultList();
            tx.commit();
            assertEquals(1, users.size());
        }
    }
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    @Test
	public void testMybatis() {
		Transaction tx = persistence.createTransaction();
		 List<Map<String,String>> processList;
		try {
		  SqlSession sqlSession = AppBeans.get("sqlSession");
		  processList=  sqlSession.selectList("ERPDBMapper.getProcess");
		  
		  tx.commit();
		} finally {
		  tx.end();
		}
		  String processListJsonString=gson.toJson(processList);
	        System.out.println(processListJsonString);
	}
//    @Test
//    public void testMybatisERPDB() {
//    	Transaction tx = persistence.createTransaction("ERPDB");
//    	List<Map<String,String>> processList;
//    	try {
//    		SqlSession sqlSession = AppBeans.get("sqlSession");
//    		processList=  sqlSession.selectList("ERPDBMapper.getStyle");
//    		
//    		tx.commit();
//    	} finally {
//    		tx.end();
//    	}
//    	String processListJsonString=gson.toJson(processList);
//    	System.out.println(processListJsonString);
//    }
}