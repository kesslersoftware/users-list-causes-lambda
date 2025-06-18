package com.boycottpro.usercauses.model;

public class CauseSummary {

    private String cause_id;
    private String cause_desc;

    public CauseSummary() {
    }

    public CauseSummary(String cause_id, String cause_desc) {
        this.cause_id = cause_id;
        this.cause_desc = cause_desc;
    }

    public String getCause_id() {
        return cause_id;
    }

    public void setCause_id(String cause_id) {
        this.cause_id = cause_id;
    }

    public String getCause_desc() {
        return cause_desc;
    }

    public void setCause_desc(String cause_desc) {
        this.cause_desc = cause_desc;
    }
}
