
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
public class IntakeSystem {
    // IntakeState
    private enum IntakeState {
        IDLE,
        TAKE_IN,
        SPIT_OUT
    }
    private IntakeState currentState;

    // Hardware
    private final DcMotor motor;

    /**
     * Creates the IntakeSystem Object
     * @param motor to represent the motor which rotates in order to s'word the object in.
     */
    public IntakeSystem(DcMotor motor) {
        this.motor = motor;
        initMotors();
    }

    /**
     * Initializes the motors
     */
    public void initMotors() {
        currentState = IntakeState.IDLE;
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setPower(0);
    }

    /**
     * Intakes rings
     */
    public void take_in() {
        if (currentState != IntakeState.TAKE_IN) {
            currentState = IntakeState.TAKE_IN;
            motor.setPower(1);
        }
    }

    /**
     * Intakes rings
     */
    public void spit_out() {
        if (currentState != IntakeState.SPIT_OUT) {
            currentState = IntakeState.SPIT_OUT;
            motor.setPower(-1);
        }
    }

    /**
     * Intakes rings
     */
    public void Carousel() {
        if (currentState != IntakeState.SPIT_OUT) {
            currentState = IntakeState.SPIT_OUT;
            motor.setPower(-1 * WheelSystem.optimalSpinningSpeed);
        }
    }

    /**
     * Shuts down the motor
     */
    public void stop() {
        if (currentState == IntakeState.TAKE_IN) {
            currentState = IntakeState.IDLE;
            motor.setPower(0);
        }
    }
}