
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
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.components.TensorFlowNew;
import org.firstinspires.ftc.teamcode.components.Vuforia;
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


    private static final String INTAKE_MOTOR1 = "intakeMotor1";
    private static final String INTAKE_MOTOR2 = "intakeMotor2";

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

    public void init(TeamState teamState, RouteState routeState) {
        super.init();
        this.teamState = teamState;
        this.currentRouteState = routeState;
        newGameState(GameState.INITIAL);
        driveSystem.initMotors();
        vuforia = Vuforia.getInstance(hardwareMap.get(WebcamName.class, WEBCAM));
        //tensorflow = new TensorFlow(vuforia);
        tensorflowNew = new TensorFlowNew(vuforia);
        armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, ELEVATOR_MOTOR));
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, INTAKE_MOTOR1), hardwareMap.get(DcMotor.class, INTAKE_MOTOR2));
        barcodes = new boolean[3];
    }

    @Override
    public void start() {
        super.start();
        vuforia.activate();
        //tensorflow.activate();
        for (int x = 1; x <= 3; x++) {
            tensorflowNew.activate(x);
            barcodes[x] = (tensorflowNew.getInference().size() > 0);
            if (barcodes[x]) {
                elevatorState = tensorflowNew.getObjectNew(x);
            }
        }
        newGameState(GameState.DETECT_BARCODE);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);

        switch (currentGameState) {
            case DETECT_BARCODE:
                //TODO ADJUST CALCULATIONS FOR DEGREE TURNING AND IMPLEMENT TEAM STUFF
                error jack if you can do this that would be great
                driveSystem.turn(-90, 0.75);
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
                if (driveSystem.driveToPosition(300, DriveSystemOther.Direction.FORWARD, 0.75)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_TWO:
                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
                } */

                if (driveSystem.turn(currentRouteState == RouteState.TOP ? -85 : 85, 1.0)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_THREE);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_THREE:
                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
                } */

                if (driveSystem.driveToPosition(300, DriveSystemOther.Direction.FORWARD, 0.75)) {
                    newGameState(GameState.PLACE_CUBE);
                }
                break;

            case PLACE_CUBE:
                armSystem.goToLevel(elevatorState);
                intakeSystem.spit_out();
                armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_BOTTOM);
                newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE_ONE : GameState.DRIVE_TO_CAROUSEL_ONE);
                break;

            case DRIVE_TO_CAROUSEL_ONE:
                if (driveSystem.driveToPosition(840, DriveSystemOther.Direction.BACKWARD, 0.75)) {
                    newGameState(GameState.DRIVE_TO_CAROUSEL_TWO);
                }
                break;

            case DRIVE_TO_CAROUSEL_TWO:
                if (driveSystem.driveToPosition(350, DriveSystemOther.Direction.RIGHT, 0.75)) {
                    newGameState(GameState.SPIN_CAROUSEL);
                }
                break;

            case SPIN_CAROUSEL:
                double baseTime = elapsedTime.seconds();
                while (elapsedTime.seconds() < baseTime + 5.0) {
                    intakeSystem.Carousel();
                }
                intakeSystem.setPower(0);
                newGameState(GameState.PARK_IN_DEPOT);
                break;

            case PARK_IN_DEPOT:
                if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case PARK_IN_WAREHOUSE_ONE:
                if (driveSystem.driveToPosition(350, DriveSystemOther.Direction.LEFT, 0.75)) {
                    newGameState(GameState.PARK_IN_WAREHOUSE_TWO);
                }
                break;

            case PARK_IN_WAREHOUSE_TWO:
                if (driveSystem.driveToPosition(1000, DriveSystemOther.Direction.BACKWARD, 0.75)) {
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
     *
     * @param newGameState to switch to
     */
    protected void newGameState(GameState newGameState) {
        currentGameState = newGameState;
    }

    protected boolean move(int ticksX, int ticksY, int rotation) {
        if (driveSystem.driveToPositionTicks(ticksX, DriveSystemOther.Direction.FORWARD, 0.75)) {
            if (driveSystem.turn(rotation, 1.0)) {
                return driveSystem.driveToPositionTicks(ticksY, DriveSystemOther.Direction.FORWARD, 0.75);
            }
        }
        return false;
    }

    protected void moveTicks(Coordinates redTeamCoords, Coordinates blueTeamCoords, int degrees) {
        int deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
        driveSystem.driveToPositionTicks(deltaTicks, DriveSystemOther.Direction.FORWARD, 1.0);
        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());

        driveSystem.turn(degrees, 1.0);

        deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
        driveSystem.driveToPositionTicks(deltaTicks, DriveSystemOther.Direction.FORWARD, 1.0);
        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
    }
}
//}
//
//    @Override
//    public void loop() {
//        telemetry.addData("GameState", currentGameState);
//
//        switch (currentGameState) {
//            case DRIVE_TO_BARCODE_CENTER:
//                if (driveSystem.getTicks() < 543){
//                    driveSystem.setAllMotorPower(0);
//                    newGameState(GameState.DETECT_BARCODE);
//                }
//                //move(Coordinates.RED_BOTTOM_CENTERBARCODE, Coordinates.BLUE_BOTTOM_CENTERBARCODE);
//                newGameState(GameState.DETECT_BARCODE);
//                break;
//
//            case DETECT_BARCODE:
//                //elevatorState = tensorflowNew.getObjectNew();
//                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
//                /*if (level == 1) {
//                    //move(Coordinates.RED_BOTTOM_LEFTBARCODE, Coordinates.BLUE_BOTTOM_RIGHTBARCODE);
//                }
//                if (level == 2) {
//                    move(Coordinates.RED_BOTTOM_RIGHTBARCODE, Coordinates.BLUE_BOTTOM_LEFTBARCODE);
//                }
//                if (level == 3) {
//                    stop();
//                }
//                if (tensorflow.getObject() == ObjectEnums.DUCK){
//                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
//                }
//                else {
//                    level++;
//                }*/
//                break;
//            case DRIVE_TO_ALLIANCE_HUB:
//                move(Coordinates.RED_ALLIANCE_HUB_BOTTOM, Coordinates.BLUE_ALLIANCE_HUB_BOTTOM);
//                newGameState(GameState.PLACE_CUBE);
//                break;
//
//            case PLACE_CUBE:
//                armSystem.goToLevel(elevatorState);
//                armSystem.release(true);
//                newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
//                break;
//                /*switch (level) {
//                    case 0:
//                        armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_MID);
//                        break;
//                    case 1:
//                        if (teamState == TeamState.RED) {
//                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_BOTTOM);
//                        } else {
//                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_TOP);
//                        }
//                        break;
//                    case 2:
//                        if (teamState == TeamState.RED) {
//                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_TOP);
//                        } else {
//                            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_BOTTOM);
//                        }
//                        break;
//                }
//                armSystem.release(true);
//                newGameState(GameState.DRIVE_TO_CAROUSEL);
//                break;*/
//
//            case DRIVE_TO_CAROUSEL:
//                //move(Coordinates.RED_CAROUSEL, Coordinates.BLUE_CAROUSEL);
//                newGameState(GameState.SPIN_CAROUSEL);
//                break;
//
//            case SPIN_CAROUSEL:
//                double baseTime = elapsedTime.seconds();
//                while (elapsedTime.seconds() < baseTime + 5.0) {
//                    wheelSystem.spinTheWheelFully();
//                }
//                wheelSystem.stopWheel();
//                newGameState(GameState.PARK_IN_DEPOT);
//                break;
//
//            case PARK_IN_DEPOT:
//                move(Coordinates.RED_DEPOT, Coordinates.BLUE_DEPOT);
//                newGameState(GameState.COMPLETE);
//                break;
//
//            case PARK_IN_WAREHOUSE:
//                move(Coordinates.RED_WAREHOUSE, Coordinates.BLUE_WAREHOUSE);
//                newGameState(GameState.COMPLETE);
//                break;
//
//            case COMPLETE:
//                stop();
//                break;
//        }
//        telemetry.update();
//    }
//
//    @Override
//    public void stop() {
//        super.stop();
//        if (tensorflowNew != null) {
//            tensorflowNew.shutdown();
//        }
//    }
//
//    /**
//     * Updates the state of the system and updates RoadRunner trajectory
//     * @param newGameState to switch to
//     */
//    protected void newGameState(GameState newGameState) {
//        currentGameState = newGameState;
//    }
//
//    protected void move(Coordinates redTeamCoords, Coordinates blueTeamCoords) {
//        double deltaTime = DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
//        int xPower = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[2];
//        double baseTime = elapsedTime.seconds();
//        while (elapsedTime.seconds() < baseTime + deltaTime) {
//            driveSystem.joystickDrive(0, xPower, 0);
//        }
//        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());
//
//        deltaTime = DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
//        int yPower = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[3];
//        baseTime = elapsedTime.seconds();
//        while (elapsedTime.seconds() < baseTime + deltaTime) {
//            driveSystem.joystickDrive(0, 0, yPower);
//        }
//        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
//    }
//
//    protected void moveTicks(Coordinates redTeamCoords, Coordinates blueTeamCoords) {
//        int deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
//        //int xPower = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[2];
//        driveSystem.driveTicks(deltaTicks);
//        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());
//
//        driveSystem.turn(90, 0.75);
//
//        deltaTicks = (int) DriveSystem.TimeCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
//        driveSystem.driveTicks(deltaTicks);
//        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
//    }
//}
