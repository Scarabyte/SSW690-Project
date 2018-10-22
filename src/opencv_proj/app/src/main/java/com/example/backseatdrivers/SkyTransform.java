package com.example.backseatdrivers;

import org.opencv.core.Mat;

import java.io.Serializable;

public class SkyTransform implements Serializable {

    Mat m;
    double imgWidth;

    SkyTransform() {
        imgWidth = 0;
    }
}
