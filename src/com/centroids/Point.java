package com.centroids;

import java.awt.*;

public class Point {
    private double x = 0;
    private double y = 0;
    private Color color = Color.red;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setColour(float r, float g, float b){
        this.color = new Color(r,g,b);
    }
    public void setColour(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public void setNewPos(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double X(){
        return x;
    }
    public double Y(){
        return y;
    }
}