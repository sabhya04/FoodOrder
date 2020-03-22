package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum GenericExceptionCode {
    /** These enum constants cover all errors*/
    SGR_001("SGR-001", "This contact number is already registered! Try other contact number."),

    RNF_003("RNF-003", "Restaurant name field should not be empty"),

    CNF_001("CNF-001", "Category id field should not be empty"),

    CNF_002("CNF-002", "No category by this id"),

    RNF_001("RNF-001","No restaurant by this id"),

    RNF_002("RNF-002", "Restaurant id field should not be empty"),

    ATHR_001("ATHR-001", "Customer is not Logged in."),

    ATHR_002("ATHR-002", "Customer is logged out. Log in again to access this endpoint."),

    ATHR_003("ATHR-003", "Your session is expired. Log in again to access this endpoint."),

    IRE_001("IRE-001", "Restaurant should be in the range of 1 to 5");


    private static final Map<String, GenericExceptionCode> Lookup = new HashMap<>();

    static {
        for (GenericExceptionCode exceptionCode : GenericExceptionCode.values()) {
            Lookup.put(exceptionCode.getCode(), exceptionCode);
        }
    }

    private final String code;
    private final String description;

    private GenericExceptionCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GenericExceptionCode getEnum(final String code) {
        return Lookup.get(code);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

