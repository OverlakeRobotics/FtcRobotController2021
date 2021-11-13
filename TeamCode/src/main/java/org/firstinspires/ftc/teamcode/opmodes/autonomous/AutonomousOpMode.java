
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
import org.firstinspires.ftc.teamcode.components.DriveSystemOther;
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
    protected TeamState teamState;

    private int level;

    // Systems
    //private TensorFlow tensorflow;
    private TensorFlowNew tensorflowNew;
    private Vuforia vuforia;

    public void init(TeamState teamState) {
        newGameState(GameState.INITIAL);
        super.init();
        this.teamState = teamState;
        vuforia = Vuforia.getInstance(hardwareMap.get(WebcamName.class, WEBCAM));
        //tensorflow = new TensorFlow(vuforia);
        tensorflowNew = new TensorFlowNew(vuforia);
        armSystem = new ArmSystem(hardwareMap.get(DcMotorEx.class, ELEVATOR_MOTOR), hardwareMap.get(Servo.class, RELEASER));
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, SPIN_WHEEL));
    }

    @Override
    public void start() {
        super.start();
        vuforia.activate();
        //tensorflow.activate();
        tensorflowNew.activate();
        newGameState(GameState.DETECT_BARCODE);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);

        switch (currentGameState) {
            case DETECT_BARCODE:
                elevatorState = tensorflowNew.getObjectNew();
                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
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
            case DRIVE_TO_ALLIANCE_HUB_ONE:
                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
                } */

                if (driveSystemOther.driveToPosition(300, DriveSystemOther.Direction.FORWARD, 0.75)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_TWO:
                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
                } */

                if (driveSystemOther.turn(currentRouteState == RouteState.TOP ? -85 : 85, 1.0)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_THREE);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_THREE:
                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
                } */

                if (driveSystemOther.driveToPosition(300, DriveSystemOther.Direction.FORWARD, 0.75)) {
                    newGameState(GameState.PLACE_CUBE);
                }
                break;

            case PLACE_CUBE:
                armSystem.goToLevel(elevatorState);
                armSystem.release(true);
                newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE_ONE : GameState.DRIVE_TO_CAROUSEL_ONE);
                break;

            case DRIVE_TO_CAROUSEL_ONE:
                if (driveSystemOther.driveToPosition(840, DriveSystemOther.Direction.BACKWARD, 0.75)) {
                    newGameState(GameState.DRIVE_TO_CAROUSEL_TWO);
                }
                break;

            case DRIVE_TO_CAROUSEL_TWO:
                if (driveSystemOther.driveToPosition(350, DriveSystemOther.Direction.RIGHT, 0.75)) {
                    newGameState(GameState.SPIN_CAROUSEL);
                }
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
                if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case PARK_IN_WAREHOUSE_ONE:
                if (driveSystemOther.driveToPosition(350, DriveSystemOther.Direction.LEFT, 0.75)) {
                    newGameState(GameState.PARK_IN_WAREHOUSE_TWO);
                }
                break;

            case PARK_IN_WAREHOUSE_TWO:
                if (driveSystemOther.driveToPosition(1000, DriveSystemOther.Direction.BACKWARD, 0.75)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case COMPLETE:
                stop();
                break;
        }
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

    protected boolean move(int ticksX, int ticksY, int rotation) {
        if (driveSystemOther.driveToPositionTicks(ticksX, DriveSystemOther.Direction.FORWARD, 0.75)) {
            if (driveSystemOther.turn(rotation, 1.0)) {
                return driveSystemOther.driveToPositionTicks(ticksY, DriveSystemOther.Direction.FORWARD, 0.75);
            }
        }
        return false;
    }

    protected void moveTicks(Coordinates redTeamCoords, Coordinates blueTeamCoords, int degrees) {
        int deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
        driveSystemOther.driveToPositionTicks(deltaTicks, DriveSystemOther.Direction.FORWARD, 1.0);
        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());

        driveSystemOther.turn(degrees, 1.0);

        deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
        driveSystemOther.driveToPositionTicks(deltaTicks, DriveSystemOther.Direction.FORWARD, 1.0);
        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
    }
}
