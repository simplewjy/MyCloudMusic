package com.aidong.loginshare.bean;

/**
 * @author simple.wu
 * @description
 * @date 2023/2/8
 */
public class UiError {
    public int errorCode;
    public String errorMessage;
    public String errorDetail;

    public UiError(int var1, String var2, String var3) {
        this.errorMessage = var2;
        this.errorCode = var1;
        this.errorDetail = var3;
    }

    public UiError(com.tencent.tauth.UiError error) {
        this.errorMessage = error.errorMessage;
        this.errorCode = error.errorCode;
        this.errorDetail = error.errorDetail;
    }

    public String toString() {
        return "errorCode: " + this.errorCode + ", errorMsg: " + this.errorMessage + ", errorDetail: " + this.errorDetail;
    }
}
