package com.example.backseatdrivers;

import org.opencv.core.Point;

public class Lane {
    public Point xL;  // The left side of the lane closest to the vehicle
    public Point xR;  // The right side of the lane closest to the vehicle
    public double percentFromCenter = 0.0;
}
