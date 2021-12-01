
package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.ImuSystem;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

import java.util.EnumMap;
import java.util.Map;

public abstract class AutonomousOpModeTest extends OpMode {

    private int elevatorLevel;
    private boolean isRBorBT;

    private GameState gameState;
    protected RouteState routeState;
    protected TeamState teamState;

    private static final double driveSpeed = 0.5;
    private static final double rotateSpeed = 0.25;

    // Systems
    protected ImuSystem imuSystem;
    protected ElapsedTime elapsedTime;
    protected DriveSystem driveSystem;

    @Override
    public void init() {
        imuSystem = new ImuSystem(hardwareMap.get(BNO055IMU.class, Constants.IMU));
        Map<DriveSystem.MotorNames, DcMotor> motors = new EnumMap<>(DriveSystem.MotorNames.class);
        motors.put(DriveSystem.MotorNames.FRONTRIGHT, hardwareMap.get(DcMotor.class, Constants.MOTOR_FRONT_RIGHT));
        motors.put(DriveSystem.MotorNames.FRONTLEFT, hardwareMap.get(DcMotor.class, Constants.MOTOR_FRONT_LEFT));
        motors.put(DriveSystem.MotorNames.BACKRIGHT, hardwareMap.get(DcMotor.class, Constants.MOTOR_BACK_RIGHT));
        motors.put(DriveSystem.MotorNames.BACKLEFT, hardwareMap.get(DcMotor.class, Constants.MOTOR_BACK_LEFT));

        elapsedTime = new ElapsedTime();
        driveSystem = new DriveSystem(motors, hardwareMap.get(BNO055IMU.class, Constants.IMU));
        isRBorBT = ((teamState == TeamState.BLUE && routeState == RouteState.TOP) || (teamState == TeamState.RED && routeState == RouteState.BOTTOM));
        newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE_PRIMARY);
        driveSystem.initMotors();
    }

    @Override
    public void start() {
        super.start();
        elapsedTime.reset();
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", gameState);

        switch (gameState) {
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
                if (driveSystem.turn(90, rotateSpeed)) {
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
                newGameState(routeState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE_ONE : GameState.DRIVE_TO_CAROUSEL_ONE);
                break;

            case DRIVE_TO_CAROUSEL_ONE:
                if (driveSystem.turn(170, rotateSpeed)) {
                    newGameState(GameState.DRIVE_TO_CAROUSEL_TWO);
                }
                break;

            case DRIVE_TO_CAROUSEL_TWO:
                if (driveSystem.driveToPosition((int) (Constants.tileWidth * Constants.mmPerInch * 2.0), teamState == TeamState.RED ? DriveSystem.Direction.LEFT : DriveSystem.Direction.RIGHT, driveSpeed)) {
                    newGameState(GameState.SPIN_CAROUSEL);
                }
                break;

            case SPIN_CAROUSEL:
                newGameState(GameState.PARK_IN_DEPOT_ONE);
                break;
            case PARK_IN_DEPOT_ONE:
                if (driveSystem.driveToPositionTicks((int) (Constants.tileWidth * Constants.mmPerInch * 0.69), DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case PARK_IN_WAREHOUSE_ONE:
                if (driveSystem.turn(teamState == TeamState.RED ? -90 : 90, rotateSpeed)) {
                    newGameState(GameState.PARK_IN_WAREHOUSE_TWO);
                }
                break;

            case PARK_IN_WAREHOUSE_TWO:
                if (driveSystem.driveToPosition((int) (0.5 * Constants.tileWidth * (12/7) * Constants.mmPerInch), teamState == TeamState.BLUE ? DriveSystem.Direction.LEFT : DriveSystem.Direction.RIGHT, driveSpeed)) {
                    newGameState(GameState.PARK_IN_WAREHOUSE_THREE);
                }
                break;

            case PARK_IN_WAREHOUSE_THREE:
                if (driveSystem.driveToPosition(1000, DriveSystem.Direction.FORWARD, driveSpeed)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case COMPLETE:
                stop();
                break;
        }
    }

    /**
     * Updates the state of the system and updates RoadRunner trajectory
     *
     * @param newGameState to switch to
     */
    protected void newGameState(GameState newGameState) {
        gameState = newGameState;
    }
}