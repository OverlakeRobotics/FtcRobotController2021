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

    private static final String WHEEL_MOTOR = "wheelMotor";

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
                hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput0"),
                hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput1"),
                hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput2"),
                hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput3"),
                hardwareMap.get(LED.class,"weightIndicatorRed"),
                hardwareMap.get(LED.class,"weightIndicatorGreen"));
        wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, "wheelMotor"));
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, "intakeMotor"));
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
