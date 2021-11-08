package org.firstinspires.ftc.teamcode.helpers;


/**
 *
 */
public enum Coordinates {
    //TODO check and correct these coordinates
    //TODO See if the webcam will still work if the robot starts pressed against the left or right edge of the starting tile
    // 17 inches is the width of the robot
    STARTING_POSITION(2.5 * Constants.tileWidth, 17),

    // Spline position to the left to avoid rings
    DETOUR_POSITION(2 * Constants.tileWidth, 2 * Constants.tileWidth),

    // 82.25
    // 129.25

    //Calibration
    CALIBRATION(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    // WAREHOUSES
    RED_WAREHOUSE(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    BLUE_WAREHOUSE(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    //ALLIANCE HUB
    RED_ALLIANCE_HUB(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    BLUE_ALLIANCE_HUB(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    SHARED_HUB(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    // Parking position
    RED_PARKING_POSITION(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    BLUE_PARKING_POSITION(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    // Carousel
    CAROUSEL(2.5 * Constants.tileWidth, 3 * Constants.tileWidth);

    private final double x;
    private final double y;

    /**
     * Constructor
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     *
     */

    Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public double getY() {
        return y;
    }
}
