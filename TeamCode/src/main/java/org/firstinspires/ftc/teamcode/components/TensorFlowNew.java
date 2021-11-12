package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.helpers.ObjectEnums;

import java.util.EnumMap;
import java.util.List;

public class TensorFlowNew {

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Cube",
            "Duck",
    };

    private TFObjectDetector tfod; //declaring objectDetector

    /**
     * Constructor for TensorFlow
     */
    public TensorFlowNew(Vuforia vuforiaSystem) {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(); //creating parameters
        tfodParameters.minResultConfidence = 0.3f; //minimumConfidenceNecessaryForActingOnDetection
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforiaSystem.getVuforiaLocalizer()); //create objectDetector
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS[0], LABELS[1]); //loading models
        tfod.activate(); //turnOn
    }

    /** Method getInference()
     *
     * Returns a list of Tensoflow recognitions
     *
     * A recognition is, from my understanding, TensorFlow having identifying
     * an object as one of its labels - the thing it should be looking for.
     *
     * In a sense, this is sort of a 'picture' of all of the relevant
     * details or aspects of the picture. This is good so that we can
     * iterate through these Recognitions and find important information,
     * such as which label (or type) - amongst other things - these Recognitions are.
     *
     * Your garbage explanation brought to you by: @anishch
     */

    public List<Recognition> getInference() { //get "image" back, a bunch of Recognitions - check out instance variables
        if (tfod != null) {
            return tfod.getRecognitions(); //Returns the list of recognitions, but only if they are different than the last call to {@link #getUpdatedRecognitions()}.
        }
        return null;
    }

    /**
     * Activates TensorFlow
     */
    public void activate() {
        if (tfod != null) {
            tfod.activate();
        }

    }

    /**
     * Shuts down TensorFlow
     */
    public void shutdown() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }


    public ArmSystem.ElevatorState getObjectNew() {
        List<Recognition> recognitionList = getInference();
//        int qDuck = -1;
//        int qCube = -1;
//        int type = -1;
//        int i = -1;
        if (recognitionList.size() >= 1){
            for (Recognition recognitions : recognitionList){
                if (recognitions.getConfidence() >= 0.4 && recognitions.getHeight() < 5.3){ /* TODO CHANGE */
                    if (recognitions.getLeft() < 12){ // TODO VALUES CHANGE
                        // DO NOTHING
                    }
                    if (recognitions.getLeft() < 34){
                        return ArmSystem.ElevatorState.LEVEL_TOP;
                    }
                    else if (recognitions.getLeft() < 53){
                        return ArmSystem.ElevatorState.LEVEL_MID;
                    }
                    else if (recognitions.getLeft() < 89){
                        return ArmSystem.ElevatorState.LEVEL_BOTTOM;
                    }
                    else{

                    }
                }
            }
        }
        double giveUpHope = Math.random();
        if (giveUpHope < 0.33){
            return ArmSystem.ElevatorState.LEVEL_BOTTOM;
        }
        if (giveUpHope < 0.66){
            return ArmSystem.ElevatorState.LEVEL_MID;
        }
        else{
            return ArmSystem.ElevatorState.LEVEL_TOP;
        }
    }
}

/*

/***
!pip install -q tflite-model-maker
 import os
 import numpy as np
 import tensorflow as tf
 assert tf.__version__.startswith('2')
 from tflite_model_maker import model_spec
 from tflite_model_maker import image_classifier
 from tflite_model_maker.config import ExportFormat
 from tflite_model_maker.config import QuantizationConfig
 from tflite_model_maker.image_classifier import DataLoader
 import matplotlib.pyplot as plt
 image_path = tf.keras.utils.get_file(
 'food_photos (1).zip',
 'https://drive.google.com/uc?id=1ca4AudLRLl8epNKipxjTIKrsRtUP7a51&export=download',
 extract=True)
 image_path = os.path.join(os.path.dirname(image_path), 'food_photos')
 from google.colab import drive
 drive.mount('/content/drive')
 data = DataLoader.from_folder(image_path)
 train_data, test_data = data.split(0.9)
 model = image_classifier.create(train_data, batch_size=1)
 loss, accuracy = model.evaluate(test_data)
 model.export(export_dir='.')
 ***/
