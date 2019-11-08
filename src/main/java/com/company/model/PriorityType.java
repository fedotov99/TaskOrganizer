package com.company.model;

public enum PriorityType {
    LOW,
    NORMAL,
    HIGH,
    URGENT;

    public static PriorityType getPriorityType(Integer index) {
        return PriorityType.values()[index];
    }
}
