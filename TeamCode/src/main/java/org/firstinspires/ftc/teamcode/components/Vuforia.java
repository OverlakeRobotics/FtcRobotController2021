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
import org.firstinspires.ftc.teamcode.helpers.BuildConfig;
import org.firstinspires.ftc.teamcode.helpers.Constants;
import org.firstinspires.ftc.teamcode.helpers.Coordinates;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.teamcode.helpers.Constants.*;

/** Teddy, trust my code, you blasphemer. */
public class Vuforia {

    private VuforiaLocalizer vuforiaLocalizer;
    private OpenGLMatrix lastLocation; // class members
    public static VuforiaTrackables targets;
    public static VuforiaTrackable redAllianceTarget;
    private VuforiaTrackableDefaultListener listener;
    private static Vuforia instance;
    private static final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = VuforiaLocalizer.CameraDirection.BACK;

    /**
     * @param webcamName to use for vuforia
     * @return instance of vuforia singleton
     */
    public static Vuforia getInstance(WebcamName webcamName, int identifier) {
        if (instance == null) instance = new Vuforia(webcamName, identifier);
        return instance;
    }

    /**
     * @param webcamName to use for vuforia
     * @return instance of vuforia singleton
     */
    public static Vuforia getInstance(WebcamName webcamName) {
        if (instance == null) instance = new Vuforia(webcamName, 1);
        return instance;
    }

    /**
     * @return instance of vuforia singleton
     */
    public static Vuforia getInstance() {
        if (instance == null) instance = new Vuforia(null, 1);
        return instance;
    }

    /**
     * Constructor
     * @param webcamName to use for init
     */
    public Vuforia(WebcamName webcamName, int identifier) {

        VuforiaLocalizer.Parameters parameters;
        if (identifier != 1) {
            parameters = new VuforiaLocalizer.Parameters();
        } else {
            parameters = new VuforiaLocalizer.Parameters(identifier);
        }
        parameters.vuforiaLicenseKey = BuildConfig.NOCTURNAL_VUFORIA_KEY;
        parameters.useExtendedTracking = true;
        if (webcamName != null) {
            parameters.cameraName = webcamName;
        } else {
            parameters.cameraDirection = CAMERA_DIRECTION;
        }
        vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters);


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

        targets = vuforiaLocalizer.loadTrackablesFromAsset("FreightFrenzy");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);

        // Name and locate each trackable object
        identifyTarget(0, "Blue Storage",       -halfField,  oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(1, "Blue Alliance Wall",  halfTile,   halfField,      mmTargetHeight, 90, 0, 0);
        identifyTarget(2, "Red Storage",        -halfField, -oneAndHalfTile, mmTargetHeight, 90, 0, 90);
        identifyTarget(3, "Red Alliance Wall",   halfTile,  -halfField,      mmTargetHeight, 90, 0, 180);

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        /**  Let all the trackable listeners know where the camera is.  */
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }
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
        targets.activate();
    }

    /**
     * Deactivates vuforia completely
     */
    public void deactivate() {
        targets.deactivate();
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
/*
    public void getLastLocation() {
        Coordinates.updateX(lastLocation.getTranslation().get(0)/Constants.mmPerInch);
        Coordinates.updateY(lastLocation.getTranslation().get(1)/Constants.mmPerInch);
    } */

    public VuforiaTrackables getTargets() {
        if (targets != null) {
            return targets;
        } else {
            return null;
        }
    }

    /***
     * Identify a target by naming it, and setting its position and orientation on the field
     * @param targetIndex
     * @param targetName
     * @param dx, dy, dz  Target offsets in x,y,z axes
     * @param rx, ry, rz  Target rotations in x,y,z axes
     */
    void    identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }
}