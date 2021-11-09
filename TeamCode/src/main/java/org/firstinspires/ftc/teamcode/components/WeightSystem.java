package org.firstinspires.ftc.teamcode.components;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "WeightSystem_TEST", group = "TeleOp")
public class WeightSystem extends LinearOpMode {

    private AnalogInput sensorAsAnalogInput0;
    private AnalogInput sensorAsAnalogInput1;
    private AnalogInput sensorAsAnalogInput2;
    private AnalogInput sensorAsAnalogInput3;


  /*
   * This function is executed when this Op Mode is selected from the Driver Station.
   */


@Override
public void runOpMode() {
    sensorAsAnalogInput0 = hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput0");
    sensorAsAnalogInput1 = hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput1");
    sensorAsAnalogInput2 = hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput2");
    sensorAsAnalogInput3 = hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput3");

    // Configure digital pin for input mode.
    telemetry.addData("DigitalTouchSensor", "Press start to continue...");
    telemetry.update();
    waitForStart();
    if (opModeIsActive()) {
        // Put run blocks here.
        while (opModeIsActive()) {
            // button is pressed if value returned is LOW or false.
            telemetry.addData("Voltage0", sensorAsAnalogInput0.getVoltage());
            telemetry.addData("Voltage1", sensorAsAnalogInput1.getVoltage());
            telemetry.addData("Voltage2", sensorAsAnalogInput2.getVoltage());
            telemetry.addData("Voltage3", sensorAsAnalogInput3.getVoltage());
            telemetry.update();

            double currentVoltage = sensorAsAnalogInput0.getVoltage() + sensorAsAnalogInput1.getVoltage() + sensorAsAnalogInput2.getVoltage() + sensorAsAnalogInput3.getVoltage();


            // through testing - found that voltage reads 1.7 when theres nothing, 0 when there is something, hence the following classifications
            if(currentVoltage > 6){
                //say nothing was found
                telemetry.addData("LightBlock", currentVoltage);
            }
            else if(currentVoltage > 5){
                telemetry.addData("MediumBlock", currentVoltage);
            }
            else{
                telemetry.addData("HeavyBlock", currentVoltage);
            }

        }


    }

}
}

