package com.pjm.queueinfo.request;

import java.util.Map;

public class Intent {
    String name;
    Map<String, String> slots;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getSlots() {
        return slots;
    }

    public void setSlots(Map<String, String> slots) {
        this.slots = slots;
    }
}
