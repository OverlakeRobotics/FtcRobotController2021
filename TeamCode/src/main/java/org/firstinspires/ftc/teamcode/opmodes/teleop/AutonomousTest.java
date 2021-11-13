package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;
import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousTest", group = "Autonomous")
public class AutonomousTest extends OpMode {

    private static final String MOTOR_FRONT_RIGHT = "motor-front-right";
    private static final String MOTOR_FRONT_LEFT = "motor-front-left";
    private static final String MOTOR_BACK_RIGHT = "motor-back-right";
    private static final String MOTOR_BACK_LEFT = "motor-back-left";

    protected DriveSystem driveSystem;

    protected ElapsedTime elapsedTime;

    private GameState currentGameState;
    private RouteState currentRouteState;
    protected TeamState teamState;

    private int level;

    @Override
    public void init() {
        elapsedTime = new ElapsedTime();
        driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));
        driveSystem.initMotors();
    }

    @Override
    public void start() {
        super.start();
        elapsedTime.reset();
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
                move(Coordinates.RED_BOTTOM_CENTERBARCODE, Coordinates.BLUE_BOTTOM_CENTERBARCODE);
                newGameState(GameState.DETECT_BARCODE);
                break;

            case DETECT_BARCODE:
                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                if (level == 1) {
                    //move(Coordinates.RED_BOTTOM_LEFTBARCODE, Coordinates.BLUE_BOTTOM_RIGHTBARCODE);
                }
                if (level == 2) {
                    //move(Coordinates.RED_BOTTOM_RIGHTBARCODE, Coordinates.BLUE_BOTTOM_LEFTBARCODE);
                }
                if (level == 3) {
                    stop();
                }
                else {
                    level++;
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB:
                move(Coordinates.RED_ALLIANCE_HUB_BOTTOM, Coordinates.BLUE_ALLIANCE_HUB_BOTTOM);
                newGameState(GameState.PLACE_CUBE);
                break;

            case PLACE_CUBE:
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
}
