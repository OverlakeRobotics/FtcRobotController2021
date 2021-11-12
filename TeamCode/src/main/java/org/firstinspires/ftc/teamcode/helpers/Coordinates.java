package org.firstinspires.ftc.teamcode.helpers;


/**
 *
 */
public enum Coordinates {
    //TODO check and correct these coordinates
    //TODO See if the webcam will still work if the robot starts pressed against the left or right edge of the starting tile
    // 17 inches is the width of the robot
    STARTING_POSITION(2.5 * Constants.tileWidth, 17),

    //FOR RED TOP
    RED_STARTING_POSITION_TOP((3 * Constants.tileWidth) - 8.5, (0.5 * Constants.tileWidth)),
    RED_TOP_CENTERBARCODE(2 * Constants.tileWidth,(0.5 * Constants.tileWidth) ),
    RED_TOP_LEFTBARCODE(2 * Constants.tileWidth, 3),



    //FOR RED BOTTOM
    RED_STARTING_POSITION_BOTTOM((3 * Constants.tileWidth) - 8.5, (-1.5 * Constants.tileWidth)),
    RED_BOTTOM_CENTERBARCODE(2 * Constants.tileWidth,(-1.5 * Constants.tileWidth) ),
    RED_BOTTOM_RIGHTBARCODE(2 * Constants.tileWidth, -((1 * Constants.tileWidth) + 3)),


    //FOR BLUE TOP
    BLUE_STARTING_POSITION_TOP((-3 * Constants.tileWidth) + 8.5, (0.5 * Constants.tileWidth)),
    BLUE_TOP_CENTERBARCODE(2 * Constants.tileWidth,(-1.5 * Constants.tileWidth) ),
    BLUE_TOP_RIGHTBARCODE(2 * Constants.tileWidth, 3),

    //FOR BLUE BOTTOM
    BLUE_STARTING_POSITION_BOTTOM((-3 * Constants.tileWidth) + 8.5, (-1.5 * Constants.tileWidth)),
    BLUE_BOTTOM_CENTERBARCODE(-2 * Constants.tileWidth,(-1.5 * Constants.tileWidth) ),
    BLUE_BOTTOM_LEFTBARCODE(-2 * Constants.tileWidth, -((1 * Constants.tileWidth) + 3)),










    // 82.25
    // 129.25

    //Calibration
    CALIBRATION(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    // WAREHOUSES DONE!
    RED_WAREHOUSE((3 * Constants.tileWidth) - (8.5), 1.7 * Constants.tileWidth),
    BLUE_WAREHOUSE((-3 * Constants.tileWidth) + (8.5), 1.7 * Constants.tileWidth),

    //ALLIANCE HUB
    RED_ALLIANCE_HUB(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    BLUE_ALLIANCE_HUB(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    SHARED_HUB(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    // Parking position
    RED_PARKING_POSITION(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    BLUE_PARKING_POSITION(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),

    // Carousel LOOK AT FIELD
    RED_CAROUSEL(2.5 * Constants.tileWidth, 3 * Constants.tileWidth),
    BLUE_CAROUSEL(1,2),
    //Storage Units DONE!
    BLUE_STORAGE_UNIT(-1.5 * Constants.tileWidth, -2.5 * Constants.tileWidth),
    RED_STORAGE_UNIT(1.5 * Constants.tileWidth, -2.5 * Constants.tileWidth);


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
