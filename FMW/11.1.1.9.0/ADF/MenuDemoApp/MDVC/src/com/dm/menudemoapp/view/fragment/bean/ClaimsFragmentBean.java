package com.dm.menudemoapp.view.fragment.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class ClaimsFragmentBean {

    public ClaimsFragmentBean() {
    }

    private String paymentMethod;

    public List getChooseAPaymentMethodOptions() {

        ArrayList chooseAPaymentMethodLOV = new ArrayList();
        
        SelectItem si = new SelectItem();
        si.setLabel("");
        si.setValue(null);
        chooseAPaymentMethodLOV.add(si);

        SelectItem selectItem = new SelectItem();
        selectItem.setLabel("New Credit Card");
        selectItem.setValue("NCC");
        chooseAPaymentMethodLOV.add(selectItem);

        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel("New Bank Account");
        selectItem2.setValue("NBA");
        chooseAPaymentMethodLOV.add(selectItem2);

        return chooseAPaymentMethodLOV;

    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
