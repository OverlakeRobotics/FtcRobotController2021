package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.components.WeightSystem;
import org.firstinspires.ftc.teamcode.components.WheelSystem;

public abstract class BaseOpMode extends OpMode {

    private static final String MOTOR_FRONT_RIGHT = "motor-front-right";
    private static final String MOTOR_FRONT_LEFT = "motor-front-left";
    private static final String MOTOR_BACK_RIGHT = "motor-back-right";
    private static final String MOTOR_BACK_LEFT = "motor-back-left";

    private static final String WEIGHT_SENSOR_ZERO = "sensorAsAnalogInput0";
    private static final String WEIGHT_SENSOR_ONE = "sensorAsAnalogInput1";
    private static final String WEIGHT_SENSOR_TWO = "sensorAsAnalogInput2";
    private static final String WEIGHT_SENSOR_THREE = "sensorAsAnalogInput3";
    private static final String WEIGHT_LIGHT_RED = "weightIndicatorRed";
    private static final String WEIGHT_LIGHT_GREEN = "weightIndicatorGreen";

    private static final String WHEEL_MOTOR = "wheelMotor";

    private static final String INTAKE_MOTOR = "intakeMotor";

    private static final String ROTATOR_MOTOR = "rotatorMotor";


    protected DriveSystem driveSystem;
    protected WeightSystem weightSystem;
    protected ArmSystem armSystem;
    protected WheelSystem wheelSystem;
    protected IntakeSystem intakeSystem;
    protected TurnTableSystem turnTableSystem;

    protected ElapsedTime elapsedTime;

    @Override
    public void init() {
        driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));
        driveSystem.initMotors();
        weightSystem = new WeightSystem(
                hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_ZERO),
                hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_ONE),
                hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_TWO),
                hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_THREE),
                hardwareMap.get(LED.class,WEIGHT_LIGHT_RED),
                hardwareMap.get(LED.class,WEIGHT_LIGHT_GREEN));
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, WHEEL_MOTOR));
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, INTAKE_MOTOR));
        turnTableSystem = new TurnTableSystem(hardwareMap.get(DcMotor.class, ROTATOR_MOTOR));
        elapsedTime = new ElapsedTime();
    }

    @Override
    public void start() {
        super.start();
        armSystem.initMotors();
        wheelSystem.initMotors();
        elapsedTime.reset();
    }
}
