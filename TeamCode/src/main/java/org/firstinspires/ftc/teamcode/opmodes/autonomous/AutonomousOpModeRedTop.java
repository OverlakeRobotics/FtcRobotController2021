package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlow;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.GameState;
import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpModeRedTop", group = "Autonomous")
public class AutonomousOpModeRedTop extends AutonomousOpMode {


    private int elevatorLevel = -3333333;
    private double baseTime;

    private GameState currentGameState;
    private Vuforia vuforia;
    private TensorFlow tensorFlow;

    private static final double driveSpeed = 0.5;
    private static final double rotateSpeed = 0.25;

    private static final int level = 2;

    public void init(TeamState teamState, RouteState routeState) {
        super.init();
        this.teamState = teamState;
        this.routeState = routeState;
        driveSystem.initMotors();
        vuforia = new Vuforia(hardwareMap.get(WebcamName.class, "Webcam 1"),0 );
        tensorFlow = new TensorFlow(vuforia);
        vuforia.activate();
        tensorFlow.activate();
        armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, Constants.ELEVATOR_MOTOR), hardwareMap.get(AnalogInput.class, "p"));
        armSystem.initMotors();
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR1), hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR2));
        intakeSystem.initMotors();
        turnTableSystem = new TurnTableSystem(hardwareMap.get(DcMotor.class, Constants.ROTATOR_MOTOR));
    }

    @Override
    public void init_loop() {
        super.init_loop();
        telemetry.addData("DUCK?", tensorFlow.seesDuck());
    }

    @Override
    public void start() {
        super.start();

        newGameState(GameState.SCAN_INITIAL);
    }

    @Override
    public void loop() {
        telemetry.addData("GameState", currentGameState);
        telemetry.addData("elevatorLevel", elevatorLevel);
        telemetry.addData("voltage", armSystem.getSensorAsAnalogInput0());
        telemetry.update();

        armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        switch (currentGameState) {
            case SCAN_INITIAL:
                if (level == 2) {
                    elevatorLevel = ArmSystem.LEVEL_CAROUSEL;
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
                } else {
                    newGameState(GameState.SCAN_SECONDARY);
                }
                break;
            case SCAN_SECONDARY:
                if (driveSystem.driveToPosition((int) (Constants.tileWidth * Constants.mmPerInch * (2/3)), DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    if (level == 3) {
                        elevatorLevel =  ArmSystem.LEVEL_TOP;
                        newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
                    } else {
                        elevatorLevel = ArmSystem.LEVEL_BOTTOM;
                    }
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_ONE:
                if (driveSystem.driveToPosition((int) ((Constants.tileWidth - 5) * Constants.mmPerInch),  DriveSystem.Direction.BACKWARD , driveSpeed)) {
                    armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    armSystem.stop();
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_TWO:
                if (driveSystem.driveToPosition((int) (0.75 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.LEFT, driveSpeed * 0.25)) {
                    armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    armSystem.stop();
                    newGameState(GameState.ROTATE_TURNTABLE);
                }
                break;
            case ROTATE_TURNTABLE:
                while (turnTableSystem.getPosition() != TurnTableSystem.LEVEL_90) {
                    armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    armSystem.stop();
                    turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_90);
                }
                baseTime = 0;
                newGameState(GameState.PLACE_CUBE);
                break;
            case PLACE_CUBE:
                if (elevatorLevel == -3333333){
                    double giveUpHope = Math.random();
                    if (giveUpHope < 0.33) {
                        elevatorLevel = ArmSystem.LEVEL_BOTTOM;
                    }
                    if (giveUpHope < 0.66) {
                        elevatorLevel =  ArmSystem.LEVEL_CAROUSEL;
                    }
                    else {
                        elevatorLevel = ArmSystem.LEVEL_TOP;
                    }
                }
                while (armSystem.notTooHigh()) {
                    armSystem.move_up();
                }
                if (baseTime == 0) {
                    baseTime = elapsedTime.seconds();
                }
                if (elapsedTime.seconds() < baseTime + 2.0) {
                    armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    armSystem.stop();
                    intakeSystem.spit_out();
                } else {
                    intakeSystem.setPower(0);
                    while (turnTableSystem.getPosition() != TurnTableSystem.LEVEL_0) {
                        armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        armSystem.stop();
                        turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
                    }
                    while (armSystem.getSensorAsAnalogInput0() < 1) {
                        armSystem.move_down();
                    }
                    baseTime = 0;
                    newGameState(GameState.PARK_IN_TOP_WAREHOUSE_ONE);
                }
                break;

            case PARK_IN_BOTTOM_WAREHOUSE:
                if (driveSystem.driveToPosition((int) (0.5 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    armSystem.stop();
                    newGameState(GameState.COMPLETE);
                }
                break;

            case PARK_IN_TOP_WAREHOUSE_ONE:
                if (driveSystem.driveToPosition((int) (0.5 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.RIGHT, driveSpeed)) {
                    newGameState(GameState.PARK_IN_TOP_WAREHOUSE_TWO);
                }
                break;

            case PARK_IN_TOP_WAREHOUSE_TWO:
                if (driveSystem.driveToPosition((int) (3 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.FORWARD, driveSpeed)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case COMPLETE:
                armSystem.moveToPosition(ArmSystem.LEVEL_TOP);
                armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                driveSystem.setMotorPower(0);
                turnTableSystem.moveToPosition( TurnTableSystem.LEVEL_90);
                armSystem.stop();
                telemetry.speak("among us among us among us");
                stop();
                break;
        }
    }

    @Override
    public void stop() {
        super.stop();
        tensorFlow.shutdown();
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