
package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * IntakeSystem.java is a component which
 * comprises of two enums in order to convey the state
 * of the system and be manipulated by state machines used in OpModes.
 * It has one motor, which can either be moving (ingesting) - or not.
 * Realize the motor's polarity is determined by set velocity's signum.
 * Understanding this should be pretty straight forward.
 */

// TODO - TEST AND FIND THIS NUMBER
public class IntakeSystem {
    // IntakeState
    private enum IntakeState {
        IDLE,
        TAKE_IN,
        SPIT_OUT,
        CAROUSEL
    }
    private IntakeState currentState;

    private static final double optimalSpinningSpeed = 0.75;
    // Hardware
    private final DcMotor motor1;
    private final DcMotor motor2;

    /**
     * Creates the IntakeSystem Object
     * @param motor1 to represent the motor which rotates in order to s'word the object in.
     */
    public IntakeSystem(DcMotor motor1, DcMotor motor2) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        initMotors();
    }

    /**
     * Initializes the motors
     */
    public void initMotors() {
        currentState = IntakeState.IDLE;
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setPower(0);
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setPower(0);
    }

    /**
     * Intakes rings
     */
    public void take_in() {
            currentState = IntakeState.TAKE_IN;
            motor1.setPower(0.15);
            motor2.setPower(-0.15);
    }

    /**
     * Intakes rings
     */
    public void spit_out() {
            currentState = IntakeState.SPIT_OUT;
            motor1.setPower(-1);
            motor2.setPower(1);
    }

    /**
     * Intakes rings
     */
    public void Carousel() {
        if (currentState != IntakeState.CAROUSEL) {
            currentState = IntakeState.CAROUSEL;
            motor1.setPower(-1 * optimalSpinningSpeed);
            motor2.setPower(-1 * optimalSpinningSpeed);
        }
    }

    public void Carousel(double speed) {
        if (currentState != IntakeState.CAROUSEL) {
            currentState = IntakeState.CAROUSEL;
            motor1.setPower(-1 * speed);
            motor2.setPower(-1 * speed);
        }
    }

    public void setPower(double num) {
        motor1.setPower(num);
        motor2.setPower(num);
    }

    public void setIdle(){
        setPower(0);
    }

    /**
     * Shuts down the motor
     */
    public void stop() {
        if (currentState == IntakeState.TAKE_IN || currentState == IntakeState.SPIT_OUT) {
            currentState = IntakeState.IDLE;
            motor1.setPower(0);
            motor2.setPower(0);
        }
    }


}