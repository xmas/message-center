package com.xmas.service.notifiers.chrome;

import java.util.List;

@SuppressWarnings("unused")
public class ChromeMessage {

    private List<String> registration_ids;

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }
}
