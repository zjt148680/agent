package com.speedboot.speedbotagent.vo;

public class EvalTimeCostVO {
    private double averageTimeCost;

    public EvalTimeCostVO(double averageTimeCost) {
        this.averageTimeCost = averageTimeCost;
    }

    public EvalTimeCostVO() {
    }

    public double getAverageTimeCost() {
        return averageTimeCost;
    }

    public void setAverageTimeCost(double averageTimeCost) {
        this.averageTimeCost = averageTimeCost;
    }
}
