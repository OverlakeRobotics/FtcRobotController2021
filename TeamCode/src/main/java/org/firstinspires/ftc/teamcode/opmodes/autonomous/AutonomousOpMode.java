
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
import org.firstinspires.ftc.teamcode.components.Vuforia;
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
    private static final String WEBCAM = "Webcam 1";

    private GameState currentGameState;
    protected TeamState teamState;

    private int level;

    // Systems
    private TensorFlow tensorflow;
    private Vuforia vuforia;

    @Override
    public void init() {
        newGameState(GameState.INITIAL);
        super.init();
        driveSystem.initMotors();
        vuforia = Vuforia.getInstance(hardwareMap.get(WebcamName.class, WEBCAM));
        tensorflow = new TensorFlow(vuforia);
        armSystem = new ArmSystem(hardwareMap.get(DcMotorEx.class, ELEVATOR_MOTOR), hardwareMap.get(Servo.class, RELEASER));
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, SPIN_WHEEL));
    }

    @Override
    public void start() {
        super.start();
        vuforia.activate();
        tensorflow.activate();
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
                if (level == 1) {
                    //move(Coordinates.RED_BOTTOM_LEFTBARCODE, Coordinates.BLUE_BOTTOM_RIGHTBARCODE);
                }
                if (level == 2) {
                    move(Coordinates.RED_BOTTOM_RIGHTBARCODE, Coordinates.BLUE_BOTTOM_LEFTBARCODE);
                }
                if (level == 3) {
                    stop();
                }
                for (Recognition recognition : tensorflow.getInference()) {
                    if (recognition.getLabel().equals("Marker")) {
                        newGameState(GameState.DRIVE_TO_ALLIANCE_HUB);
                    } else {
                        level++;
                    }
                }
                break;

            case DRIVE_TO_ALLIANCE_HUB:
                move(Coordinates.RED_ALLIANCE_HUB, Coordinates.BLUE_ALLIANCE_HUB);
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
                move(Coordinates.RED_CAROUSEL, Coordinates.BLUE_CAROUSEL);
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

            case COMPLETE:
                stop();
                break;
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
