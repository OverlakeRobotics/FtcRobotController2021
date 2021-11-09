package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.components.WeightSystem;
import org.firstinspires.ftc.teamcode.components.WheelSystem;

import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

public abstract class BaseOpMode extends OpMode {

    // Variables
    protected boolean trajectoryFinished;
    protected Coordinates currentPosition;

    // Systems
    protected DriveSystem driveSystem;
    protected Vuforia vuforia;
    protected TurnTableSystem turnTableSystem;
    protected ArmSystem armSystem;
    protected ColorSensor colorSensor;
    protected IntakeSystem intakeSystem;

    @Override
    public void init() {
        // Variables
        this.msStuckDetectInit = 20000;
        this.msStuckDetectInitLoop = 20000;
        currentPosition = (Coordinates.STARTING_POSITION);

        // Systems
        vuforia = Vuforia.getInstance();
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, "IntakeSystem"));
        driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, "motor_one"), hardwareMap.get(DcMotor.class, "motor_two"), hardwareMap.get(DcMotor.class, "motor_three"), hardwareMap.get(DcMotor.class, "motor_four"));
        driveSystem.driver_x_coordinate = Coordinates.STARTING_POSITION.getX();
        driveSystem.driver_y_coordinate = Coordinates.STARTING_POSITION.getY();
        turnTableSystem = new TurnTableSystem((hardwareMap.get(DcMotorEx.class, "TurnTableSystem")));
        armSystem = new ArmSystem(hardwareMap.get(DcMotorEx.class, "LinearActuatorMotor"), hardwareMap.get(Servo.class, "ReleaserServo"));
    }

    @Override
    public void start() {
        vuforia.activate();
    }

    /**
     * Initializes Vuforia data
     */
    protected void vuforiaData() {
        VectorF translation = vuforia.vector();

        // only one of these two will be used
        if (translation != null) {
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / Constants.mmPerInch, translation.get(1) / Constants.mmPerInch, translation.get(2) / Constants.mmPerInch);
        }
        telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                vuforia.getXOffset() / Constants.mmPerInch, vuforia.getYOffset() / Constants.mmPerInch, vuforia.getZOffset() / Constants.mmPerInch);

        if (translation != null) {
            telemetry.addLine("null");
        } else {
            telemetry.addLine("not null");
        }
    }

    /**
     * Powershot routine
     * @return if all 3 powershots have been fired
     */
    protected boolean powerShotRoutine() {
        switch (powerShotState) {
            case IDLE:
                powerShotState = PowerShotState.ONE;
                updateShotTrajectory();
                break;

            case ONE:
                updateStatus(PowerShotState.TWO);
                break;

            case TWO:
                updateStatus(PowerShotState.THREE);
                break;

            case THREE:
                updateStatus(PowerShotState.FINISHED);
                break;

            case FINISHED:
                return true;
        }
        return false;
    }

    /**
     * Checks if the robot is at shooting position. If it is, shoots and updates the shot number and the shooting state.
     * @param nextState if an update of status is needed
     */
    private void updateStatus(PowerShotState nextState) {
        if (atShootingPosition() && shootingSystem.shoot()) {
            powerShotState = nextState;
            updateShotTrajectory();
        }
    }

    /**
     * Updates the shot trajectory based on the current shot state
     */
    private void updateShotTrajectory() {
        trajectory = Trajectories.getTrajectory(powerShotState, currentPosition);
        roadRunnerDriveSystem.followTrajectoryAsync(trajectory);
    }

    /**
     * Assumes shooter is set to State PowerShot
     * @return if the robot has finished its current trajectory
     */
    private boolean atShootingPosition() {
        trajectoryFinished = roadRunnerDriveSystem.update();
        return trajectoryFinished;
    }

    @Override
    public void stop() {
        if (vuforia != null) {
            vuforia.deactivate();
        }

        if (shootingSystem != null) {
            shootingSystem.shutDown();
        }

        if (yeetSystem != null) {
            yeetSystem.shutDown();
        }
    }
}
