package com.artwellhk.alertsystem.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.BaseIntegerIdEntity;
import javax.persistence.Column;

@Table(name = "ALERTSYSTEM_PROCESS")
@Entity(name = "alertsystem$Process")
public class Process extends BaseIntegerIdEntity {
    private static final long serialVersionUID = -8384761519220970052L;

    @Column(name = "NAME")
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}