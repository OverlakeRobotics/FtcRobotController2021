//package org.firstinspires.ftc.teamcode.opmodes.teleop;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.AnalogInput;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.LED;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.components.DriveSystem;
//import org.firstinspires.ftc.teamcode.components.DriveSystemOther;
//import org.firstinspires.ftc.teamcode.components.ImuSystem;
//// import org.firstinspires.ftc.teamcode.components.WeightSystem;
//// import org.firstinspires.ftc.teamcode.components.WheelSystem;
//import org.firstinspires.ftc.teamcode.helpers.Constants;
//import org.firstinspires.ftc.teamcode.helpers.Coordinates;
//import org.firstinspires.ftc.teamcode.helpers.GameState;
//import org.firstinspires.ftc.teamcode.helpers.RouteState;
//import org.firstinspires.ftc.teamcode.helpers.TeamState;
//import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;
//
//@Autonomous(name = "AutonomousTest", group = "Autonomous")
//public class AutonomousTest extends BaseOpMode {
//
//    private GameState currentGameState;
//    private RouteState currentRouteState = RouteState.BOTTOM;
//    private Coordinates CURRENT_POSITION;
//    protected TeamState teamState;
//
//    private int level;
//
//    @Override
//    public void init() {
//        newGameState(GameState.INITIAL);
//        imuSystem = new ImuSystem(hardwareMap.get(BNO055IMU.class, "imu"));
//
//        CURRENT_POSITION = Coordinates.BLUE_STARTING_POSITION_BOTTOM;
//        motors.put(DriveSystemOther.MotorNames.FRONTRIGHT, hardwareMap.get(DcMotor.class, Constants.MOTOR_FRONT_RIGHT));
//        motors.put(DriveSystemOther.MotorNames.FRONTLEFT, hardwareMap.get(DcMotor.class, Constants.MOTOR_FRONT_LEFT));
//        motors.put(DriveSystemOther.MotorNames.BACKRIGHT, hardwareMap.get(DcMotor.class, Constants.MOTOR_BACK_RIGHT));
//        motors.put(DriveSystemOther.MotorNames.BACKLEFT, hardwareMap.get(DcMotor.class, Constants.MOTOR_BACK_LEFT));
//
//        driveSystem = new DriveSystemOther(motors, imuSystem);
//        elapsedTime = new ElapsedTime();
//    }
//
//    @Override
//    public void start() {
//        elapsedTime.reset();
//        newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
//    }
//
//    @Override
//    public void loop() {
//        telemetry.addData("GameState", currentGameState);
//        telemetry.update();
//        switch (currentGameState) {
//            case DRIVE_TO_ALLIANCE_HUB_ONE:
//                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
//                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
//                } */
//
//                if (driveSystem.driveToPosition(300, DriveSystemOther.Direction.FORWARD, 0.75)) {
//                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
//                }
//                break;
//            case DRIVE_TO_ALLIANCE_HUB_TWO:
//                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
//                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
//                } */
//
//                if (driveSystem.turn(currentRouteState == RouteState.TOP ? -85 : 85, 1.0)) {
//                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_THREE);
//                }
//                break;
//            case DRIVE_TO_ALLIANCE_HUB_THREE:
//                /* if (move(teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? 70 : 70, teamState == TeamState.RED ? -90 : 90)) {
//                    newGameState(currentRouteState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL);
//                } */
//
//                if (driveSystem.driveToPosition(300, DriveSystemOther.Direction.FORWARD, 0.75)) {
//                    if (currentRouteState == RouteState.TOP) {
//                        newGameState(GameState.PARK_IN_WAREHOUSE_ONE);
//                    } else {
//                        newGameState(GameState.DRIVE_TO_CAROUSEL_ONE);
//                    }
//                }
//                break;
//
//            case DRIVE_TO_CAROUSEL_ONE:
//                if (driveSystem.driveToPosition(840, DriveSystemOther.Direction.BACKWARD, 0.75)) {
//                    newGameState(GameState.DRIVE_TO_CAROUSEL_TWO);
//                }
//                break;
//
//            case DRIVE_TO_CAROUSEL_TWO:
//                if (driveSystem.driveToPosition(350, DriveSystemOther.Direction.RIGHT, 0.75)) {
//                    newGameState(GameState.PARK_IN_DEPOT);
//                }
//                break;
//
//            case PARK_IN_DEPOT:
//                if (driveSystem.driveToPosition(700, DriveSystemOther.Direction.LEFT, 0.75)) {
//                    newGameState(GameState.COMPLETE);
//                }
//                break;
//
//            case PARK_IN_WAREHOUSE_ONE:
//                if (driveSystem.driveToPosition(350, DriveSystemOther.Direction.LEFT, 0.75)) {
//                    newGameState(GameState.PARK_IN_WAREHOUSE_TWO);
//                }
//                break;
//
//            case PARK_IN_WAREHOUSE_TWO:
//                if (driveSystem.driveToPosition(1000, DriveSystemOther.Direction.BACKWARD, 0.75)) {
//                    newGameState(GameState.COMPLETE);
//                }
//                break;
//
//            case COMPLETE:
//                stop();
//                break;
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
//    protected boolean move(int ticksX, int ticksY, int rotation) {
//        driveSystem.driveToPositionTicks(ticksX, DriveSystemOther.Direction.FORWARD, 0.75);
//        return true;
//        /*if (driveSystemOther.driveToPositionTicks(ticksX, DriveSystemOther.Direction.FORWARD, 0.75)) {
//            if (driveSystemOther.turn(rotation, 1.0)) {
//                return driveSystemOther.driveToPositionTicks(ticksY, DriveSystemOther.Direction.FORWARD, 0.75);
//            }
//        }
//        return false;*/
//    }
//
//
//    protected void moveTicks(Coordinates redTeamCoords, Coordinates blueTeamCoords, int degrees) {
//        int deltaMM = (int) DriveSystemOther.TicksMM(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[0];
//        driveSystem.driveToPosition(deltaMM, DriveSystemOther.Direction.FORWARD, deltaMM/Math.abs(deltaMM));
//        Coordinates.updateX(teamState == TeamState.RED ? redTeamCoords.getX() : blueTeamCoords.getX());
//
//        driveSystem.turn(degrees, 1.0);
//
//        deltaMM = (int) DriveSystem.TicksCoordinate(Coordinates.CURRENT_POSITION, teamState == TeamState.RED ? redTeamCoords : blueTeamCoords)[1];
//        driveSystem.driveToPositionTicks(deltaMM, DriveSystemOther.Direction.FORWARD, deltaMM/Math.abs(deltaMM));
//        Coordinates.updateY(teamState == TeamState.RED ? redTeamCoords.getY() : blueTeamCoords.getY());
//    }
//}
