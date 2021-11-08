// TODO - FINALIZE THIS CLASS IN RELATION TO UPDATED PLAN

package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class WheelSystem {

    private final DcMotor wheelToSpin;

    //private final Servo servo;

    // TODO - TEST AND FIND THIS NUMBER
    private static final int ARC_LENGTH = 35;
    private static final double optimalSpinningSpeed = 0.75;
    // this should be the fastest possible speed that we can go without causing the duck to fly off the wheel


    private void initMotors(){
        wheelToSpin.setDirection(DcMotorSimple.Direction.REVERSE);
        wheelToSpin.setPower(0.0);
    }

    public WheelSystem(DcMotor wheelToSpin){
        this.wheelToSpin = wheelToSpin;
    }

    public void spinTheWheelFully(){
        wheelToSpin.setPower(0);
        wheelToSpin.setTargetPosition(wheelToSpin.getCurrentPosition() + ARC_LENGTH);
        wheelToSpin.setMode(DcMotor.RunMode.RUN_TO_POSITION); //is this right? Refer to last year's YeetSystem
        wheelToSpin.setPower(optimalSpinningSpeed);
        //wheelToSpin.setPower(0.0);
    }

    private void delay(){
        long initTime = System.currentTimeMillis();
        boolean enoughTimeElapsed = false;
        while (enoughTimeElapsed){
            if (System.currentTimeMillis() - initTime > 0.55){ /* TODO: FIND TIME FOR FRICTION FORCE TO ACCELERATE VELCOITY TO 0 M/S */
                enoughTimeElapsed = true;
            }
            else{

            }
        }
    }
}