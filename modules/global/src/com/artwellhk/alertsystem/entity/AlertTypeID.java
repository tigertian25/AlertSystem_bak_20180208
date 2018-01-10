package com.artwellhk.alertsystem.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum AlertTypeID implements EnumClass<Integer> {

    GongYiNoSend(1),
    GongYiNoReceive(2),
    HuaHuaNoSend(3),
    HuaHuaNoReceive(4),
    DianJiNoSend(5),
    DianJiNoReceive(6),
    TaoKouNoSend(7),
    TaoKouNoReceive(8),
    ShouFengNoSend(9),
    ShouFengNoReceive(10),
    XiShuiNoSend(11),
    XiShuiNoReceive(12),
    TangYiNoSend(13),
    TangYiNoReceive(14);

    private Integer id;

    AlertTypeID(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static AlertTypeID fromId(Integer id) {
        for (AlertTypeID at : AlertTypeID.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}