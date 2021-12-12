/***
 * Vinay's vision of the elevator arm system:
 * We will have an elevating arm system that will be tilted a bit downward (roughly 30 degrees)
 * The three levels defined are for different heights that the block needs to be released to
 * – we will use a motor to use the arm up/down
 * Once the intake delivers the block to the top of the arm, the block will drop to the end of the arm by gravity,
 * and there there is a stopper that can be lowered using a servo to release the block.
 ***/

package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

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

    public static final double LEVEL_TOP = 0.65;
    public static final double LEVEL_CAROUSEL = 1.115;
    public static final double LEVEL_BOTTOM = 1.786;
    public static final double LEVEL_INTAKE = 2.65;

    // use potentiamotor to detect voltage, then do from there is brian's suggestion

    public void stop() {
        elevatorMotor.setPower(0.0);
    }

    public DcMotor getElevatorMotor() {
        return elevatorMotor;
    }

    public void initMotors() {
        elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevatorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorMotor.setPower(0);

    }

    public ArmSystem(DcMotor elevatorMotor, AnalogInput sensorAsAnalogInput0){
        this.elevatorMotor = elevatorMotor;
        this.sensorAsAnalogInput0 = sensorAsAnalogInput0;
        this.elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.elevatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    //high - 0.65
    //carousel - 1.16
    // bottom - 1.786
    //lolo - 2.65

    public boolean notTooHigh(){
        return (sensorAsAnalogInput0.getVoltage() >= LEVEL_TOP);
    }

    public boolean notTooLow(){
        return (sensorAsAnalogInput0.getVoltage() <= LEVEL_INTAKE);
    }

    public double getSensorAsAnalogInput0() {
        return sensorAsAnalogInput0.getVoltage();
    }

    /**
     * Moves arm up
     */
    public void move_up() {
        if (notTooHigh()){
            elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            elevatorMotor.setPower(1.0);
        }
    }

    /**
     * Moves arm down
     */
    public void move_down() {
        if (notTooLow()){
            elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            elevatorMotor.setPower(-0.75);
        }
    }

    public void moveToPosition(double voltage){
        if (getSensorAsAnalogInput0() < voltage) {
            while (getSensorAsAnalogInput0() < voltage) {
                move_down();
            }
        } else if (getSensorAsAnalogInput0() > voltage) {
            while (getSensorAsAnalogInput0() > voltage) {
                move_up();
            }
        }
        stop();
    }
}