package com.example.a12652.just_try.Data;

import java.util.ArrayList;

/**
 * Created by 12652 on 2020/1/26.
 */

public class UserInfoData {
    private String usermobile;
    private String password;
    private String depot_name;
    private String depot_address;
    private String depot_manager;
    private int warehouse_num;

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepot_name() {
        return depot_name;
    }

    public void setDepot_name(String depot_name) {
        this.depot_name = depot_name;
    }

    public String getDepot_address() {
        return depot_address;
    }

    public void setDepot_address(String depot_address) {
        this.depot_address = depot_address;
    }

    public String getDepot_manager() {
        return depot_manager;
    }

    public void setDepot_manager(String depot_manager) {
        this.depot_manager = depot_manager;
    }

    public int getWarehouse_num() {
        return warehouse_num;
    }

    public void setWarehouse_num(int warehouse_num) {
        this.warehouse_num = warehouse_num;
    }

    public ArrayList<Integer> getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(ArrayList<Integer> warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    private ArrayList<Integer> warehouse_code;
}
