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
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.EnumMap;

/***
 *  one motor - to change elevation
 *  POTENTIAL second motor - to change distance/length arm is reaching out
 *  one Servo - to release stopper.
 */

public class ArmSystem {
    // TODO

    private final DcMotorEx elevatorMotor;
    private final Servo releaser;

    //TODO - TEST + CHANGE THESE NUMBERS
    private static final double CLOSED_POSITION = 0.3;
    private static final double OPEN_POSITION = 0.93;

    private EnumMap <ElevatorState, Integer> mapToPosition;

    public enum ElevatorState {
        LEVEL_TOP,
        LEVEL_MID,
        LEVEL_BOTTOM
    }

    private ElevatorState currentArmState;

    public void initMotors() {
        elevatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorMotor.setVelocity(0);
        releaser.setPosition(CLOSED_POSITION);

    }

    public ArmSystem(DcMotorEx elevatorMotor, Servo releaser){
        this.elevatorMotor = elevatorMotor;
        this.releaser = releaser;
        mapToPosition.put(ElevatorState.LEVEL_TOP, 32);
        mapToPosition.put(ElevatorState.LEVEL_MID, 16);
        mapToPosition.put(ElevatorState.LEVEL_BOTTOM, 8);
    }

    public void goToLevel(ElevatorState state){
        releaser.setPosition(CLOSED_POSITION);
        elevatorMotor.setPower(0);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //is this right? Refer to last year's YeetSystem
        elevatorMotor.setTargetPosition((mapToPosition.get(state)));
        elevatorMotor.setPower(0.75);
        //elevatorMotor.setPower(0);
    }

    public void release(boolean bool){
        if (bool){
            releaser.setPosition(CLOSED_POSITION);
        }
        else{
            releaser.setPosition(OPEN_POSITION);
        }
        long initTime = System.currentTimeMillis();
        boolean enoughTimeElapsed = false;
        while (enoughTimeElapsed){
            if (System.currentTimeMillis() - initTime > 0.55){ /* TODO: FIND TIME TO DELAY/GO DOWN */
                enoughTimeElapsed = true;
            }
            else{

            }
        }
        releaser.setPosition(CLOSED_POSITION);
    }

    public boolean arm(){
        switch (currentArmState){
            case LEVEL_TOP:
                break;
            case LEVEL_MID:
                break;
            case LEVEL_BOTTOM:
                break;
        }
        return false;
    }
}