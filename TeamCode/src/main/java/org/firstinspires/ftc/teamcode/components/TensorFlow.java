package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.helpers.ObjectEnums;

import java.util.EnumMap;
import java.util.List;

public class TensorFlow {

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private TFObjectDetector tfod; //declaring objectDetector

    private static final String VUFORIA_KEY = "Ad0Srbr/////AAABmdpa0/j2K0DPhXQjE2Hyum9QUQXZO8uAVCNpwlogfxiVmEaSuqHoTMWcV9nLlQpEnh5bwTlQG+T35Vir8IpdrSdk7TctIqH3QBuJFdHsx5hlcn74xa7AiQSJgUD/n7JJ2zJ/Er5Hc+b+r616Jf1YU6RO63Ajk5+TFB9N3a85NjMD6eDm+C6f14647ELnmGC03poSOeczbX7hZpIEObtYdVyKZ2NQ/26xDfSwwJuyMgUHwWY6nl6mk0GMnIGvu0/HoGNgyR5EkUQWyx9XlmxSrldY7BIEVkiKmracvD7W9hEGZ2nPied6DTY5RFNuFX07io6+I59/d7291NXKVMDnFAqSt4a2JYsECv+j7b25S0mD";
    //parameters.useExtendedTracking = true;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        tfod.setZoom(3.0, 16/9);
    }

    /**
     * Constructor for TensorFlow
     */
    public TensorFlow(Vuforia vuforiaSystem) {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(); //creating parameters
        tfodParameters.minResultConfidence = 0.3f; //minimumConfidenceNecessaryForActingOnDetection
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforiaSystem.getVuforiaLocalizer()); //create objectDetector
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS[0], LABELS[1], LABELS[2], LABELS[3]); //loading models
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

        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
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

    /**
     * Returns the target region currently selected
     * @return the target region currently selected
     */
    public ObjectEnums getObject() {
        if (tfod == null) {
            return ObjectEnums.NADA;
        }
        List<Recognition> recognitionList = getInference();
        if (recognitionList.size() == 1 && recognitionList.get(0).getConfidence() >= 0.4) {
            if (recognitionList.get(0).getLabel().equals(LABELS[0])){
                return ObjectEnums.BALL;
            }
            if (recognitionList.get(0).getLabel().equals(LABELS[1])){
                return ObjectEnums.CUBE;
            }
            if (recognitionList.get(0).getLabel().equals(LABELS[2])){
                return ObjectEnums.DUCK;
            }
            if (recognitionList.get(0).getLabel().equals(LABELS[3])){
                return ObjectEnums.MARKER;
            }
        }
        return ObjectEnums.NADA;
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
