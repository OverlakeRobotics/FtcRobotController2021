
package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.components.TensorFlowNew;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.helpers.Constants;

import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

public abstract class AutonomousOpModeTest extends BaseOpMode {


    private int elevatorLevel;
    private boolean isRBorBT;
    private double baseTime;

    private GameState currentGameState;
    public RouteState routeState;
    protected TeamState teamState;

    private static final double driveSpeed = 0.5;
    private static final double rotateSpeed = 0.25;

    private static final int level = 2;

    public void init(TeamState teamState, RouteState routeState) {
        isRBorBT = ((teamState == TeamState.BLUE && routeState == RouteState.TOP) || (teamState == TeamState.RED && routeState == RouteState.BOTTOM));
        super.init();
        this.teamState = teamState;
        this.routeState = routeState;
        newGameState(GameState.SCAN_INITIAL);
        driveSystem.initMotors();
        //armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, Constants.ELEVATOR_MOTOR));
        //armSystem.initMotors();
        //armSystem.goToLevel(ArmSystem.LEVEL_CAROUSEL);
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR1), hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR2));
        intakeSystem.initMotors();
        turnTableSystem = new TurnTableSystem(hardwareMap.get(DcMotor.class, Constants.ROTATOR_MOTOR));
    }

    @Override
    public void start() {
        super.start();
        //vuforia.activate();
        //tensorflow.activate()

        newGameState(GameState.SCAN_INITIAL);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);
        telemetry.addData("elevatorLevel", elevatorLevel);
        telemetry.update();

        switch (currentGameState) {
            case SCAN_INITIAL:
                if (level == 2) {
                    elevatorLevel = ArmSystem.LEVEL_MID;
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE_PRIMARY);
                } else {
                    newGameState(GameState.SCAN_SECONDARY);
                }
                break;
            case SCAN_SECONDARY:
                if (driveSystem.driveToPosition((int) (Constants.tileWidth * Constants.mmPerInch * (2/3)), isRBorBT ? DriveSystem.Direction.FORWARD : DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    if (level == 3) {
                        elevatorLevel = isRBorBT ? ArmSystem.LEVEL_BOTTOM : ArmSystem.LEVEL_TOP;
                    } else {
                        elevatorLevel = isRBorBT ? ArmSystem.LEVEL_TOP : ArmSystem.LEVEL_BOTTOM;
                    }
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE_SECONDARY);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_ONE_PRIMARY:
                if (driveSystem.driveToPosition((int) (0.8 * Constants.tileWidth * Constants.mmPerInch), isRBorBT ? DriveSystem.Direction.FORWARD : DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_ONE_SECONDARY:
                if (driveSystem.driveToPosition((int) (0.8 * Constants.tileWidth * Constants.mmPerInch * (1/3)), isRBorBT ? DriveSystem.Direction.FORWARD : DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_TWO:
                if (driveSystem.driveToPosition((int) (0.4 * Constants.tileWidth * (12/7) * Constants.mmPerInch), DriveSystem.Direction.LEFT, driveSpeed)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_THREE);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_THREE:
                if (driveSystem.turn(-90, rotateSpeed)) {
                    newGameState(GameState.PLACE_CUBE_ONE);
                }
                break;

            case PLACE_CUBE_ONE:
                if (elevatorLevel == 0){
                    double giveUpHope = Math.random();
                    if (giveUpHope < 0.33) {
                        elevatorLevel = ArmSystem.LEVEL_BOTTOM;
                    }
                    if (giveUpHope < 0.66) {
                        elevatorLevel =  ArmSystem.LEVEL_MID;
                    }
                    else {
                        elevatorLevel = ArmSystem.LEVEL_TOP;
                    }
                }
                //armSystem.goToLevel(elevatorLevel);
                baseTime = elapsedTime.seconds();
                while (elapsedTime.seconds() > baseTime + 5.0) {
                    intakeSystem.spit_out();
                }
                //armSystem.goToLevel(ArmSystem.LEVEL_BOTTOM);
                intakeSystem.setPower(0);
                newGameState(routeState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE_ONE : GameState.DRIVE_TO_CAROUSEL_ONE);
                break;

            case DRIVE_TO_CAROUSEL_ONE:
                if (driveSystem.turn(90, rotateSpeed)) {
                    newGameState(GameState.DRIVE_TO_CAROUSEL_TWO);
                }
                break;

            case DRIVE_TO_CAROUSEL_TWO:
                if (driveSystem.turn(90, rotateSpeed)) {
                    newGameState(GameState.DRIVE_TO_CAROUSEL_THREE);
                }
                break;

            case DRIVE_TO_CAROUSEL_THREE:
                if (driveSystem.driveToPosition((int) (Constants.tileWidth * Constants.mmPerInch * 2.0), teamState == TeamState.BLUE ? DriveSystem.Direction.LEFT : DriveSystem.Direction.RIGHT, driveSpeed)) {
                    newGameState(GameState.SPIN_CAROUSEL);
                }
                break;

            case SPIN_CAROUSEL:
                //armSystem.goToLevel(ArmSystem.LEVEL_CAROUSEL);
                baseTime = elapsedTime.seconds();
                while (elapsedTime.seconds() > baseTime + 5.0) {
                    intakeSystem.Carousel();
                }
                intakeSystem.setPower(0);
                newGameState(GameState.PARK_IN_DEPOT_ONE);
                break;
            case PARK_IN_DEPOT_ONE:
                if (driveSystem.driveToPositionTicks((int) (Constants.tileWidth * Constants.mmPerInch * 0.69), DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case PARK_IN_WAREHOUSE_ONE:
                if (driveSystem.turn(teamState == TeamState.BLUE ? -90 : 90, rotateSpeed)) {
                    newGameState(GameState.PARK_IN_WAREHOUSE_TWO);
                }
                break;

            case PARK_IN_WAREHOUSE_TWO:
                if (driveSystem.driveToPosition((int) (0.5 * Constants.tileWidth * (12/7) * Constants.mmPerInch), teamState == TeamState.BLUE? DriveSystem.Direction.LEFT : DriveSystem.Direction.RIGHT, driveSpeed)) {
                    newGameState(GameState.PARK_IN_WAREHOUSE_THREE);
                }
                break;

            case PARK_IN_WAREHOUSE_THREE:
                if (driveSystem.driveToPosition((int) (Constants.tileWidth * Constants.mmPerInch * 3.0), DriveSystem.Direction.FORWARD, driveSpeed)) {
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
        /*if (tensorflowNew != null) {
            tensorflowNew.shutdown();
        }*/
    }

    /**
     * Updates the state of the system and updates RoadRunner trajectory
     *
     * @param newGameState to switch to
     */
    protected void newGameState(GameState newGameState) {
        currentGameState = newGameState;
    }
}