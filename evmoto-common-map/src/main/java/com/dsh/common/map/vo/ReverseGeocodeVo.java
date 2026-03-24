package com.dsh.common.map.vo;

public class ReverseGeocodeVo {
    private String address;
    private AddressComponentsVo[] addressComponentsVos;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressComponentsVo[] getAddressComponentsVos() {
        return addressComponentsVos;
    }

    public void setAddressComponentsVos(AddressComponentsVo[] addressComponentsVos) {
        this.addressComponentsVos = addressComponentsVos;
    }
}
