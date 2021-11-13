package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Target;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Real Teleop", group="TeleOp")
public class DriveTeleOp extends BaseOpMode {

    private ArmSystem armSystem;
    private DriveSystem driveSystem;
    private TurnTableSystem turnTableSystem;
    private IntakeSystem intakeSystem;


    private static final String MOTOR_FRONT_RIGHT = "motor-front-right";
    private static final String MOTOR_FRONT_LEFT = "motor-front-left";
    private static final String MOTOR_BACK_RIGHT = "motor-back-right";
    private static final String MOTOR_BACK_LEFT = "motor-back-left";



    private static final String MOTOR_TURNTABLE = "motor-turn-table";


    // FunctionsList
    private enum Functions {
        RAISE_ARM,
        RAISE_ARM_AFTER_YEET,
        LOWER_ARM,
        SHOOT_POWERSHOTS,
        SUCK,
        SHOOT
    }

    // Variables
    private Set<Functions> calledFunctions;
    private boolean isSucking;

    // Systems
    private boolean suckWasPressed = false;
    private boolean shooterWasPressed = false;
    private boolean shouldStartShooter = true;

    @Override
    public void init() {
        super.init();
        try {
            intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, "IntakeSystem"));
            driveSystem = new DriveSystem(hardwareMap.get(DcMotor.class, MOTOR_FRONT_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_FRONT_LEFT), hardwareMap.get(DcMotor.class, MOTOR_BACK_RIGHT), hardwareMap.get(DcMotor.class, MOTOR_BACK_LEFT));

        } catch (Exception e) {
            telemetry.addData(Constants.ROBOT_SYSTEM_ERROR, e.getStackTrace());
        }
        calledFunctions = new HashSet<>();
    }

    @Override
    public void loop() {
        //TODO implement gamepad mechanics
        // Drive
        float rx = (float) Math.pow(gamepad1.right_stick_x, 1);
        float lx = -(float) Math.pow(gamepad1.left_stick_x, 1);
        float ly = -(float) Math.pow(gamepad1.left_stick_y, 1);
        driveSystem.joystickDrive(rx, lx, ly);

        // Executes loaded functions
        Iterator<Functions> i = calledFunctions.iterator();
        while (i.hasNext()) {
            Functions f = i.next();
            switch (f) {
                case RAISE_ARM:
                    if (yeetSystem.pickedUp(true)) {
                        i.remove();
                    }
                    break;

                case RAISE_ARM_AFTER_YEET:
                    if (yeetSystem.pickedUp(false)) {
                        i.remove();
                    }
                    break;

                case LOWER_ARM:
                    if (yeetSystem.placed()) {
                        i.remove();
                    }
                    break;

                case SHOOT:
                    if (shootingSystem.shoot()) {
                        i.remove();
                    }
                    break;

                case SHOOT_POWERSHOTS:
                    if (roadRunnerDriveSystem.shootPowerShots(shootingSystem)) {
                        i.remove();
                    }
                    break;
            }
        }

        if (gamepad1.dpad_up) {
            calledFunctions.add(Functions.SHOOT_POWERSHOTS);
        }

        // IntakeSystem
        if (gamepad1.a) {
            if (!suckWasPressed) {
                isSucking = !isSucking;
            }
            suckWasPressed = true;
        } else {
            suckWasPressed = false;
        }

        if (gamepad1.dpad_left) {
            yeetSystem.setPower(-0.3);
        }

        if (gamepad1.back) {
            yeetSystem.reset();
        }

        if (gamepad1.x || gamepad1.y) {
            if (!shooterWasPressed) {
                if (shouldStartShooter) {
                    if (gamepad1.y) {
                        shootingSystem.warmUp(Target.POWER_SHOT);
                    } else {
                        shootingSystem.warmUp(Target.TOWER_GOAL);
                    }
                } else {
                    shootingSystem.shutDown();
                }
                shouldStartShooter = !shouldStartShooter;
            }
            shooterWasPressed = true;
        } else {
            shooterWasPressed = false;
        }

        if (isSucking) {
            intakeSystem.suck();
            if (gamepad1.right_trigger > 0.3) {
                intakeSystem.unsuck();
            }
        } else {
            intakeSystem.stop();
        }

        // YeetSystem
        if (gamepad1.left_bumper) {
            calledFunctions.add(Functions.RAISE_ARM);
        }

        if (gamepad1.left_trigger > 0.3f) {
            calledFunctions.add(Functions.RAISE_ARM_AFTER_YEET);
        }

        if (gamepad1.right_bumper) {
            calledFunctions.add(Functions.LOWER_ARM);
        }

        // ShootingSystem
        if (gamepad1.b) {
            isSucking = false;
            calledFunctions.add(Functions.SHOOT);
        }
    }
}