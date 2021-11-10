package org.firstinspires.ftc.teamcode.components;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.LED;


public class WeightSystem {

    private AnalogInput sensorAsAnalogInput0;
    private AnalogInput sensorAsAnalogInput1;
    private AnalogInput sensorAsAnalogInput2;
    private AnalogInput sensorAsAnalogInput3;
    private LED weightIndicatorRed;
    private LED weightIndicatorGreen;


    public WeightSystem(AnalogInput input0, AnalogInput input1, AnalogInput input2, AnalogInput input3, LED red, LED green){
        sensorAsAnalogInput0 = input0; // hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput0")
        sensorAsAnalogInput1 = input1; // hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput1")
        sensorAsAnalogInput2 = input2; // hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput2")
        sensorAsAnalogInput3 = input3; // hardwareMap.get(AnalogInput.class, "sensorAsAnalogInput3")
        weightIndicatorRed = red; // hardwareMap.get(LED.class,"weightIndicatorRed")
        weightIndicatorGreen = green; // hardwareMap.get(LED.class,"weightIndicatorGreen")

        weightIndicatorGreen.enableLight(false); // light should be off at start
        weightIndicatorRed.enableLight(false);
    }

    public void checkWeight(){
        double currentVoltage = sensorAsAnalogInput0.getVoltage() + sensorAsAnalogInput1.getVoltage() + sensorAsAnalogInput2.getVoltage() + sensorAsAnalogInput3.getVoltage();


        // through testing - found that voltage reads 1.7 when theres nothing, 0 when there is something, hence the following classifications
        if(currentVoltage > 6){ // Light Block
            weightIndicatorGreen.enableLight(true);
            weightIndicatorRed.enableLight(false);
            // the light should be off when there is a light block
        }
        else if(currentVoltage > 5){ // Medium Block
            weightIndicatorRed.enableLight(true);
            weightIndicatorGreen.enableLight(false);
            // red light is on when there is a
        }
        else{ // Light Block
            weightIndicatorRed.enableLight(true);
            weightIndicatorGreen.enableLight(false);
            // light up green
        }

    }
}

