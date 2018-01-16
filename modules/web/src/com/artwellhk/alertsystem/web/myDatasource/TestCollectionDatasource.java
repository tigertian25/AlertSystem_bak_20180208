package com.artwellhk.alertsystem.web.myDatasource;

import com.artwellhk.alertsystem.entity.TestEntity;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;


public class TestCollectionDatasource extends CustomCollectionDatasource<TestEntity, Integer> {

    @Override
    protected Collection<TestEntity> getEntities(Map<String, Object> params) {
    	Collection<TestEntity> c=new ArrayList<TestEntity>();
    	TestEntity t=new TestEntity();
    	t.setName("one");
    	t.setInfo("oneInfo");
    	c.add(t);
    	t.setName("two");
    	t.setInfo("otwoInfo");
    	c.add(t);
        return c;
    }

    /**
     * @return Count of all entities, stored in database
     */
   /* @Override
    public int getCount() {
        return 1;
    }*/
}