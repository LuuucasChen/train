package com.lucas.train.business.req;

import com.lucas.common.req.PageReq;

public class TrainCarriageQueryReq extends PageReq {

    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "Train_carriageQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}
