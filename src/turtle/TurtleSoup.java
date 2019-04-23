/* Copyright (c) 2007-2014 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	turtle.forward(20);
    	turtle.turn(90);
    	turtle.forward(20);
    	turtle.turn(90);
    	turtle.forward(20);
    	turtle.turn(90);
    	turtle.forward(20);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        assert(sides > 2);
        return((180 * ((double) sides - 2)) / (double) sides);
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return((int) Math.round(360 / Math.abs(angle - 180)));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        assert(sides >= 3);
        for (int i=0; i < sides; i++) {
        	turtle.forward(sideLength);
        	turtle.turn(180-calculateRegularPolygonAngle(sides));
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX currentY current location
     * @param targetX targetY target point
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360.
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        int xDiff = targetX - currentX;
        int yDiff = targetY - currentY;
        double newAngle = 0;
        if (yDiff == 0) {
        	if (xDiff > 0) {
        		newAngle = 90;
        	} else if (xDiff < 0) {
        		newAngle = 270;
        	}
        } else {
        	if (xDiff > 0 && yDiff > 0)
        		newAngle = Math.toDegrees(Math.atan(xDiff / yDiff));
        	else if (xDiff < 0 && yDiff > 0)
        		newAngle = 360 - Math.atan(Math.abs(xDiff) / yDiff);
        	else if (xDiff > 0 && yDiff < 0)
        		newAngle = 180 - Math.atan(Math.abs(xDiff) / yDiff);
        	else if (xDiff < 0 && yDiff < 0)
        		newAngle = 180 + Math.atan(Math.abs(xDiff) / yDiff);
        	else if (xDiff == 0) {
        		if (yDiff > 0)
        			newAngle = 0;
        		else
        			newAngle = 180;
        	}
        }
        
        double angleChange = 0;
        if (newAngle < currentHeading)
        	angleChange = 360 - (currentHeading - newAngle);
        else if (newAngle > currentHeading)
        	angleChange = newAngle - currentHeading;
        
        return Math.round(angleChange);
        
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size (# of points) - 1.
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> returnData = new ArrayList<Double>();
        assert(xCoords.size() == yCoords.size());
        double currentHeading = 0.0;
        for(int i=0; i < xCoords.size()-1; i++) {
        	currentHeading = calculateHeadingToPoint(currentHeading,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1)); 
        	returnData.add(currentHeading);
        	
        }
        return returnData;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * We'll be peer-voting on the different images, and the highest-rated one will win a prize. 
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        List<Integer> xCoords = new ArrayList<Integer>();
        List<Integer> yCoords = new ArrayList<Integer>();
        
        for (int i = 0; i < 5000; i=i+10) {
        	xCoords.add(i*2);
        	yCoords.add(i);
        }
        
        List<Double> headings = calculateHeadings(xCoords, yCoords);
        
        int currentX = 0;
        int currentY = 0;
        for (int i=0; i < xCoords.size()-1; i++) {
        	turtle.forward((int) Math.round(Math.sqrt((xCoords.get(i) - currentX) + (yCoords.get(i) - currentY))));
        	turtle.turn(headings.get(i));
        }
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        drawPersonalArt(turtle);

        // draw the window
        turtle.draw();
    }

}
