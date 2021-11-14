package org.firstinspires.ftc.teamcode.opmodes.tests;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "SpinSpeedTest", group = "TeleOp")
public abstract class TestOfSpinningSpeedCarouselIntake extends BaseOpMode {

      protected IntakeSystem intakeSystem;

      private static final String INTAKE_MOTOR1 = "intakeMotor1";
      private static final String INTAKE_MOTOR2 = "intakeMotor2";
      private double dub = 0.1;

      @Override
      public void init(){
          intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, INTAKE_MOTOR1), hardwareMap.get(DcMotor.class, INTAKE_MOTOR2));
      }

      @Override
      public void start(){
          super.start();
          intakeSystem.initMotors();
      }

      @Override
      public void loop(){
          if(gamepad1.a) {
              dub += 0.1;
//              intakeSystem.Carousel();
              intakeSystem.Carousel(dub);
              telemetry.addData("carouselPower %d", dub);
          }
      }

}
