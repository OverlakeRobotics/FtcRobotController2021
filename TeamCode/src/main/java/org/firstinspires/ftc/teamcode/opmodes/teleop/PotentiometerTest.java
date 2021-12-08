package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.PotentiometerSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

@TeleOp(name = "PotentiometerOpMode", group = "TeleOp")
public class PotentiometerTest extends OpMode {

    private AnalogInput potentiometerSystem;
    private DcMotor elevatorMotor;
    @Override
    public void init(){
        potentiometerSystem = hardwareMap.get(AnalogInput.class, "p");
        elevatorMotor = hardwareMap.get(DcMotor.class, Constants.ELEVATOR_MOTOR);
        elevatorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        telemetry.addData("See: ", potentiometerSystem.getVoltage());
        telemetry.addData("Motor position: ", elevatorMotor.getCurrentPosition());
    }



}