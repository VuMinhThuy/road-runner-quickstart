package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

@Autonomous(name = "Vision_Alignment", group = "Autonomous")
public class Vision extends LinearOpMode {

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(com.qualcomm.robotcore.hardware.HardwareDevice.class, "Webcam 1"), aprilTag);

        Pose2d startPose = new Pose2d(-63, -36, 0);
        TankDrive drive = new TankDrive(hardwareMap, startPose);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(startPose)
                        .splineTo(new Vector2d(-15, -10), Math.toRadians(45))
                        .splineTo(new Vector2d(15, -36), Math.toRadians(0))
                        .build()
        );

        double visionHeadingBuffer = getHeadingErrorFromVision();

        if (Math.abs(visionHeadingBuffer) > 1.0) {
            Actions.runBlocking(
                    drive.actionBuilder(drive.localizer.getPose())
                            .turn(Math.toRadians(visionHeadingBuffer))
                            .build()
            );
        }

        telemetry.addData("Vision Alignment", "Complete");
        telemetry.update();
    }

    private double getHeadingErrorFromVision() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                return detection.ftcPose.bearing;
            }
        }
        return 0;
    }
}