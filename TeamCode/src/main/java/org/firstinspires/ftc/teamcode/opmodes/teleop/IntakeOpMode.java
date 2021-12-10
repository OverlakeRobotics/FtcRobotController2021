package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "amogus", group = "TeleOp")
public class IntakeOpMode extends BaseOpMode {

    @Override
    public void loop() {
        if (gamepad1.left_bumper) {
            intakeSystem.motorLeft.setPower(1.0);
        }
        if (gamepad1.right_bumper) {
            intakeSystem.motorRight.setPower(1.0);
        }
    }
}