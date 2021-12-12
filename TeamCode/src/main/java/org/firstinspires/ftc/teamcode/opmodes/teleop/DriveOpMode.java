package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.PotentiometerSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.helpers.TeamState;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public abstract class DriveOpMode extends BaseOpMode {

    TeamState teamState;


    public void init(TeamState teamState) {
        super.init();
        this.teamState = teamState;
    }

    @Override
    public void loop() {

        telemetry.addData("See: ", armSystem.getSensorAsAnalogInput0());
        telemetry.addData("rotatorMotor", turnTableSystem.getPosition());
        telemetry.addData("elevator-motor", armSystem.getElevatorMotor().getCurrentPosition());
        telemetry.addData("motor-back-left", driveSystem.motors.get(DriveSystem.MotorNames.BACKLEFT).getCurrentPosition());
        telemetry.addData("motor-front-left", driveSystem.motors.get(DriveSystem.MotorNames.FRONTLEFT).getCurrentPosition());
        telemetry.addData("motor-back-right", driveSystem.motors.get(DriveSystem.MotorNames.BACKRIGHT).getCurrentPosition());
        telemetry.addData("motor-front-right", driveSystem.motors.get(DriveSystem.MotorNames.FRONTRIGHT).getCurrentPosition());

        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

        if (gamepad2.dpad_right){
            turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
            telemetry.addData("ACTIVE", "turnTableSystem right");
        } else if (gamepad2.dpad_left){
            turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_90);
            telemetry.addData("ACTIVE", "turnTableSystem left");
        } else if (gamepad1.right_bumper){
            turnTableSystem.moveClock();
            telemetry.addData("ACTIVE", "turntable clockwise");
        } else if (gamepad1.left_bumper){
            turnTableSystem.moveCounter();
            telemetry.addData("ACTIVE", "turntable counterclockwise");
        } else {
            turnTableSystem.stop();
        }


        /*if (gamepad2.y){
            armSystem.moveToPosition(ArmSystem.LEVEL_TOP);
            telemetry.addData("ACTIVE", "armSystem, TOP");
        } else if (gamepad2.a){
            armSystem.moveToPosition(ArmSystem.LEVEL_INTAKE);
            telemetry.addData("ACTIVE", "armSystem, BOTTOM");
        } else if (gamepad2.x){
            armSystem.moveToPosition(ArmSystem.LEVEL_BOTTOM);
        } else if (gamepad2.b){
            armSystem.moveToPosition(ArmSystem.LEVEL_CAROUSEL);
        }*/ if (gamepad2.dpad_up) {
            armSystem.move_up();
            telemetry.addData("ACTIVE", "armSystem up");
        } else if (gamepad2.dpad_down){
            armSystem.move_down();
            telemetry.addData("ACTIVE", "armSystem down");
        } else {
            armSystem.stop();
            armSystem.getElevatorMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        if (gamepad2.right_bumper) {
            intakeSystem.take_in();
            telemetry.addData("ACTIVE", "intake");
        } else if (gamepad2.left_bumper) {
            intakeSystem.spit_out();
            telemetry.addData("ACTIVE", "outtake");
        } else if (gamepad2.right_stick_button) {
            intakeSystem.Carousel(teamState);
            telemetry.addData("ACTIVE", "carousel");
        } else {
            intakeSystem.setIdle();
        }

        driveSystem.drive(rx, -lx, ly);

        telemetry.addData("rx", rx);
        telemetry.addData("lx", lx);
        telemetry.addData("ly", ly);
        telemetry.addData("TIME_ELAPSED_MILSEC", elapsedTime.milliseconds());
        telemetry.update();
    }

}