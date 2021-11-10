package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.WeightSystem;

public abstract class BaseOpMode extends OpMode {

    private static final String MOTOR_FRONT_RIGHT = "motor-front-right";
    private static final String MOTOR_FRONT_LEFT = "motor-front-left";
    private static final String MOTOR_BACK_RIGHT = "motor-back-right";
    private static final String MOTOR_BACK_LEFT = "motor-back-left";

    protected DriveSystem driveSystem;
    protected WeightSystem weightSystem;

    protected ElapsedTime elapsedTime;
    protected double baseTime;
    protected double deltaTime;

    @Override
    public void init() {
        driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));
        driveSystem.initMotors();
        weightSystem = new WeightSystem(hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput0"), hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput1"), hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput2"), hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput3"), hardwareMap.get(LED.class,"weightIndicatorRed"), hardwareMap.get(LED.class,"weightIndicatorGreen"));
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
    }
}
