package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class WheelSystem {

    private final DcMotor wheelToSpin;


    // TODO - TEST AND FIND THIS NUMBER
    public static final double optimalSpinningSpeed = 0.75;
    // this should be the fastest possible speed that we can go without causing the duck to fly off the wheel (on edge of carousel)


    public void initMotors(){
        wheelToSpin.setDirection(DcMotorSimple.Direction.REVERSE);
        wheelToSpin.setPower(0.0);
    }

    public WheelSystem(DcMotor wheelToSpin){
        this.wheelToSpin = wheelToSpin;
    }

    public void spinWheel(boolean yes){
        if(yes){
            spinTheWheelFully();
        }
        else{
            stopWheel();
        }
    }

    public void spinTheWheelFully(){
        //wheelToSpin.setPower(0);
        //wheelToSpin.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wheelToSpin.setPower(optimalSpinningSpeed);
    }

    public void stopWheel() {
        wheelToSpin.setPower(0.0);
    }

    /*private void delay(){
        long initTime = System.currentTimeMillis();
        boolean enoughTimeElapsed = false;
        while (enoughTimeElapsed){
            if (System.currentTimeMillis() - initTime > 0.55){ /* TODO: FIND TIME FOR FRICTION FORCE TO ACCELERATE VELCOITY TO 0 M/S */ /*
                enoughTimeElapsed = true;
            }
            else{

            }
        }
    }*/
}