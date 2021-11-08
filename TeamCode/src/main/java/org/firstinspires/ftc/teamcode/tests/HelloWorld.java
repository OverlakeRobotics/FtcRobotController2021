package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;


@Autonomous(name = "hellWorld")
public class HelloWorld extends OpMode {

    private String str;
    private DcMotor motor1;
    private DcMotor motor2;

    @Override
    public void init() {
        str = "hello world";
        motor1 = hardwareMap.dcMotor.get("motor");
        //motor1.
        motor1.setTargetPosition(100000);
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor1.setPower(0.75);
        motor2 = hardwareMap.dcMotor.get("motor2");
        //motor1.
        motor2.setTargetPosition(100000);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setPower(-0.75);
    }

    @Override
    public void loop() {
        telemetry.addLine(str);
    }
}