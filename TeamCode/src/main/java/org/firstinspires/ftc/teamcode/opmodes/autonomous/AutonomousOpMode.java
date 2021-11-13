
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
    boolean[] barcodes;

    private static final String SPIN_WHEEL = "spin-wheel";
    private static final String WEBCAM = "Webcam 1";

    private GameState currentGameState;
    private RouteState currentRouteState;
    protected TeamState teamState;

    private int level;

    // Systems
    //private TensorFlow tensorflow;
    private TensorFlowNew tensorflowNew;
    private Vuforia vuforia;

    @Override
    public void init() {
        newGameState(GameState.INITIAL);
        super.init();
        driveSystem.initMotors();
        vuforia = Vuforia.getInstance(hardwareMap.get(WebcamName.class, WEBCAM));
        //tensorflow = new TensorFlow(vuforia);
        tensorflowNew = new TensorFlowNew(vuforia);
        armSystem = new ArmSystem(hardwareMap.get(DcMotorEx.class, ELEVATOR_MOTOR), hardwareMap.get(Servo.class, RELEASER));
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, SPIN_WHEEL));
        barcodes = new boolean[3];
    }

    @Override
    public void start() {
        super.start();
        vuforia.activate();
        //tensorflow.activate();
        for (int x = 1; x <= 3; x++){
            tensorflowNew.activate(x);
            barcodes[x] = (tensorflowNew.getInference().size() > 0);
        }
        for (int x = 1; x <= 3; x++){
            if (barcodes[x] == true){
                elevatorState = tensorflowNew.getObjectNew(x);
            }
        }
        newGameState(GameState.DRIVE_TO_BARCODE_CENTER);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);

        switch (currentGameState) {
            case DRIVE_TO_BARCODE_CENTER:
                if (driveSystem.getTicks() < 543){
                    driveSystem.setAllMotorPower(0);
                    newGameState(GameState.DETECT_BARCODE);
                }
                //move(Coordinates.RED_BOTTOM_CENTERBARCODE, Coordinates.BLUE_BOTTOM_CENTERBARCODE);
                newGameState(GameState.DETECT_BARCODE);
                break;

            case DETECT_BARCODE:
                elevatorState = tensorflowNew.getObjectNew();
                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                /*if (level == 1) {
                    //move(Coordinates.RED_BOTTOM_LEFTBARCODE, Coordinates.BLUE_BOTTOM_RIGHTBARCODE);
                }
                if (level == 2) {
                    move(Coordinates.RED_BOTTOM_RIGHTBARCODE, Coordinates.BLUE_BOTTOM_LEFTBARCODE);
                }
                if (level == 3) {
                    stop();
                }
                if (tensorflow.getObject() == ObjectEnums.DUCK){
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                }
                else {
                    level++;
                }*/
                break;
            case DRIVE_TO_ALLIANCE_HUB:
                move(Coordinates.RED_ALLIANCE_HUB_BOTTOM, Coordinates.BLUE_ALLIANCE_HUB_BOTTOM);
                newGameState(GameState.PLACE_CUBE);
                break;

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
                break;*/

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
        }
        telemetry.update();
    }

    @Override
    public void stop() {
        super.stop();
        if (tensorflowNew != null) {
            tensorflowNew.shutdown();
        }
    }

    /**
     * Updates the state of the system and updates RoadRunner trajectory
     * @param newGameState to switch to
     */
    protected void newGameState(GameState newGameState) {
        currentGameState = newGameState;
    }

    protected void move(Coordinates redTeamCoords, Coordinates blueTeamCoords) {
        double deltaTime = DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
        int xPower = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[2];
        double baseTime = elapsedTime.seconds();
        while (elapsedTime.seconds() < baseTime + deltaTime) {
            driveSystem.joystickDrive(0, xPower, 0);
        }
        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());

        deltaTime = DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
        int yPower = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[3];
        baseTime = elapsedTime.seconds();
        while (elapsedTime.seconds() < baseTime + deltaTime) {
            driveSystem.joystickDrive(0, 0, yPower);
        }
        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
    }

    protected void moveTicks(Coordinates redTeamCoords, Coordinates blueTeamCoords) {
        int deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
        //int xPower = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[2];
        driveSystem.driveTicks(deltaTicks);
        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());

        driveSystem.turn(90, 0.75);

        deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
        driveSystem.driveTicks(deltaTicks);
        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
    }
}
