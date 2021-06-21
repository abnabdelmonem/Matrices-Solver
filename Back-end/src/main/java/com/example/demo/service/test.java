package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;

public class test {

    public static void main(String[] args) {
    double[][] a = {
            {0.0D,4.0D,-1.0D},
            {-2.0D,3.0D,1.0D},
            {2.0D,-1.0D,6.0D}
    };

    double [] b = {11.0D,4.0D,7.0D};
    OpClass c = new OpClass();
    ArrayList<ArrayList<Double>> d = c.gaussSeidil(a,b,null,1 , 0.0,0);
    if(d==null)
        System.out.println("ok");

    }
}
