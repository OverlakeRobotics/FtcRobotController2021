package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.AutonomousOpMode;

public class DriveSystem {

    //Constants
    public static final double MAIN_SPEED_COEFFICIENT = 1;
    public static final double SLOW_DRIVE_SPEED_COEFFICIENT = 0.25;
    public static final double MIN_SPEED = 0.37;
    public static final double STRAFE_COEFF = 0.09;
    public static final String TAG = "DriveSystem";
    public static final double P_TURN_COEFF = 0.012;     // Larger is more responsive, but also less stable
    public static final double HEADING_THRESHOLD = 1 ;      // As tight as we can make it with an integer gyro

    //Motors
    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    public ImuSystem imuSystem;

    private int mTargetTicks;
    private double mTargetHeading;
    public boolean mSlowDrive;


    //IMUs (for Gyros)
    private BNO055IMU IMUSystemOne;
    private BNO055IMU IMUSystemTwo; // Should be used for redundancy but not enabled as of present

    public enum Direction {
        FORWARD, BACKWARD, LEFT, RIGHT;

        /*private static Direction getDirection(Direction direction) {
            if (direction == FORWARD){

            }
            if (direction == BACKWARD){

            }
            if (direction == LEFT){

            }
            if (direction == RIGHT){

            }
            return direction;
        }*/
    }

    //Other "settings"
    public boolean slowDriveMode = false;

    //Coordinates/Location
    // TODO - Set these equal to the actual coordinates relative to the field
//    public double x_coordinate;
//    public double y_coordinate;
//    public double driver_x_coordinate;
//    public double driver_y_coordinate;

    /**
     * Initializes the DriveSystem
     */
    public DriveSystem(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft, ImuSystem imuSystem) {
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
        this.imuSystem = imuSystem;
    }

    public void initMotors() {
        this.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorBackLeft.setPower(0);
        this.motorBackRight.setPower(0);
        this.motorFrontLeft.setPower(0);
        this.motorFrontRight.setPower(0);
    }

    /**
     * Turns the robot by a given number of degrees
     * @param degrees The degrees to turn the robot by
     * @param maxPower The maximum power of the motors
     */
    public boolean turn(double degrees, double maxPower) {
        // Since controller hub is vertical, use pitch instead of heading
        double heading = imuSystem.getHeading();
        // if controller hub is flat: double heading = imuSystem.getHeading();
        if(mTargetHeading == 0) {
            mTargetHeading = (heading + degrees) % 360;
            Log.d(TAG, "Setting Heading -- Target: " + mTargetHeading);

            Log.d(TAG, "Degrees: " + degrees);
        }
        double difference = mTargetHeading - heading;
        Log.d(TAG,"Difference: " + difference);
        return onHeading(maxPower, heading);

    }


    private double computeDegreesDiff() {
        double diff = mTargetHeading - imuSystem.getHeading();
        return Math.abs(diff) == 180 ? diff : diff % 180;
    }

    public boolean onHeading(double speed, double heading) {
        double leftSpeed;

        // determine turn power based on +/- error
        double error = computeDegreesDiff();

        // If it gets there: stop
        if (Math.abs(error) <= HEADING_THRESHOLD) {
            mTargetHeading = 0;
            setAllMotorPower(0.0);
            return true;
        }

        // Go full speed until 60% there
        leftSpeed = Math.abs(error) / 125.0;

        Log.d(TAG, "Left Speed: " + leftSpeed);
        leftSpeed = Range.clip(leftSpeed, MIN_SPEED, 1.0);

        // Send desired speeds to motors.
        tankDrive(leftSpeed * Math.signum(error), -leftSpeed * Math.signum(error));
        return false;
    }
    

    private void tankDrive(double leftPower, double rightPower) {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(leftPower);
        motorBackRight.setPower(rightPower);
        motorBackLeft.setPower(0);
    }
    /**
     * Sets the motor's power, taking speed into account.
     * @param motor The motor that will have a change in power
     * @param power The speed of the motor, from -1 to 1
     */
    private void setMotorPower (DcMotor motor, double power) {
        motor.setPower(MAIN_SPEED_COEFFICIENT * (slowDriveMode ? SLOW_DRIVE_SPEED_COEFFICIENT * power : power));
    }

    public void setAllMotorPower(double power) {
        motorFrontLeft.setPower(MAIN_SPEED_COEFFICIENT * (slowDriveMode ? SLOW_DRIVE_SPEED_COEFFICIENT * power : power));
        motorFrontRight.setPower(MAIN_SPEED_COEFFICIENT * (slowDriveMode ? SLOW_DRIVE_SPEED_COEFFICIENT * power : power));
        motorBackLeft.setPower(MAIN_SPEED_COEFFICIENT * (slowDriveMode ? SLOW_DRIVE_SPEED_COEFFICIENT * power : power));
        motorBackRight.setPower(MAIN_SPEED_COEFFICIENT * (slowDriveMode ? SLOW_DRIVE_SPEED_COEFFICIENT * power : power));
    }

    /**
     * Turn joystick input into motor movement.
     * @param rx The x of the right joystick, from -1 to 1
     * @param lx The x of the left joystick, from -1 to 1
     * @param ly The y of the left joystick, from -1 to 1
     */
    public void joystickDrive (float rx, float lx, float ly) {
        if (Math.abs(rx) < 0.01) {
            rx = 0;
        }
        if (Math.abs(ly) < 0.01) {
            ly = 0;
        }
        if (Math.abs(lx) < 0.01) {
            lx = 0;
        }

        //Powers assume robot forward is forward for motor as well
        setMotorPower(motorBackLeft,  lx + ly - rx);
        setMotorPower(motorFrontLeft, - lx + ly - rx);
        setMotorPower(motorBackRight, (- lx + ly + rx) * -1);
        setMotorPower(motorFrontRight, (lx + ly + rx) * -1);
    }

    public int getTicks() {
        int num = 0;
        num += motorBackLeft.getCurrentPosition() + motorBackRight.getCurrentPosition() + motorFrontLeft.getCurrentPosition() + motorFrontRight.getCurrentPosition();
        return num;
    }

    public void driveTicks(int ticks) {
        motorFrontLeft.setTargetPosition(motorFrontLeft.getTargetPosition() + ticks);
        motorFrontRight.setTargetPosition(motorFrontRight.getTargetPosition() + ticks);
        motorBackLeft.setTargetPosition(motorBackLeft.getTargetPosition() + ticks);
        motorBackRight.setTargetPosition(motorBackRight.getTargetPosition() + ticks);
    }

    public static double[] TimeCoordinate(Coordinates robotCoordinate, Coordinates newCoordinate) {
        double deltaX = newCoordinate.getX() - robotCoordinate.getX();
        double deltaY = newCoordinate.getY() - robotCoordinate.getY();
        double Xtime = (deltaX/Constants.tileWidth) * Constants.FULL_POWER_TILE_TIME;
        double Ytime = (deltaY/Constants.tileWidth) * Constants.FULL_POWER_TILE_TIME;
        double xMagnitude = deltaX/Math.abs(deltaX); //goes in drivesystem params
        double yMagnitude = deltaY/Math.abs(deltaY); // goes in drivesystem params
        return new double[]{Xtime, Ytime, xMagnitude, yMagnitude};
    }

    public static double[] TicksCoordinate(Coordinates robotCoordinate, Coordinates newCoordinate) {
        double deltaX = newCoordinate.getX() - robotCoordinate.getX();
        double deltaY = newCoordinate.getY() - robotCoordinate.getY();
        double XTicks = (deltaX/Constants.tileWidth) * Constants.TICKS_IN_TILE;
        double YTicks = (deltaY/Constants.tileWidth) * Constants.TICKS_IN_TILE;
        return new double[]{XTicks, YTicks};
    }


    //Todo Add functionality for gyro assisted strafe

    //Todo God mode
    //Drive with GodMode Enabled
    //Angles are relative to the vertical axis, with 90 being right and -90 being left
//    private void godDrive (float rx, float lx, float ly) {
//        double gyroAngle = Math.atan2(y_coordinate - driver_y_coordinate, x_coordinate - driver_x_coordinate);
//        double joystickAngle = Math.atan(lx/ly);
//        double angleToDrive = joystickAngle - gyroAngle;
//
//        double vectorDriveMagnitude = Math.sqrt(ly*ly+lx*lx);
//
//        float horizontalVectorDrive = (float) vectorDriveMagnitude * (float) Math.sin(angleToDrive);
//        float verticalVectorDrive = (float) vectorDriveMagnitude * (float)  Math.cos(angleToDrive);
//
//        joystickDrive(rx, horizontalVectorDrive, verticalVectorDrive);
//
//    }
}