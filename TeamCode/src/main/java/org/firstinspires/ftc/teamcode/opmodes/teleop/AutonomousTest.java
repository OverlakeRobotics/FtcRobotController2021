package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.ImuSystem;
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
    private Coordinates CURRENT_POSITION;
    protected TeamState teamState;

    private int level;

    @Override
    public void init() {
        elapsedTime = new ElapsedTime();
        driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));
        driveSystem.initMotors();
        CURRENT_POSITION = Coordinates.BLUE_STARTING_POSITION_BOTTOM;
    }

    @Override
    public void start() {
        super.start();
        elapsedTime.reset();
        newGameState(GameState.DETECT_BARCODE);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);

        switch (currentGameState) {
            case DETECT_BARCODE:
                newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                break;
            case DRIVE_TO_ALLIANCE_HUB:
                moveTicks(Coordinates.RED_ALLIANCE_HUB_BOTTOM, Coordinates.BLUE_ALLIANCE_HUB_BOTTOM, 90);
                newGameState(GameState.PLACE_CUBE);
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
