package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "BasicTeleDrive_TEST", group = "TeleOp")
public class BasicTeleDrive extends BaseOpMode {

    private boolean bool = false;
    private boolean intakeOn = false;
    private boolean outtakeOn = false;
    private boolean carouselOn = false;

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

        if (gamepad1.y) {
            intakeSystem.take_in();
        }
        else if (gamepad1.b) {
            intakeSystem.spit_out();
        }
        else if (gamepad1.x) {
            intakeSystem.Carousel();
        }
        else {
            intakeSystem.setPower(0.0);
        }

        if (gamepad1.right_bumper){
            turnTableSystem.moveClock();
        }
        if (gamepad1.left_bumper){
            turnTableSystem.moveCounter();
        }

        if (gamepad1.dpad_up) {
            armSystem.move_up();
        }
        else if (gamepad1.dpad_down){
            armSystem.move_down();
        }
        else{
            armSystem.stop();
        }

        //weightSystem.checkWeight();

        driveSystem.drive(rx, -lx, ly);

        telemetry.addData("rx", rx);
        telemetry.addData("lx", lx);
        telemetry.addData("ly", ly);
        telemetry.addData("TIME_ELAPSED_MILSEC", elapsedTime.milliseconds());
        telemetry.update();
    }

}