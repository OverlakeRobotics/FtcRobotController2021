//package org.firstinspires.ftc.teamcode.opmodes.teleop;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.AnalogInput;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import org.firstinspires.ftc.teamcode.components.DriveSystem;
//import org.firstinspires.ftc.teamcode.components.PotentiometerSystem;
//import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
//import org.firstinspires.ftc.teamcode.helpers.Constants;
//import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;
//
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
//
//@TeleOp(name = "PotentiometerOpMode", group = "TeleOp")
//public class PotentiometerTest extends BaseOpMode {
//
//    private AnalogInput analogInput;
//    @Override
//    public void init(){
//        this.analogInput = hardwareMap.get(AnalogInput.class, "p");
//        super.init();
//    }
//
//    @Override
//    public void loop() {
//        telemetry.addData("See: ", analogInput.getVoltage());
//    }
//
//}