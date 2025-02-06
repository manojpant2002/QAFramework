package com.lseg.digital.framework.qa.config;

public enum Environment {
    QA,
    UAT,
    PPE,
    PROD;

    public static Environment fromString(String env) {
        return env != null ? valueOf(env.toUpperCase()) : QA;
    }
} 