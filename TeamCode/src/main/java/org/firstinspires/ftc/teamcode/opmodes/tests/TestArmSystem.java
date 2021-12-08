package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "ArmSystemTest", group = "TeleOp")
public abstract class TestArmSystem extends BaseOpMode {

    protected ArmSystem armSystem;

    private static final String ARM_MOTOR = "armMotor";

    int targetTicks = 0;



    @Override
    public void init(){
        super.init();
        //armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, ARM_MOTOR));
    }

    @Override
    public void start(){
        super.start();
        armSystem.initMotors();
    }

    @Override
    public void loop(){
        if(gamepad1.dpad_up){
            targetTicks++;
        }
        if(gamepad1.dpad_down) {
            targetTicks--;
        }
        armSystem.moveTo(targetTicks);
        telemetry.addData("ticks: %i ", targetTicks);
    }

}
