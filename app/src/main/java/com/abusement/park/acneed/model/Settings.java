package com.abusement.park.acneed.model;

import java.util.Objects;

public class Settings {

    private byte alertFrequency;

    public static final Settings DEFAULT_SETTINGS = new Settings();

    static {
        DEFAULT_SETTINGS.alertFrequency = 7;
    }

    public Settings() {
    }

    public Settings(byte alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public short getAlertFrequency() {
        return alertFrequency;
    }

    public void setAlertFrequency(byte alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Settings that = (Settings) obj;
        return Objects.equals(alertFrequency, that.alertFrequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alertFrequency);
    }
}
