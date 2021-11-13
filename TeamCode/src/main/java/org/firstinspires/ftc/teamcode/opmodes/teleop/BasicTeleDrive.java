package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "BasicTeleDrive_TEST", group = "TeleOp")
public class BasicTeleDrive extends BaseOpMode {

    private boolean bool = false;
    @Override
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

        if (gamepad1.b){
            bool = !bool;
        }
        if (bool) {
            intakeSystem.take_in();
        }
        else{
            intakeSystem.spit_out();
        }

        if (gamepad1.cross){
            intakeSystem.Carousel();
        }

        while (gamepad1.right_bumper){
            turnTableSystem.moveClock();
        }
        while (gamepad1.left_bumper){
            turnTableSystem.moveCounter();
        }

        if (gamepad1.dpad_up){
            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_TOP);
        }
        if (gamepad1.dpad_down){
            armSystem.goToLevel(ArmSystem.ElevatorState.LEVEL_BOTTOM);
        }

        weightSystem.checkWeight();

        driveSystem.joystickDrive(rx, lx, ly);

        telemetry.addData("rx", rx);
        telemetry.addData("lx", lx);
        telemetry.addData("ly", ly);
        telemetry.addData("TIME_ELAPSED_MILSEC", elapsedTime.milliseconds());
        telemetry.update();
    }

}