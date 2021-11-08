package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.EnumMap;

/***
 *  one motor - to rotate turntable
 *  4 Positions - Top, Right, Back, Left
 */

public class TurnTableSystem {
    // TODO

    private final DcMotorEx rotatorMotor;

    //TODO - TEST + CHANGE THESE NUMBERS
    private static final double CLOSED_POSITION = 0.3;
    private static final double OPEN_POSITION = 0.93;

    private EnumMap <CircleState, Integer> mapToPosition;

    private enum CircleState {
        LEVEL_FORWARD,
        LEVEL_RIGHT,
        LEVEL_BACK,
        LEVEL_LEFT,
    }

    private CircleState currentArmState;

    private void initMotors() {
        rotatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rotatorMotor.setVelocity(0);

    }

    public TurnTableSystem(DcMotorEx rotatorMotor){
        this.rotatorMotor = rotatorMotor;
        mapToPosition.put(CircleState.LEVEL_BACK, 32);
        mapToPosition.put(CircleState.LEVEL_FORWARD, 16);
        mapToPosition.put(CircleState.LEVEL_RIGHT, 8);
        mapToPosition.put(CircleState.LEVEL_LEFT, 4);
    }

    private void goToSetPosition(CircleState state){
        rotatorMotor.setPower(0);
        rotatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //is this right? Refer to last year's YeetSystem
        rotatorMotor.setTargetPosition((this.mapToPosition.get(state)));
        rotatorMotor.setPower(0.75);
        //rotatorMotor.setPower(0);
    }

    private void goToPosition(int pos){
        rotatorMotor.setPower(0);
        rotatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //is this right? Refer to last year's YeetSystem
        rotatorMotor.setTargetPosition(pos);
        rotatorMotor.setPower(0.75);
        //rotatorMotor.setPower(0);
    }

    private void place(ArmSystem.ElevatorState level, CircleState state){
        /*if (bool){
            releaser.setPosition(CLOSED_POSITION);
        }
        else{
            releaser.setPosition(OPEN_POSITION);
        }
        long initTime = System.currentTimeMillis();
        boolean enoughTimeElapsed = false;
        while (enoughTimeElapsed){ */
            /*if (System.currentTimeMillis() - initTime > 0.55){ */ /* TODO: FIND TIME TO DELAY/GO DOWN
                enoughTimeElapsed = true;
            }
            else{

            }
        }
        releaser.setPosition(CLOSED_POSITION);*/
    }

    /*public boolean arm(){
        switch (currentArmState){
            case LEVEL_TOP:
                break;
            case LEVEL_MID:
                break;
            case LEVEL_BOTTOM:
                break;
        }
        return false;
    }*/
}