package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystemOther;
import org.firstinspires.ftc.teamcode.components.ImuSystem;
import org.firstinspires.ftc.teamcode.components.WeightSystem;
import org.firstinspires.ftc.teamcode.components.WheelSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.EnumMap;

@TeleOp(name = "TeleOp", group = "TeleOp")
public class BasicTeleDrive extends OpMode {

    protected static final String MOTOR_FRONT_RIGHT = "motor-front-right";
    protected static final String MOTOR_FRONT_LEFT = "motor-front-left";
    protected static final String MOTOR_BACK_RIGHT = "motor-back-right";
    protected static final String MOTOR_BACK_LEFT = "motor-back-left";

    //protected DriveSystem driveSystem;
    protected DriveSystemOther driveSystemOther;
    protected ImuSystem imuSystem;

    protected EnumMap<DriveSystemOther.MotorNames, DcMotor> motors = new EnumMap<>(DriveSystemOther.MotorNames.class);

    protected ElapsedTime elapsedTime;

    @Override
    public void init() {
        imuSystem = new ImuSystem(hardwareMap.get(BNO055IMU.class, "imu"));

        motors.put(DriveSystemOther.MotorNames.FRONTRIGHT, hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT));
        motors.put(DriveSystemOther.MotorNames.FRONTLEFT, hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT));
        motors.put(DriveSystemOther.MotorNames.BACKRIGHT, hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT));
        motors.put(DriveSystemOther.MotorNames.BACKLEFT, hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));

        //driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));
        //driveSystem.initMotors();
        driveSystemOther = new DriveSystemOther(motors, imuSystem);
        elapsedTime = new ElapsedTime();
    }

    @Override
    public void start() {
        super.start();
        elapsedTime.reset();
    }

    @Override
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

        driveSystemOther.drive(rx, lx, ly);

        telemetry.addData("rx", rx);
        telemetry.addData("lx", lx);
        telemetry.addData("ly", ly);
        telemetry.addData("TIME_ELAPSED_MILSEC", elapsedTime.milliseconds());
        telemetry.update();
    }

}