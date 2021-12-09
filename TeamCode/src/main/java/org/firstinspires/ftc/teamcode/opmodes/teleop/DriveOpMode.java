package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.PotentiometerSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

@TeleOp(name = "DriveOpMode", group = "TeleOp")
public class DriveOpMode extends BaseOpMode {

    private int bool = -500;
    public boolean forTest = true;


    @Override
    public void init_loop(){
        telemetry.addData("Position: ", armSystem.getElevatorMotor().getCurrentPosition());
        telemetry.addData("Voltage ", armSystem.sensorAsAnalogInput0.getVoltage());
//        if (armSystem.sensorAsAnalogInput0.getVoltage() < 1.1 || armSystem.sensorAsAnalogInput0.getVoltage() > 1.2){
//            armSystem.moveToPosition(ArmSystem.LEVEL_BOTTOM);
//        }

    }

    @Override
    public void loop() {

       // telemetry.addData("See: ", PotentiometerSystem.sensorAsAnalogInput0.getVoltage());
        telemetry.addData("rotatorMotor", turnTableSystem.getPosition());
        telemetry.addData("elevator-motor", armSystem.getElevatorMotor().getCurrentPosition());
        telemetry.addData("motor-back-left", driveSystem.motors.get(DriveSystem.MotorNames.BACKLEFT).getCurrentPosition());
        telemetry.addData("motor-front-left", driveSystem.motors.get(DriveSystem.MotorNames.FRONTLEFT).getCurrentPosition());
        telemetry.addData("motor-back-right", driveSystem.motors.get(DriveSystem.MotorNames.BACKRIGHT).getCurrentPosition());
        telemetry.addData("motor-front-right", driveSystem.motors.get(DriveSystem.MotorNames.FRONTRIGHT).getCurrentPosition());

        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

//        if(gamepad2.right_trigger > 0.5){
//            while(turnTableSystem.getPosition() != TurnTableSystem.LEVEL_0){
//                if (armSystem.getElevatorMotor().getCurrentPosition() <= ArmSystem.LEVEL_MID + 50) {
//                    while (armSystem.getElevatorMotor().getCurrentPosition() != ArmSystem.LEVEL_TOP) {
//                        armSystem.moveTo(ArmSystem.LEVEL_TOP);
//                    }
//                }
//                while(turnTableSystem.getPosition() != TurnTableSystem.LEVEL_0) {
//                    turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
//                }
//            }
//            armSystem.goToLevel(0);
//            // Arm down to position for intake
//        }
        if(gamepad2.dpad_right){
            turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_0);
            telemetry.addData("ACTIVE", "turnTableSystem right");
        }
        if(gamepad2.dpad_left){
            turnTableSystem.moveToPosition(TurnTableSystem.LEVEL_90);
            telemetry.addData("ACTIVE", "turnTableSystem left");
        }
        if (gamepad2.y){
            bool = 1;
            //armSystem.getElevatorMotor().setTargetPosition(ArmSystem.LEVEL_TOP);
            telemetry.addData("ACTIVE", "armSystem, TOP");
        }
        else if (gamepad2.a){
            bool = -1;
            /*
            armSystem.getElevatorMotor().setTargetPosition(ArmSystem.LEVEL_BOTTOM);
            if(armSystem.getElevatorMotor().getCurrentPosition() > ArmSystem.LEVEL_BOTTOM){
                armSystem.getElevatorMotor().setPower(0.75);
            }
            else{
                armSystem.getElevatorMotor().setPower(0);
            }*/
            //armSystem.goToLevel(ArmSystem.LEVEL_BOTTOM);
            telemetry.addData("ACTIVE", "armSystem, BOTTOM");
        }
//        else{
//            armSystem.getElevatorMotor().setTargetPosition(armSystem.getElevatorMotor().getCurrentPosition());
//            armSystem.getElevatorMotor().setPower(0);
//        }

        /*
        if (bool == 1 && armSystem.getElevatorMotor().getCurrentPosition() < ArmSystem.LEVEL_TOP) {
            armSystem.move_up();
        }
        else if (bool == -1 && armSystem.getElevatorMotor().getCurrentPosition() > ArmSystem.LEVEL_BOTTOM){
            armSystem.move_down();
        }
        else{
            armSystem.stop();
            bool = 0;
        }
        */

//        if (bool == 1){
//            if ((armSystem.goToLevel(ArmSystem.LEVEL_TOP))){
//                bool = 0;
//            }
//        }
//        else if (bool == -1){
//            if ((armSystem.goToLevel(ArmSystem.LEVEL_BOTTOM))){
//                bool = 0;
//            }
//        }
//        else{
//            armSystem.getElevatorMotor().setPower(0);
//        }


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