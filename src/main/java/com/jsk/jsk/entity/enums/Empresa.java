package com.jsk.jsk.entity.enums;

public enum Empresa {
    JSK, COCA_COLA, NESTLE;
    public String getNombreLegible() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase().replace('_', ' ');
    }
}
