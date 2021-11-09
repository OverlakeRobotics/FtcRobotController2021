
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.teamcode.helpers.Constants.tileWidth;

@Autonomous(name = "AutonomousOpMode", group = "Autonomous")
public class AutonomousOpMode extends BaseOpMode {

    // Variables
    private GameState currentGameState;
    private ElapsedTime elapsedTime;

    // Systems
    private TensorFlow tensorflow;
    private DriveSystem driveSystem;

    @Override
    public void init() {
        super.init();
        tensorflow = new TensorFlow();
        tensorflow.activate();
        elapsedTime = new ElapsedTime();
        newGameState(GameState.INITIAL);
        elapsedTime.reset();
        driveSystem = new DriveSystem();
        driveSystem.initMotors();
    }

    @Override
    public void init_loop() {
        if (elapsedTime.milliseconds() > 1500) {
            switch (tensorflow.getObject()) {
                case NADA:
                    break;

                case BALL:
                    break;

                case CUBE:
                    break;

                case DUCK:
                    break;

                case MARKER:
                    break;
            }
        }
    }

    private void updateTargetRegion() {
        int max = shotCount[0];
        max = Math.max(max, shotCount[1]);
        max = Math.max(max, shotCount[2]);
        if (shotCount[1] == max) {
            targetRegion = TargetDropBox.BOX_B;
        } else if (shotCount[2] == max) {
            targetRegion = TargetDropBox.BOX_C;
        } else {
            targetRegion = TargetDropBox.BOX_A;
        }
        telemetry.addData("Target Region", targetRegion.name());
        Log.d("COLOR", colorSensor.red() + "");
        telemetry.update();
    }

    @Override
    public void start() {
        tensorflow.shutdown();
        super.start();
        updateTargetRegion();
    }

    @Override
    public void loop() {
//        vuforiaData();
        telemetry.addData("GameState", currentGameState);
        telemetry.update();

//        Log.d("POSITION", "STATE: " + currentGameState.name());
//        Log.d("POSITION", "x: " + poseEstimate.getX());
//        Log.d("POSITION","y: " + poseEstimate.getY());
//        Log.d("POSITION", "heading: " + poseEstimate.getHeading());

        trajectoryFinished = currentGameState == GameState.INITIAL || roadRunnerDriveSystem.update() || trajectory == null;

        if (currentGameState == GameState.RETURN_TO_NEST || currentGameState == GameState.COMPLETE) {
            if (elapsedTime.milliseconds() > 500 && !pickedUpArm) {
                pickedUpArm = yeetSystem.pickedUp(false);
            }
        }

        // Makes sure the trajectory is finished before doing anything else
        switch (currentGameState) {
            case INITIAL:
                newGameState(GameState.DRIVE_TO_BARCODE);
                break;

            case DRIVE_TO_BARCODE:
                while (elapsedTime.seconds() < 20) { // TODO: update this int
                    driveSystem.setAllMotorPower(1);
                }
                driveSystem.setAllMotorPower(0);
                newGameState(GameState.DETECT_BARCODE);
                break;

            case DETECT_BARCODE:
                tensorflow
                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                break;

            case DRIVE_TO_ALLIANCE_HUB:
                newGameState(GameState.PLACE_CUBE);
                break;

            case PLACE_CUBE:
                newGameState(GameState.DRIVE_TO_CAROUSEL);
                break;

            case DRIVE_TO_CAROUSEL:
                newGameState(GameState.SPIN_CAROUSEL);
                break;

            case SPIN_CAROUSEL:
                newGameState(GameState.PARK_IN_DEPOT);
                break;

            case PARK_IN_DEPOT:
                newGameState(GameState.COMPLETE);
                break;

            case COMPLETE:
                stop();
                break;


        }
    }

    @Override
    public void stop() {
        super.stop();
        if (tensorflow != null) {
            tensorflow.shutdown();
        }
    }

    /**
     * Updates the state of the system and updates RoadRunner trajectory
     * @param newGameState to switch to
     */
    protected void newGameState(GameState newGameState) {
        currentGameState = newGameState;
        currentPosition = roadRunnerDriveSystem.getPositionEstimate();

        if (currentGameState == GameState.DELIVER_WOBBLE) {
            trajectory = Trajectories.getTrajectory(targetRegion, currentPosition, deliveredFirstWobble);
        } else {
            trajectory = Trajectories.getTrajectory(currentGameState, currentPosition);
        }

        if (trajectory != null) {
            roadRunnerDriveSystem.followTrajectoryAsync(trajectory);
        }
    }

    /**
     * Calibrates RoadRunner using Vuforia data
     * Because camera is sideways, the x offset corresponds to y coordinates and visa versa
     * Vuforia is in millimeters and everything else is in inches
     */
    private void calibrateLocation() {
        double xUpdate = Coordinates.CALIBRATION.getX() - (vuforia.getYOffset() / Constants.mmPerInch - tileWidth);
        double yUpdate = Coordinates.CALIBRATION.getY() + vuforia.getXOffset() / Constants.mmPerInch;
        roadRunnerDriveSystem.setPoseEstimate(new Pose2d(xUpdate, yUpdate));
    }
}
