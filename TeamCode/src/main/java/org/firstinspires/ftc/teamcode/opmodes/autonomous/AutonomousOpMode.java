
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.hardware.AnalogInput;
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

public abstract class AutonomousOpMode extends BaseOpMode {

    /*
    * ROBOT CONFIG
    *
    * Control Hub
    * Port 0 - NeveRest 40 Gearmotor - motor-back-left
    * Port 1 - NeveRest 40 Gearmotor - motor-front-left
    * Port 2 - NeveRest 40 Gearmotor - motor-back-right
    * Port 3 - NeveRest 40 Gearmotor - motor-front-right
    *
    * Expansion Hub
    * Port 0 - NeveRest 40 Gearmotor - intakeMotor2
    * Port 1 - NeveRest 40 Gearmotor - intakeMotor1
    * Port 2 - NeveRest 40 Gearmotor - rotatorMotor
    * Port 3 - NeveRest 40 Gearmotor - elevator-motor
    * */

    private int elevatorLevel;
    private boolean isRBorBT;

    private GameState currentGameState;
    private RouteState routeState;
    protected TeamState teamState;

    private static final double driveSpeed = 0.5;
    private static final double rotateSpeed = 0.25;

    // Systems
    private TensorFlow tensorflow;
    private TensorFlowNew tensorflowNew;
    private Vuforia vuforia;

    public void init(TeamState teamState, RouteState routeState) {
        isRBorBT = ((teamState == TeamState.BLUE && this.routeState == RouteState.TOP) || (teamState == TeamState.RED && this.routeState == RouteState.BOTTOM));
        super.init();
        this.teamState = teamState;
        this.routeState = routeState;
        driveSystem.initMotors();
        vuforia = Vuforia.getInstance();
        tensorflow = new TensorFlow(vuforia);
        tensorflowNew = new TensorFlowNew(vuforia);
        armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, Constants.ELEVATOR_MOTOR), hardwareMap.get(AnalogInput.class, "p"));
        armSystem.initMotors();
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR1), hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR2));
        intakeSystem.initMotors();
        turnTableSystem = new TurnTableSystem(hardwareMap.get(DcMotor.class, Constants.ROTATOR_MOTOR));
    }

    @Override
    public void start() {
        super.start();
        vuforia.activate();
        //tensorflow.activate();

        newGameState(GameState.SCAN_INITIAL);
    }

    @Override
    public void loop() {


        telemetry.addData("GameState", currentGameState);
        telemetry.addData("elevatorLevel", elevatorLevel);
        telemetry.addData("DUCK???", tensorflowNew.seesDuck());
        telemetry.addData("ANYTHING?????", tensorflowNew.seeAnything());
        telemetry.update();

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
