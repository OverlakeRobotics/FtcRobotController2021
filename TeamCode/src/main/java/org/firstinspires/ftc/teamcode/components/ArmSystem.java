/***
 * Vinay's vision of the elevator arm system:
 * We will have an elevating arm system that will be tilted a bit downward (roughly 30 degrees)
 * The three levels defined are for different heights that the block needs to be released to
 * – we will use a motor to use the arm up/down
 * Once the intake delivers the block to the top of the arm, the block will drop to the end of the arm by gravity,
 * and there there is a stopper that can be lowered using a servo to release the block.
 ***/

package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/***
 *  one motor - to change elevation
 *  POTENTIAL second motor - to change distance/length arm is reaching out
 *  one Servo - to release stopper.
 */

public class ArmSystem {
    // TODO

    private final DcMotor elevatorMotor;
//    private final Servo releaser;

    //TODO - TEST + CHANGE THESE NUMBERS
    private static final double CLOSED_POSITION = 0.3;
    private static final double OPEN_POSITION = 0.93;

    public static final int LEVEL_TOP =  500;
    public static final int LEVEL_MID = 300;
    public static final int LEVEL_BOTTOM = 100;
    public static final int LEVEL_CAROUSEL = 150;
    public static final int LEVEL_DROP = 100;

    private static int start_position;
    // use potentiamotor to detect voltage, then do from there is brian's suggestion

    public void stop() {
        elevatorMotor.setPower(0.0);
    }

    public DcMotor getElevatorMotor() {
        return elevatorMotor;
    }

    public void initMotors() {
        elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorMotor.setPower(0);
//        releaser.setPosition(CLOSED_POSITION);

    }

    public ArmSystem(DcMotor elevatorMotor/*, ?Servo releaser*/){
        this.elevatorMotor = elevatorMotor;
//        this.releaser = releaser;

        // use potentiamotor to detect voltage, then do from there is brian's suggestion
        start_position = elevatorMotor.getCurrentPosition();

    }



    public void goToLevel(int state){
        elevatorMotor.setTargetPosition(state);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //is this right? Refer to last year's YeetSystem
        if(state > elevatorMotor.getCurrentPosition()){
            elevatorMotor.setPower(0.75);
        }
        else{
            elevatorMotor.setPower(-0.75);
        }


        // make sure the arm reaches desired position before exiting this method
        //elevatorMotor.setPower(0);
       
    }

    /**
     * Moves arm up
     */
    public void move_up() {
        elevatorMotor.setTargetPosition(elevatorMotor.getCurrentPosition() + 80);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorMotor.setPower(1.0);
    }

    /**
     * Moves arm down
     */
    public void move_down() {
        elevatorMotor.setTargetPosition(elevatorMotor.getCurrentPosition() - 80);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorMotor.setPower(-1.0);

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
}
