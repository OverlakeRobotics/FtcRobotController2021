package org.firstinspires.ftc.teamcode.opmodes.teleop;

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
public class PotentiometerTest extends BaseOpMode {

    private PotentiometerSystem potentiometerSystem;
    @Override
    public void init(){
        potentiometerSystem = new PotentiometerSystem(hardwareMap.get(AnalogInput.class, "p"));
        super.init();
    }

    @Override
    public void loop() {
        telemetry.addData("See: ", PotentiometerSystem.sensorAsAnalogInput0.getVoltage());
    }

}