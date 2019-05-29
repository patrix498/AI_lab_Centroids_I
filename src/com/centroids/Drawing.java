package com.centroids;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Drawing extends JPanel {
    ArrayList<Point> CList = new ArrayList<>();
    ArrayList<Point> PList = new ArrayList<>();


    public static ArrayList<Point> generateNewPoints(int how_much, int h, int w) {
        ArrayList<Point> points = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < how_much; ++i) {
            int x1 = Math.abs(r.nextInt()) % w;
            int y1 = Math.abs(r.nextInt()) % h;
            points.add(new Point(x1, y1));
        }
        return points;
    }

    public static ArrayList<Point> getNewCentroids(ArrayList<Point> centroids, ArrayList<Point> points, int how_much_centroids) {
        if(centroids.size() != 0){
            for (Point centroid:centroids){
                points.add(centroid);
            }
            centroids.clear();
        }
        Random rnd = new Random();
        for (int i = 0; i < how_much_centroids; ++i) {
            int randomEle = rnd.nextInt(points.size());
            float r = rnd.nextFloat();
            float g = rnd.nextFloat();
            float b = rnd.nextFloat();

            points.get(randomEle).setColour(r, g, b);
            centroids.add(points.get(randomEle));
            points.remove(randomEle);
        }
        return centroids;
    }

    public static void groupToCentroids(ArrayList<Point> centroids, ArrayList<Point> points) {
        if (centroids.size() == 1) {
            for (Point p : points) {
                p.setColour(centroids.get(0).getColor());
            }
            return;
        }
        for (Point p : points) {
            double shortestRoute = Math.sqrt((Math.abs(p.X() - centroids.get(0).X())) + (Math.abs(p.Y() - centroids.get(0).Y())));
            p.setColour(centroids.get(0).getColor());
            for (Point centroid : centroids) {
                if (shortestRoute > Math.sqrt((Math.abs(p.X() - centroid.X())) + (Math.abs(p.Y() - centroid.Y())))) {
                    shortestRoute = Math.sqrt((Math.abs(p.X() - centroid.X())) + (Math.abs(p.Y() - centroid.Y())));
                    p.setColour(centroid.getColor());
                }
            }
        }
    }

    public void centralizeCentroid(ArrayList<Point> centroids, ArrayList<Point> points){
        for(Point centroid:centroids){
            double sumX = 0, sumY = 0;
            int numberOfPoints = 0;
            for (Point point:points) {
                if(centroid.getColor() == point.getColor()){
                    sumX += point.X();
                    sumY += point.Y();
                    ++numberOfPoints;
                }
            }
            centroid.setNewPos(sumX/numberOfPoints, sumY/numberOfPoints);
        }
    }

    public void reDraw(ArrayList<Point> centroids, ArrayList<Point> points){
        this.CList = centroids;
        this.PList = points;
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        int height = 10, width = 10;

        drawIn2D(CList, g2d, height, width);
        drawIn2D(PList, g2d, height, width);

        for (Point centroid:CList){
            for (Point point:PList){
                if(centroid.getColor() == point.getColor()){
                    g2d.setColor(centroid.getColor());
                    g2d.drawLine((int)centroid.X(),(int)centroid.Y(),(int)point.X(),(int)point.Y());
                }
            }
        }
    }

    public void drawIn2D(ArrayList<Point> points, Graphics2D g2d, int height, int width) {
        for(Point p:points){
            g2d.setColor(p.getColor());
            g2d.fillOval((int)p.X()-height/2,(int)p.Y()-width/2,height,width);
            g2d.drawOval((int)p.X()-height/2,(int)p.Y()-width/2,height,width);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int height = 1000;
        int width = 1000;
        ArrayList<Point> points = generateNewPoints(5000, height, width);
        ArrayList<Point> centroids = new ArrayList<>();
        centroids = getNewCentroids(centroids, points, 5);
        groupToCentroids(centroids, points);
        Drawing drawing = new Drawing();
        JFrame frame = new JFrame("Centroids");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(drawing);
        drawing.reDraw(centroids,points);
        frame.setSize(height, width);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        while(true) {
            sleep(100);
            drawing.centralizeCentroid(centroids, points);
            drawing.reDraw(centroids, points);
            groupToCentroids(centroids,points);
            sleep(100);
            drawing.reDraw(centroids, points);
        }
    }
}
