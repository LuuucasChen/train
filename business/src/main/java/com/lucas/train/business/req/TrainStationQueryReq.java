package com.lucas.train.business.req;

import com.lucas.common.req.PageReq;

public class TrainStationQueryReq extends PageReq {

    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "Train_stationQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}
