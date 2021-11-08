package org.firstinspires.ftc.teamcode.components;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class WeightSystem extends LinearOpMode {

  private AnalogInput sensorAsAnalogInput;

  /*
   * This function is executed when this Op Mode is selected from the Driver Station.
   */


@Override
public void runOpMode() {
    sensorAsAnalogInput = hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput");

    // Configure digital pin for input mode.
    telemetry.addData("DigitalTouchSensorExample", "Press start to continue...");
    telemetry.update();
    waitForStart();
    if (opModeIsActive()) {
        // Put run blocks here.
        while (opModeIsActive()) {
            // button is pressed if value returned is LOW or false.
            telemetry.addData("Voltage", sensorAsAnalogInput.getVoltage());
            telemetry.update();
        }
    }
}
}

