
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.List;

import static org.firstinspires.ftc.teamcode.helpers.Constants.tileWidth;

@Autonomous(name = "AutonomousOpMode", group = "Autonomous")
public class AutonomousOpMode extends BaseOpMode {

    // Variables
    private GameState currentGameState;
    private RouteState routeState;
    private TeamState teamState;

    private ElapsedTime elapsedTime;
    private boolean objectDetected;

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
    public void loop() {
        vuforiaData();
        telemetry.addData("GameState", currentGameState);
        telemetry.update();

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
                int i = 0;
                float distance = 9.0f;
                while (!objectDetected) {
                    if (i == 1) {
                        if (teamState == TeamState.RED) {
                            // move left
                        } else {
                            // move right
                        }
                    }
                    if (i == 2) {
                        if (teamState == TeamState.RED) {
                            // move right twice
                        } else {
                            // move left twice
                        }
                    }
                    if (i == 3) {
                        stop();
                    }
                    for (Recognition recognition : tensorflow.getInference()) {
                        if (recognition.getLabel().equals("Marker")) {
                            objectDetected = true;
                        } else {
                            i++;
                        }
                    }
                }
                if (objectDetected) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                }
                break;

            case DRIVE_TO_ALLIANCE_HUB:
                if (teamState == TeamState.RED) {
                    // move right
                } else {
                    // move left
                }
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
