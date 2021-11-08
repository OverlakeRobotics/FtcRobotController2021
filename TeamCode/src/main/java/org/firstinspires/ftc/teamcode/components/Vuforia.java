package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.helpers.Constants;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/** Teddy, trust my code, you blasphemer. */
public class Vuforia {

    private VuforiaLocalizer vuforiaLocalizer;
    private OpenGLMatrix lastLocation; // class members
    public static VuforiaTrackables trackables;
    public static VuforiaTrackable redAllianceTarget; // declare objects that need
    private VuforiaTrackableDefaultListener listener;
    private static Vuforia instance;
    private static final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = VuforiaLocalizer.CameraDirection.BACK;

    /**
     * @param webcamName to use for vuforia
     * @return instance of vuforia singleton
     */
    public static Vuforia getInstance(WebcamName webcamName) {
        if (instance == null) instance = new Vuforia(webcamName);
        return instance;
    }

    /**
     * @return instance of vuforia singleton
     */
    public static Vuforia getInstance() {
        if (instance == null) instance = new Vuforia(null);
        return instance;
    }

    /**
     * Constructor
     * @param webcamName to use for init
     */
    private Vuforia(WebcamName webcamName) {
        initVuforiaLocalizer(webcamName);
        initUltsGoal(webcamName);
    }

    /**
     * Initializes the vuforia localizer
     * @param webcamName to use for init
     */
    private void initVuforiaLocalizer(WebcamName webcamName) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "Ad0Srbr/////AAABmdpa0/j2K0DPhXQjE2Hyum9QUQXZO8uAVCNpwlogfxiVmEaSuqHoTMWcV9nLlQpEnh5bwTlQG+T35Vir8IpdrSdk7TctIqH3QBuJFdHsx5hlcn74xa7AiQSJgUD/n7JJ2zJ/Er5Hc+b+r616Jf1YU6RO63Ajk5+TFB9N3a85NjMD6eDm+C6f14647ELnmGC03poSOeczbX7hZpIEObtYdVyKZ2NQ/26xDfSwwJuyMgUHwWY6nl6mk0GMnIGvu0/HoGNgyR5EkUQWyx9XlmxSrldY7BIEVkiKmracvD7W9hEGZ2nPied6DTY5RFNuFX07io6+I59/d7291NXKVMDnFAqSt4a2JYsECv+j7b25S0mD";
        parameters.useExtendedTracking = true;
        if (webcamName != null) {
            parameters.cameraName = webcamName;
        } else {
            parameters.cameraDirection = CAMERA_DIRECTION;
        }
        vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initializes the UltsGoal
     * @param webcamName of the webcam to use
     */
    private void initUltsGoal(WebcamName webcamName) {
        // TODO most likely will need to end up establishing precise positions in the future
        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
        float phoneXRotate = 0;
        float phoneYRotate = 0;
        float phoneZRotate = 0;

        //TODO fix these coordinates
        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(Constants.CAMERA_FORWARD_DISPLACEMENT, Constants.CAMERA_LEFT_DISPLACEMENT, Constants.CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

//        targetsUltGoal = vuforiaLocalizer.loadTrackablesFromAsset("UltimateGoal");
//
//        redAllianceTarget = targetsUltGoal.get(2);
//        redAllianceTarget.setName("Red Alliance");
//        listener = ((VuforiaTrackableDefaultListener)redAllianceTarget.getListener());
//        if (webcamName == null) {
//            listener.setPhoneInformation(robotFromCamera, VuforiaLocalizer.CameraDirection.BACK);
//        } else {
//            listener.setCameraLocationOnRobot(webcamName, robotFromCamera);
//        }
//
//        redAllianceTarget.setLocation(OpenGLMatrix
//                .translation(0, 0, mmTargetHeight)
//                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));
//
//        lastLocation = listener.getUpdatedRobotLocation();

    }

    /**
     * @return current vuforia localizer
     */
    public VuforiaLocalizer getVuforiaLocalizer() {
        return vuforiaLocalizer;
    }

    /**
     * Activates vuforia and sets up targetsUltGoal
     */
    public void activate() {
        trackables.activate();
    }

    /**
     * Deactivates vuforia completely
     */
    public void deactivate() {
        trackables.deactivate();
        instance = null;
    }

    /**
     * Index 0: Rotation of the target relative to the robot
     * Index 1: Vertical distance from target relative to the robot
     * @return x offset
     */
    public float getXOffset() {
        if (lastLocation != null) {
            Log.d("CALIBRATION", "X Offset == " + (lastLocation.getTranslation().get(0) - redAllianceTarget.getLocation().getTranslation().get(0)));
            return lastLocation.getTranslation().get(0) - redAllianceTarget.getLocation().getTranslation().get(0);
        }
        return Float.NaN;
    }

    /**
     *
     * @return y offset
     */
    public float getYOffset() {
        if (lastLocation != null) {
            Log.d("CALIBRATION", "Y Offset == " + (lastLocation.getTranslation().get(1) - redAllianceTarget.getLocation().getTranslation().get(1)));
            return lastLocation.getTranslation().get(1) - redAllianceTarget.getLocation().getTranslation().get(1);
        }
        return Float.NaN;
    }

    /**
     *
     * @return z offset
     */
    public float getZOffset() {
        if (lastLocation != null) {
            Log.d("CALIBRATION", "Z Offset == " + (lastLocation.getTranslation().get(2) - redAllianceTarget.getLocation().getTranslation().get(2)));
            return lastLocation.getTranslation().get(2) - redAllianceTarget.getLocation().getTranslation().get(2);
        }
        return Float.NaN;
    }

    /**
     *
     * @return vector of the robot's last location
     */
    public VectorF vector() {
        if (listener.isVisible()) {
            return lastLocation.getTranslation();
        }
        return null;
    }

    /**
     * Updates the robot's current position if there is a new position
     */
    public void updateLocation() {
        OpenGLMatrix proposedPosition = listener.getUpdatedRobotLocation();
        if (proposedPosition != null) {
            lastLocation = proposedPosition;
        }
    }
}