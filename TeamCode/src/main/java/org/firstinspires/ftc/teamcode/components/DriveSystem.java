package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.util.Range;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveSystem {

    //Constants
    private static final double MAIN_SPEED_COEFFICIENT = 1;
    private static final double SLOW_DRIVE_SPEED_COEFFICIENT = 0.25;

    //Motors
    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackRight;
    private DcMotor motorBackLeft;

    //IMUs (for Gyros)
    private BNO055IMU IMUSystemOne;
    private BNO055IMU IMUSystemTwo; // Should be used for redundancy but not enabled as of present

    public enum Direction {
        FORWARD, BACKWARD, LEFT, RIGHT;

        private static Direction getDirection(Direction direction) {
            if (direction == FORWARD){

            }
            if (direction == BACKWARD){

            }
            if (direction == LEFT){

            }
            if (direction == RIGHT){

            }
            return direction;
        }
    }

    //Other "settings"
    public boolean slowDriveMode = false;

    //Coordinates/Location
    // TODO - Set these equal to the actual coordinates relative to the field
    private double x_coordinate;
    private double y_coordinate;
    private double driver_x_coordinate;
    private double driver_y_coordinate;

    /**
     * Initializes the DriveSystem
     */
    public DriveSystem(DcMotor motorFrontRight, DcMotor motorFrontLeft, DcMotor motorBackRight, DcMotor motorBackLeft) {
        this.motorFrontRight = motorFrontRight;
        this.motorFrontLeft = motorFrontLeft;
        this.motorBackRight = motorBackRight;
        this.motorBackLeft = motorBackLeft;
    }

    public void initMotors(boolean bool) {
        this.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        this.motorBackLeft.setPower(0);
        this.motorBackRight.setPower(0);
        this.motorFrontLeft.setPower(0);
        this.motorFrontRight.setPower(0);
    }

    /**
     * Sets the motor's power, taking speed into account.
     * @param motor The motor that will have a change in power
     * @param power The speed of the motor, from -1 to 1
     */
    private void setMotorPower (DcMotor motor, double power) {
        motor.setPower(MAIN_SPEED_COEFFICIENT * (slowDriveMode ? SLOW_DRIVE_SPEED_COEFFICIENT * power : power));
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

    //Todo Add functionality for gyro assisted strafe

    //Todo God mode
    //Drive with GodMode Enabled
    //Angles are relative to the vertical axis, with 90 being right and -90 being left
    private void godDrive (float rx, float lx, float ly) {
        double gyroAngle = Math.atan2(y_coordinate - driver_y_coordinate, x_coordinate - driver_x_coordinate);
        double joystickAngle = Math.atan(lx/ly);
        double angleToDrive = joystickAngle - gyroAngle;

        double vectorDriveMagnitude = Math.sqrt(ly*ly+lx*lx);

        float horizontalVectorDrive = (float) vectorDriveMagnitude * (float) Math.sin(angleToDrive);
        float verticalVectorDrive = (float) vectorDriveMagnitude * (float)  Math.cos(angleToDrive);

        joystickDrive(rx, horizontalVectorDrive, verticalVectorDrive);

    }

    /* Old Code from TankDrive
    //Constants
    private static final String[] MOTOR_LEFT_NAME = {"treadLeftRear", "treadLeftFront"};
    private static final String[] MOTOR_RIGHT_NAME = {"treadRightRear", "treadLeftRear"};
    private static final double MAIN_SPEED_COEFFICIENT = 1.0;
    private static final double SLOWDRIVE_SPEED_COEFFICIENT = 0.25;

    //Motors use arrays because we do not know if the robot will have one or two
    private DcMotor[] motorsTreadLeft = new DcMotor[MOTOR_LEFT_NAME.length];
    private DcMotor[] motorsTreadRight = new DcMotor[MOTOR_RIGHT_NAME.length];

    public DriveSystem() {
        for (int i = 0; i < MOTOR_LEFT_NAME.length; i++) {
             motorsTreadLeft[i] = hardwareMap.get(DcMotor.class, MOTOR_LEFT_NAME[i]);
        }
        for (int i = 0; i < MOTOR_RIGHT_NAME.length; i++) {
            motorsTreadLeft[i] = hardwareMap.get(DcMotor.class, MOTOR_RIGHT_NAME[i]);
        }
    }

    //Moves all motors in the array with the specified value.
    private void driveAllMotors(DcMotor[] motorSet, double speed) {
        for (DcMotor motor : motorSet) {
            motor.setPower(speed);
        }
    }

    private void tankDrive(double leftPower, double rightPower) {
        driveAllMotors(motorsTreadLeft, leftPower);
        driveAllMotors(motorsTreadRight, rightPower);
    }*/
}