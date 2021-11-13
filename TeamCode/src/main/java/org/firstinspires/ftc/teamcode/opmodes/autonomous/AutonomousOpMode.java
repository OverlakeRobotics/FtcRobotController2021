
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.components.TensorFlowNew;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.components.WheelSystem;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.ObjectEnums;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.teamcode.helpers.Constants.tileWidth;

public abstract class AutonomousOpMode extends BaseOpMode {

    private static final String ELEVATOR_MOTOR = "elevator-motor";
    private static final String RELEASER = "releaser";
    private ArmSystem.ElevatorState elevatorState;

    private static final String SPIN_WHEEL = "spin-wheel";
    private static final String WEBCAM = "Webcam 1";

    private GameState currentGameState;
    private RouteState currentRouteState;
    private Coordinates CURRENT_POSITION;
    protected TeamState teamState;

    private int level;

    // Systems
    private TensorFlow tensorflow;
    private TensorFlowNew tensorflowNew;
    private Vuforia vuforia;

    @Override
    public void init() {
        newGameState(GameState.INITIAL);
        super.init();
        driveSystem.initMotors();
        CURRENT_POSITION = Coordinates.BLUE_STARTING_POSITION_BOTTOM;
        vuforia = Vuforia.getInstance(hardwareMap.get(WebcamName.class, WEBCAM));
        tensorflow = new TensorFlow(vuforia);
        tensorflowNew = new TensorFlowNew(vuforia);
        armSystem = new ArmSystem(hardwareMap.get(DcMotorEx.class, ELEVATOR_MOTOR), hardwareMap.get(Servo.class, RELEASER));
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, SPIN_WHEEL));
    }

    @Override
    public void start() {
        super.start();
        vuforia.activate();
        tensorflow.activate();
        tensorflowNew.activate();
        newGameState(GameState.DETECT_BARCODE);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);

        switch (currentGameState) {
            case DETECT_BARCODE:
                elevatorState = tensorflowNew.getObjectNew();
                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                break;
            case DRIVE_TO_ALLIANCE_HUB:
                moveTicks(Coordinates.RED_ALLIANCE_HUB_BOTTOM, Coordinates.BLUE_ALLIANCE_HUB_BOTTOM, 90);
                newGameState(GameState.PLACE_CUBE);
                break;
/*
            case PLACE_CUBE:
                armSystem.goToLevel(elevatorState);
                armSystem.release(true);
                newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
                break;
                /*switch (level) {
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
                //move(Coordinates.RED_CAROUSEL, Coordinates.BLUE_CAROUSEL);
                newGameState(GameState.SPIN_CAROUSEL);
                break;

            case SPIN_CAROUSEL:
                double baseTime = elapsedTime.seconds();
                while (elapsedTime.seconds() < baseTime + 5.0) {
                    wheelSystem.spinTheWheelFully();
                }
                wheelSystem.stopWheel();
                newGameState(GameState.PARK_IN_DEPOT);
                break;

            case PARK_IN_DEPOT:
                move(Coordinates.RED_DEPOT, Coordinates.BLUE_DEPOT);
                newGameState(GameState.COMPLETE);
                break;

            case PARK_IN_WAREHOUSE:
                move(Coordinates.RED_WAREHOUSE, Coordinates.BLUE_WAREHOUSE);
                newGameState(GameState.COMPLETE);
                break;

            case COMPLETE:
                stop();
                break;
*/
        }
        telemetry.update();
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

    protected void moveTicks(Coordinates redTeamCoords, Coordinates blueTeamCoords, int heading) {
        int deltaTicks = (int) DriveSystem.TimeCoordinate(CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
        driveSystem.driveTicks(deltaTicks);
        CURRENT_POSITION.x = (teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());

        driveSystem.turn(heading, 0.75);

        deltaTicks = (int) DriveSystem.TimeCoordinate(CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
        driveSystem.driveTicks(deltaTicks);
        CURRENT_POSITION.y = (teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
    }
}
