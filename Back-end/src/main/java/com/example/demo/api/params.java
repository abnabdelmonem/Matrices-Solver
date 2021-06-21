package com.example.demo.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class params {
    private double[][] matrix;
    private double[] equal;
    private double[] initial;
    private int opNumber;
    private int luNumber;
    private int precision;
    private int iterations;
    private double error;

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getEqual() {
        return equal;
    }

    public double[] getInitial() {
        return initial;
    }

    public int getOpNumber() {
        return opNumber;
    }

    public int getLuNumber() {
        return luNumber;
    }

    public int getPrecision() {
        return precision;
    }

    public int getIterations() {
        return iterations;
    }

    public double getError() {
        return error;
    }
}

