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

    public static final int LEVEL_TOP = 200;
    public static final int LEVEL_MID = -200;
    public static final int LEVEL_BOTTOM = -400;
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

    }

    public ArmSystem(DcMotor elevatorMotor, AnalogInput sensorAsAnalogInput0){
        this.elevatorMotor = elevatorMotor;
        this.sensorAsAnalogInput0 = sensorAsAnalogInput0;
        // use potentiamotor to detect voltage, then do from there is brian's suggestion
        start_position = elevatorMotor.getCurrentPosition();
        this.elevatorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.elevatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public boolean inRange(){
        return (sensorAsAnalogInput0.getVoltage() > 2.69 || sensorAsAnalogInput0.getVoltage() < 0.8);
    }

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

    public void moveToPosition(int ticks){
        elevatorMotor.setTargetPosition(ticks);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevatorMotor.setPower(0.25);
    }
}