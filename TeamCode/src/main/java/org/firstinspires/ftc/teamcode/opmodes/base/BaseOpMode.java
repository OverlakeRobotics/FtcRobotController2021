package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystemOther;
import org.firstinspires.ftc.teamcode.components.ImuSystem;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.TurnTableSystem;
import org.firstinspires.ftc.teamcode.helpers.Constants;
// import org.firstinspires.ftc.teamcode.components.WeightSystem;

import java.util.EnumMap;

// TODO - Finalize INTAKESYSTEM TO WORK PROPERLY TOGETHER EVERYWHERE
public abstract class BaseOpMode extends OpMode {

//    protected DriveSystem driveSystem;
    //protected WeightSystem weightSystem;
    protected ArmSystem armSystem;
   // protected WheelSystem wheelSystem;
    protected IntakeSystem intakeSystem;
    protected TurnTableSystem turnTableSystem;
    protected ImuSystem imuSystem;
    protected ElapsedTime elapsedTime;
    protected DriveSystemOther driveSystem;

    protected EnumMap<DriveSystemOther.MotorNames, DcMotor> motors = new EnumMap<>(DriveSystemOther.MotorNames.class);

    @Override
    public void init() {
        imuSystem = new ImuSystem(hardwareMap.get(BNO055IMU.class, Constants.IMU));

        motors.put(DriveSystemOther.MotorNames.FRONTRIGHT, hardwareMap.get(DcMotor.class, Constants.MOTOR_FRONT_RIGHT));
        motors.put(DriveSystemOther.MotorNames.FRONTLEFT, hardwareMap.get(DcMotor.class, Constants.MOTOR_FRONT_LEFT));
        motors.put(DriveSystemOther.MotorNames.BACKRIGHT, hardwareMap.get(DcMotor.class, Constants.MOTOR_BACK_RIGHT));
        motors.put(DriveSystemOther.MotorNames.BACKLEFT, hardwareMap.get(DcMotor.class, Constants.MOTOR_BACK_LEFT));

        armSystem = new ArmSystem(hardwareMap.get(DcMotor.class, Constants.ELEVATOR_MOTOR));
        driveSystem = new DriveSystemOther(motors, imuSystem);
        //weightSystem = new WeightSystem(
                //hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_ZERO),
                //hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_ONE),
                //hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_TWO),
                //hardwareMap.get(AnalogInput.class, WEIGHT_SENSOR_THREE),
                //hardwareMap.get(LED.class,WEIGHT_LIGHT_RED),
                //hardwareMap.get(LED.class,WEIGHT_LIGHT_GREEN));
       // wheelSystem = new WheelSystem(hardwareMap.get(DcMotor.class, INTAKE_MOTOR1));
        intakeSystem = new IntakeSystem(hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR1), hardwareMap.get(DcMotor.class, Constants.INTAKE_MOTOR2));
        turnTableSystem = new TurnTableSystem(hardwareMap.get(DcMotor.class, Constants.ROTATOR_MOTOR));
        elapsedTime = new ElapsedTime();
    }

    @Override
    public void start() {
        super.start();
        armSystem.initMotors();
        intakeSystem.initMotors();
        elapsedTime.reset();
    }
}
