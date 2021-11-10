
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.components.WheelSystem;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.teamcode.helpers.Constants.tileWidth;

@Autonomous(name = "AutonomousOpMode", group = "Autonomous")
public class AutonomousOpMode extends BaseOpMode {

    private static final String ELEVATOR_MOTOR = "elevator-motor";
    private static final String RELEASER = "releaser";

    private static final String SPIN_WHEEL = "spin-wheel";

    private GameState currentGameState;
    protected TeamState teamState;

    private boolean objectDetected;
    private int level;

    // Systems
    private TensorFlow tensorflow;
    private ArmSystem armSystem;
    private WheelSystem wheelSystem;

    @Override
    public void init() {
        super.init();
        tensorflow = new TensorFlow();
        tensorflow.activate();
        newGameState(GameState.INITIAL);
        armSystem = new ArmSystem(hardwareMap.get(DcMotorEx.class, ELEVATOR_MOTOR), hardwareMap.get(Servo.class, RELEASER));
        armSystem.initMotors();
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, SPIN_WHEEL));
        wheelSystem.initMotors();
    }

    @Override
    public void loop() {
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
                while (!objectDetected) {
                    if (level == 1) {
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
                    if (level == 2) {
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
                    if (level == 3) {
                        stop();
                    }
                    for (Recognition recognition : tensorflow.getInference()) {
                        if (recognition.getLabel().equals("Marker")) {
                            objectDetected = true;
                        } else {
                            level++;
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
                switch (level) {
                    case 0:
                        armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_MID);
                        break;
                    case 1:
                        if (teamState == TeamState.RED) {
                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_BOTTOM);
                        } else {
                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_TOP);
                        }
                        break;
                    case 2:
                        if (teamState == TeamState.RED) {
                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_TOP);
                        } else {
                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_BOTTOM);
                        }
                        break;
                }
                armSystem.release(true);
                newGameState(GameState.DRIVE_TO_CAROUSEL);
                break;

            case DRIVE_TO_CAROUSEL:
                if (teamState == TeamState.RED) {
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(0, -1, 0);
                    }
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(1, 0, 0);
                    }
                    // move left, rotate right
                } else {
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(0, 1, 0);
                    }
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(-1, 0, 0);
                    }
                    // move right, rotate left
                }
                newGameState(GameState.SPIN_CAROUSEL);
                break;

            case SPIN_CAROUSEL:
                deltaTime = 5.0;
                baseTime = elapsedTime.seconds();
                while (elapsedTime.seconds() < baseTime + deltaTime) {
                    wheelSystem.spinTheWheelFully();
                }
                wheelSystem.stopWheel();
                newGameState(GameState.PARK_IN_DEPOT);
                break;

            case PARK_IN_DEPOT:
                if (teamState == TeamState.RED) {
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(-1, 0, 0);
                    }
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(0, 1, 0);
                    }
                    // rotate left, move right
                } else {
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(1, 0, 0);
                    }
                    deltaTime = 5.0;
                    baseTime = elapsedTime.seconds();
                    while (elapsedTime.seconds() < baseTime + deltaTime) {
                        driveSystem.joystickDrive(0, -1, 0);
                    }
                    // rotate right, move left
                }
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
        //double xUpdate = Coordinates.CALIBRATION.getX() - (vuforia.getYOffset() / Constants.mmPerInch - tileWidth);
        //double yUpdate = Coordinates.CALIBRATION.getY() + vuforia.getXOffset() / Constants.mmPerInch;
    }
}
