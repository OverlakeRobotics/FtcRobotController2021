/***
 * Vinay's vision of the elevator arm system:
 * We will have an elevating arm system that will be tilted a bit downward (roughly 30 degrees)
 * The three levels defined are for different heights that the block needs to be released to
 * – we will use a motor to use the arm up/down
 * Once the intake delivers the block to the top of the arm, the block will drop to the end of the arm by gravity,
 * and there there is a stopper that can be lowered using a servo to release the block.
 ***/

package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 *  one motor - to change elevation
 *  POTENTIAL second motor - to change distance/length arm is reaching out
 *  one Servo - to release stopper.
 */

public class ArmSystem {
    // TODO

    public static AnalogInput sensorAsAnalogInput0;
    private final DcMotor elevatorMotor;
    private boolean bool = false;
//    private final Servo releaser;

    //TODO - TEST + CHANGE THESE NUMBERS
    private static final double CLOSED_POSITION = 0.3;
    private static final double OPEN_POSITION = 0.93;

    public static final int LEVEL_TOP = 277;
    public static final int LEVEL_MID = -225;
    public static final int LEVEL_BOTTOM = -450;
    public static final int LEVEL_CAROUSEL = 0; // to test

    private static int start_position = LEVEL_CAROUSEL;
    // use potentiamotor to detect voltage, then do from there is brian's suggestion

    public void stop() {
        elevatorMotor.setPower(0.0);
    }

    public DcMotor getElevatorMotor() {
        return elevatorMotor;
    }

    public void initMotors() {
        /*while (sensorAsAnalogInput0.getVoltage() != 1.4){

        }*/
        elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorMotor.setPower(0);
//        releaser.setPosition(CLOSED_POSITION);

    }

    public ArmSystem(DcMotor elevatorMotor, AnalogInput sensorAsAnalogInput0){
        this.elevatorMotor = elevatorMotor;
        this.sensorAsAnalogInput0 = sensorAsAnalogInput0;
        // use potentiamotor to detect voltage, then do from there is brian's suggestion
        start_position = elevatorMotor.getCurrentPosition();
        //this.mCalibrationDistance = elevatorMotor.getCurrentPosition();
        this.elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.elevatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        mWaiting = new Deadline(WAIT_TIME, TimeUnit.MILLISECONDS);
//        mTargetHeight = 0;
//        setelevatorMotorHeight(mTargetHeight);
//        movePresetPosition(Position.POSITION_HOME);
//        mCurrentState = ArmState.STATE_CHECK_CLEARANCE;
//        openGripper();
    }

    public boolean inRange(){
        return (sensorAsAnalogInput0.getVoltage() > 2.69 || sensorAsAnalogInput0.getVoltage() < 0.8);
    }


    /*public void goToLevel(int state){


        /*elevatorMotor.setTargetPosition(state);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //is this right? Refer to last year's YeetSystem
        elevatorMotor.setPower(1.0);


        // make sure the arm reaches desired position before exiting this method
        //elevatorMotor.setPower(0);
       
    }*/

    /**
     * Moves arm up
     */
    public void move_up() {
        if(sensorAsAnalogInput0.getVoltage() >= 0.8) {
            elevatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            elevatorMotor.setPower(1.0);
        }
        else{
            telemetry.addData("ARM OUT OF RANGE!!! ", inRange());
            telemetry.update();
        }
    }

    /**
     * Moves arm down
     */
    public void move_down() {
        if(sensorAsAnalogInput0.getVoltage() <= 2.69) {
            elevatorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            elevatorMotor.setPower(-0.75);
        }
        else{
            telemetry.addData("ARM OUT OF RANGE", inRange());
            telemetry.update();
        }
    }
    public void release(boolean bool){
//        if (bool){
//            releaser.setPosition(CLOSED_POSITION);
//        }
//        else{
//            releaser.setPosition(OPEN_POSITION);
//        }
//        long initTime = System.currentTimeMillis();
//        boolean enoughTimeElapsed = false;
//        while (enoughTimeElapsed){
//            if (System.currentTimeMillis() - initTime > 0.55){ /* TODO: FIND TIME TO DELAY/GO DOWN */
//                enoughTimeElapsed = true;
//            }
//            else{
//
//            }
//        }
//        releaser.setPosition(CLOSED_POSITION);
    }

    public void moveTo(int ticks){
        elevatorMotor.setTargetPosition(ticks);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorMotor.setPower(0.75);
    }

    public void moveToPosition(int num){
        moveTo(num);
    }
}

/*
    This class controls everything related to the arm, including driver assist features.
    IMPORTANT: When working on this class (and arm stuff in general),
    keep the servo names consistent: (from closest to the block to farthest)
        - Gripper
        - Wrist
        - Elbow
        - Pivot
 */
//public class ArmSystem {
//
//    public enum Position {
//        // Double values ordered Pivot, elbow, wrist.
//        LEVEL_TOP(-3 + 280),
//        LEVEL_MID(-505 + 280),
//        LEVEL_BOTTOM(-730 + 280),
//        LEVEL_CAROUSEL(0);
//
//        private double height;
//
//        Position(double height) {
//            this.height = height;
//        }
//
//        private double getHeight() {return this.height; }
//    }
//
//    public enum ArmState {
//        STATE_CHECK_CLEARANCE,
//        STATE_CLEAR_CHASSIS,
//        STATE_ADJUST_ORIENTATION,
//        STATE_SETTLE,
//        STATE_RAISE,
//        STATE_LOWER_HEIGHT,
//        STATE_DROP,
//        STATE_WAITING,
//        STATE_OPEN,
//        STATE_CLEAR_TOWER,
//        STATE_HOME,
//        STATE_INITIAL,
//    }
//    public enum ArmDirection {
//        UP, DOWN, IDLE
//    }
//
//    private ArmState mCurrentState;
//    private ArmDirection mDirection;
//
//    // Don't change this unless in calibrate() or init(), is read in the calculateHeight method
//    private int mCalibrationDistance;
//
//    private DcMotor slider;
//
//    // This is in block positions, not ticks
//    public double mTargetHeight;
//    // The queued position
//    private double mQueuePos;
//    // This variable is used for all the auto methods.
//    private Deadline mWaiting;
//
//    private final int MAX_HEIGHT = 6;
//    private final int INCREMENT_HEIGHT = 525; // how much the ticks increase when a block is added
//
//    private final int WAIT_TIME = 400;
//
//    public static final String TAG = "ArmSystem"; // for debugging
//
//    /*
//     If the robot is at the bottom of the screen, and X is the block:
//     XO
//     XO  <--- Position west
//     OO
//     XX  <--- Position south
//     OX
//     OX  <--- Position east
//     XX
//     OO  <--- Position north
//     */
//    public ArmSystem(DcMotor slider) {
//        this.slider = slider;
//        this.mCalibrationDistance = slider.getCurrentPosition();
//        this.slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        this.slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        mWaiting = new Deadline(WAIT_TIME, TimeUnit.MILLISECONDS);
//        mTargetHeight = 0;
//        setSliderHeight(mTargetHeight);
//        movePresetPosition(Position.LEVEL_CAROUSEL);
//        mCurrentState = ArmState.STATE_CHECK_CLEARANCE;
//        openGripper();
//    }
//
//    // Go to "west" position
//    public void moveWest() {
//        movePresetPosition(Position.POSITION_WEST);
//    }
//
//    // Go to "north" position
//    public void moveNorth() {
//        movePresetPosition(Position.POSITION_NORTH);
//    }
//
//    public void moveEast() {
//        movePresetPosition(Position.POSITION_EAST);
//    }
//
//    public void moveSouth() {
//        movePresetPosition(Position.POSITION_SOUTH);
//    }
//
//    // Go to capstone position
//    public boolean moveToCapstone() {
//        return moveInToPosition(Position.POSITION_CAPSTONE);
//    }
//
//    // Helper method for going to capstone or home
//    private boolean moveInToPosition(Position position) {
//        switch(mCurrentState) {
//            case STATE_CHECK_CLEARANCE:
//                ensureIsAboveChassis();
//                break;
//            case STATE_CLEAR_CHASSIS:
//                if (runSliderToTarget()) {
//                    movePresetPosition(position);
//                    mWaiting.reset();
//                    mCurrentState = ArmState.STATE_ADJUST_ORIENTATION;
//                }
//                break;
//            case STATE_ADJUST_ORIENTATION:
//                if(mWaiting.hasExpired()) {
//
//                    setSliderHeight(position.getHeight());
//                    mCurrentState = ArmState.STATE_SETTLE;
//                }
//                break;
//            case STATE_SETTLE:
//                if (runSliderToTarget()) {
//                    mCurrentState = ArmState.STATE_CHECK_CLEARANCE;
//                    return true;
//                }
//                break;
//        }
//        return false;
//    }
//
//    // Auto method for moving out to the queued height and given position
//    public boolean moveOutToPosition(Position position) {
//        switch(mCurrentState) {
//            case STATE_CHECK_CLEARANCE:
//                if (mQueuePos < 2) {
//                    setSliderHeight(2);
//                } else {
//                    setSliderHeight(mQueuePos);
//                }
//                mCurrentState = ArmState.STATE_CLEAR_CHASSIS;
//            case STATE_CLEAR_CHASSIS:
//                if (runSliderToTarget()) {
//                    movePresetPosition(position);
//                    mWaiting.reset();
//                    mCurrentState = ArmState.STATE_ADJUST_ORIENTATION;
//                }
//                break;
//            case STATE_ADJUST_ORIENTATION:
//                if(mWaiting.hasExpired()) {
//                    setSliderHeight(mQueuePos);
//                    mCurrentState = ArmState.STATE_RAISE;
//                }
//                break;
//            case STATE_RAISE:
//                if (runSliderToTarget()) {
//                    Log.d(TAG, "Run");
//                    incrementQueue();
//                    mCurrentState = ArmState.STATE_CHECK_CLEARANCE;
//                    return true;
//                }
//                break;
//        }
//        return false;
//    }
//
//    // Makes sure that the arm is above height 2 in order to clear the chassis
//    private void ensureIsAboveChassis() {
//        if (getSliderPos() < calculateHeight(2)) {
//            setSliderHeight(2);
//        } else {
//            slider.setTargetPosition(slider.getCurrentPosition());
//        }
//        mCurrentState = ArmState.STATE_CLEAR_CHASSIS;
//    }
//
//    // Go to the home position
//    // Moves the slider up to one block high, moves the gripper to the home position, and then moves
//    // back down so we can fit under the bridge.
//    public boolean moveToHome() {
//        return moveInToPosition(Position.POSITION_HOME);
//    }
//
//    public void openGripper() {
//        servoEnumMap.get(ServoNames.GRIPPER).setPosition(GRIPPER_OPEN);
//    }
//
//    public void closeGripper() {
//        servoEnumMap.get(ServoNames.GRIPPER).setPosition(GRIPPER_CLOSE);
//    }
//
//    public void toggleGripper() {
//        if (servoEnumMap.get(ServoNames.GRIPPER).getPosition() == GRIPPER_CLOSE) {
//            openGripper();
//        } else {
//            closeGripper();
//        }
//    }
//
//    // Pos should be the # of blocks high it should be
//    // MUST BE CALLED before runSliderToTarget
//    public void setSliderHeight(double pos) {
//        mTargetHeight = Range.clip(pos, 0, MAX_HEIGHT);
//        setPosTarget();
//        if (slider.getCurrentPosition() == calculateHeight(mTargetHeight)) {
//            mDirection = ArmDirection.IDLE;
//            return;
//        } else if (slider.getCurrentPosition() > calculateHeight(mTargetHeight)) {
//            mDirection = ArmDirection.DOWN;
//        } else {
//            mDirection = ArmDirection.UP;
//        }
//        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//
//    public void setSliderHeight(int pos) {
//        // * 1.0 converts to double
//        setSliderHeight(pos * 1.0);
//    }
//
//    // Little helper method for setSliderHeight
//    private int calculateHeight(double pos){
//        return (int) (pos == 0 ? mCalibrationDistance : mCalibrationDistance + (pos * INCREMENT_HEIGHT));
//    }
//
//    private double reverseCalcHeight(double pos) {
//        return  Math.round(pos/INCREMENT_HEIGHT);
//    }
//
//    // Must be called every loop
//    public boolean runSliderToTarget() {
//        Log.d(TAG, "Direction:" + mDirection);
//        Log.d(TAG, "Curr Pos" + slider.getCurrentPosition());
//        Log.d(TAG, "Target Pos" + slider.getTargetPosition());
//        if (mDirection == ArmDirection.IDLE) {
//            return true;
//        } else {
//            slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//
//        if (mDirection == ArmDirection.UP && slider.getCurrentPosition() <  slider.getTargetPosition()){
//            slider.setPower(1.0);
//        } else if (mDirection == ArmDirection.DOWN && slider.getCurrentPosition() > slider.getTargetPosition()) {
//            slider.setPower(-1.0);
//        } else {
//            mDirection = ArmDirection.IDLE;
//            slider.setTargetPosition(slider.getCurrentPosition());
//            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            return true;
//        }
//        return false;
//    }
//
//    public int getSliderPos() {
//        return slider.getCurrentPosition();
//    }
//
//    private void setPosTarget() {
//        slider.setTargetPosition(calculateHeight(mTargetHeight));
//    }
//
//    public void resetQueue() {
//        mQueuePos = 0;
//    }
//
//    public void incrementQueue() {
//        mQueuePos++;
//        if (mQueuePos > MAX_HEIGHT) {
//            resetQueue();
//        }
//    }
//
//    public void decrementQueue() {
//        mQueuePos = Math.max(0, mQueuePos - 1);
//    }
//
//    public double getQueue() {
//        return mQueuePos;
//    }
//
//    public void changePlaceState(ArmState state) {
//        mCurrentState = state;
//    }
//
//    public void startPlacing() {
//        mCurrentState = ArmState.STATE_DROP;
//    }
//
//    private boolean areRoughlyEqual(int a, int b) {
//        return Math.abs(Math.abs(a) - Math.abs(b)) < 10;
//    }
//
//}

