package com.example.backseatdrivers;

import org.opencv.core.Point;

public class LaneMarker extends Point {
    public boolean v;
    public double w;

    public LaneMarker(double newX, double newY, boolean valid, double weight) {
        x = newX;
        y = newY;
        v = valid;
        w = weight;
    }
}
