
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

public abstract class AutonomousOpMode extends BaseOpMode {


    private int elevatorLevel = -3333333;
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
        armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, Constants.ELEVATOR_MOTOR), hardwareMap.get(AnalogInput.class, "p"));
        armSystem.initMotors();
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
                    elevatorLevel = ArmSystem.LEVEL_CAROUSEL;
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
                } else {
                    newGameState(GameState.SCAN_SECONDARY);
                }
                break;
            case SCAN_SECONDARY:
                if (driveSystem.driveToPosition((int) (Constants.tileWidth * Constants.mmPerInch * (2/3)), isRBorBT ? DriveSystem.Direction.FORWARD : DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    if (level == 3) {
                        elevatorLevel = isRBorBT ? ArmSystem.LEVEL_BOTTOM : ArmSystem.LEVEL_TOP;
                        newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
                    } else {
                        elevatorLevel = isRBorBT ? ArmSystem.LEVEL_TOP : ArmSystem.LEVEL_BOTTOM;
                    }
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_ONE);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_ONE:
                if (driveSystem.driveToPosition((int) ((Constants.tileWidth - 5) * Constants.mmPerInch), DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    newGameState(GameState.DRIVE_TO_ALLIANCE_HUB_TWO);
                }
                break;
            case DRIVE_TO_ALLIANCE_HUB_TWO:
                if (driveSystem.driveToPosition((int) (0.8 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.LEFT, driveSpeed * 0.5)) {
                    newGameState(GameState.ROTATE_TURNTABLE);
                }
                break;
            case ROTATE_TURNTABLE:
                while (turnTableSystem.getPosition() != TurnTableSystem.LEVEL_90) {
                    turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_90);
                }
                baseTime = 0;
                newGameState(GameState.PLACE_CUBE);
                break;
            case PLACE_CUBE:
//                if (elevatorLevel == -3333333){
//                    double giveUpHope = Math.random();
//                    if (giveUpHope < 0.33) {
//                        elevatorLevel = ArmSystem.LEVEL_BOTTOM;
//                    }
//                    if (giveUpHope < 0.66) {
//                        elevatorLevel =  ArmSystem.LEVEL_CAROUSEL;
//                    }
//                    else {
//                        elevatorLevel = ArmSystem.LEVEL_TOP;
//                    }
//                }
                armSystem.moveToPosition(elevatorLevel);
                if (baseTime == 0) {
                    baseTime = elapsedTime.seconds();
                }
                if (elapsedTime.seconds() < baseTime + 2.0) {
                    armSystem.stop();
                    intakeSystem.spit_out();
                } else {
                    intakeSystem.setPower(0);
                    while (turnTableSystem.getPosition() != TurnTableSystem.LEVEL_0) {
                        turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
                    }
                    newGameState(routeState == RouteState.TOP ? GameState.PARK_IN_WAREHOUSE : GameState.DRIVE_TO_CAROUSEL_ONE);
                }
                break;
            case DRIVE_TO_CAROUSEL_ONE:
                if (driveSystem.turn(78, rotateSpeed)) {
                    newGameState(GameState.DRIVE_TO_CAROUSEL_TWO);
                }
                break;

            case DRIVE_TO_CAROUSEL_TWO:
                if (driveSystem.driveToPosition((int) (2.25 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.LEFT, driveSpeed)) {
                    armSystem.moveToPosition(ArmSystem.LEVEL_CAROUSEL);
                    newGameState(GameState.DRIVE_TO_CAROUSEL_THREE);
                }
                break;
            case DRIVE_TO_CAROUSEL_THREE:
                if (driveSystem.driveToPosition((int) (4.25 * Constants.mmPerInch), DriveSystem.Direction.FORWARD, driveSpeed * 0.5)) {
                    newGameState(GameState.SPIN_CAROUSEL);
                    baseTime = 0;
                }
                break;
            case SPIN_CAROUSEL:
                if (baseTime == 0) {
                    baseTime = elapsedTime.seconds();
                }
                armSystem.stop();
                if (elapsedTime.seconds() < baseTime + 6.0) {
                    intakeSystem.Carousel(teamState);
                } else {
                    intakeSystem.setPower(0);
                    newGameState(GameState.PARK_IN_WAREHOUSE);
                }
                break;

            case PARK_IN_WAREHOUSE:
                if (driveSystem.driveToPosition((int) (0.5 * Constants.tileWidth * Constants.mmPerInch), DriveSystem.Direction.BACKWARD, driveSpeed)) {
                    newGameState(GameState.COMPLETE);
                }
                break;

            case COMPLETE:
                armSystem.moveToPosition(0);
                driveSystem.setMotorPower(0);
                turnTableSystem.moveToPosition(-1 * TurnTableSystem.LEVEL_90);
                armSystem.stop();
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