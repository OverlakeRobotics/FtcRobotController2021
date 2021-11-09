
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.teamcode.helpers.Constants.tileWidth;

@Autonomous(name = "AutonomousOpMode", group = "Autonomous")
public class AutonomousOpMode extends BaseOpMode {

    // Variables
    private static final String MOTOR_FRONT_RIGHT = "motor-front-right";
    private static final String MOTOR_FRONT_LEFT = "motor-front-left";
    private static final String MOTOR_BACK_RIGHT = "motor-back-right";
    private static final String MOTOR_BACK_LEFT = "motor-back-left";

    private GameState currentGameState;
    private RouteState routeState;
    private TeamState teamState;

    private ElapsedTime elapsedTime;
    private boolean objectDetected;

    // Systems
    private TensorFlow tensorflow;
    private DriveSystem driveSystem;
    private ArmSystem armSystem;

    @Override
    public void init() {
        super.init();
        tensorflow = new TensorFlow();
        tensorflow.activate();
        elapsedTime = new ElapsedTime();
        newGameState(GameState.INITIAL);
        elapsedTime.reset();
        driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));
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
                    driveSystem.joystickDrive(0, 0, 1);
                }
                driveSystem.joystickDrive(0, 0, 0);
                newGameState(GameState.DETECT_BARCODE);
                break;

            case DETECT_BARCODE:
                int i = 0;
                float distance = 9.0f;
                while (!objectDetected) {
                    if (i == 1) {
                        if (teamState == TeamState.RED) {
                            double deltaTime = 2.0;
                            double baseTime = elapsedTime.seconds();
                            while (elapsedTime.seconds() < baseTime + deltaTime) {
                                driveSystem.joystickDrive(0, -1, 0);
                            }
                            // move left
                        } else {
                            double deltaTime = 2.0;
                            double baseTime = elapsedTime.seconds();
                            while (elapsedTime.seconds() < baseTime + deltaTime) {
                                driveSystem.joystickDrive(0, 1, 0);
                            }
                            // move right
                        }
                    }
                    if (i == 2) {
                        if (teamState == TeamState.RED) {
                            double deltaTime = 4.0;
                            double baseTime = elapsedTime.seconds();
                            while (elapsedTime.seconds() < baseTime + deltaTime) {
                                driveSystem.joystickDrive(0, 1, 0);
                            }
                            // move right twice
                        } else {
                            double deltaTime = 4.0;
                            double baseTime = elapsedTime.seconds();
                            while (elapsedTime.seconds() < baseTime + deltaTime) {
                                driveSystem.joystickDrive(0, -1, 0);
                            }
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
                    double deltaTime = 2.0;
                    double baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(0, 1, 0);
                    }
                } else {
                    double deltaTime = 2.0;
                    double baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(0, -1, 0);
                    }
                }
                newGameState(GameState.PLACE_CUBE);
                break;

            case PLACE_CUBE:
                // do this
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
    }
}
