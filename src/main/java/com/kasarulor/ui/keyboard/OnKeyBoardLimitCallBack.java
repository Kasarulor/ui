package com.kasarulor.ui.keyboard;

public interface OnKeyBoardLimitCallBack {
    int limitLength();

    void onResult(String passwordresult, int limitLegth);

    void onResult(String passwordresult, int limitLegth, String other);
}
