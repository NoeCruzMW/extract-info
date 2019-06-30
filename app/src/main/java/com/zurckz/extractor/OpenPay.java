package com.zurckz.extractor;

import android.app.Application;

import mx.openpay.android.Openpay;

public class OpenPay extends Application {

    private final Openpay openpay;

    public OpenPay() {
        this.openpay = new Openpay("mcvtzhbhqd8mxtsjeodd", "pk_fe1b8218a6744ea8a6efa2c579efcc70", false);
    }


    public Openpay getOpenpay() {
        return this.openpay;
    }
}
