package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "DriveOpMode", group = "TeleOp")
public class DriveOpMode extends BaseOpMode {

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void loop() {

        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

        if(gamepad2.right_trigger > 0.5){
            while(turnTableSystem.getPosition() != TurnTableSystem.LEVEL_0){
                if (armSystem.getElevatorMotor().getCurrentPosition() <= ArmSystem.LEVEL_MID + 50) {
                    while (armSystem.getElevatorMotor().getCurrentPosition() != ArmSystem.LEVEL_TOP) {
                        armSystem.goToLevel(ArmSystem.LEVEL_TOP);
                    }
                }
                while(turnTableSystem.getPosition() != TurnTableSystem.LEVEL_0) {
                    turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
                }
            }
            armSystem.goToLevel(0);
            // Arm down to position for intake
        }
        if(gamepad2.dpad_right){
            turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
            telemetry.addData("ACTIVE", "turnTableSystem right");
        }
        if(gamepad2.dpad_left){
            turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_90);
            telemetry.addData("ACTIVE", "turnTableSystem left");
        }
        if(gamepad2.y){
            armSystem.goToLevel(ArmSystem.LEVEL_TOP);
            telemetry.addData("ACTIVE", "armSystem, TOP");
        }
        if(gamepad2.a){
            armSystem.goToLevel(ArmSystem.LEVEL_BOTTOM);
            telemetry.addData("ACTIVE", "armSystem, BOTTOM");
        }

        // y is highest, a is shared hub

        if (gamepad2.right_bumper) {
            intakeSystem.take_in();
            telemetry.addData("ACTIVE", "intake");
        }
        else if (gamepad2.left_bumper) {
            intakeSystem.spit_out();
            telemetry.addData("ACTIVE", "outtake");

        }
        else {
            intakeSystem.setPower(0.0);
        }

        if (gamepad1.right_bumper){
            turnTableSystem.moveClock();
            telemetry.addData("ACTIVE", "turntable clockwise");
        }
        if (gamepad1.left_bumper){
            turnTableSystem.moveCounter();
            telemetry.addData("ACTIVE", "turntable counterclockwise");

        }

        telemetry.addData("CurrentPosition: ", armSystem.getElevatorMotor().getCurrentPosition());

        if (gamepad1.dpad_up) {
            armSystem.move_up();
            telemetry.addData("ACTIVE", "armSystem up");
        }
        else if (gamepad1.dpad_down){
            armSystem.move_down();
            telemetry.addData("ACTIVE", "armSystem down");

        }
        else{
            armSystem.stop();
        }

        /*if (gamepad1.dpad_right) {
            turnTableSystem.move_right();
        }
        else if (gamepad1.dpad_left){
            turnTableSystem.move_left();
        }
        else{
            turnTableSystem.stop();
        }*/

        //weightSystem.checkWeight();

        driveSystem.drive(rx, -lx, ly);

        telemetry.addData("rx", rx);
        telemetry.addData("lx", lx);
        telemetry.addData("ly", ly);
        telemetry.addData("TIME_ELAPSED_MILSEC", elapsedTime.milliseconds());
        telemetry.addData("ARM", armSystem.getElevatorMotor().getCurrentPosition());
        telemetry.update();
    }

}