package com.example.a12652.just_try.Data;

/**
 * Created by 12652 on 2020/4/7.
 */

public class DevInfo {
    public String getDev_type() {
        return dev_type;
    }

    public void setDev_type(int dev_type) {
        if (dev_type==1){
            this.dev_type = "红外线探针";
        }else{
            this.dev_type = "显微图像探针";
        }

    }

    public String getDev_install_time() {
        return dev_install_time;
    }

    public void setDev_install_time(String dev_install_time) {
        this.dev_install_time = dev_install_time;
    }

    public int getDev_id() {
        return dev_id;
    }

    public void setDev_id(int dev_id) {
        this.dev_id = dev_id;
    }

    public String getDev_status() {
        return dev_status;
    }

    public void setDev_status(int dev_status) {
        if (dev_status==0){
            this.dev_status = "开";
        }else {
            this.dev_status = "关";
        }

    }

    public String dev_type;
    public String dev_install_time;
    public int dev_id;
    public String dev_status;
}
