package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name="Full_Cycle_With_Avoidance", group="Competition")
public class Testauto extends LinearOpMode {

    Vector2d chamDo = new Vector2d(-12, -12);
    Pose2d startPose = new Pose2d(-36, -60, Math.toRadians(90));

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        DistanceSensor frontSensor = hardwareMap.get(DistanceSensor.class, "sensor_front");

        drive.setPoseEstimate(startPose);

        TrajectorySequence fullPath = drive.trajectorySequenceBuilder(startPose)

                .clone(() -> startIntake())
                .forward(12)
                .splineTo(chamDo, Math.toRadians(45))
                .clone(() -> shootBalls())
                .waitSeconds(0.5)

                .setReversed(true)
                .splineTo(new Vector2d(-36, -12), Math.toRadians(180))
                .setReversed(false)
                .clone(() -> startIntake())
                .waitSeconds(0.5)
                .splineTo(chamDo, Math.toRadians(45))
                .clone(() -> shootBalls())
                .waitSeconds(0.5)

                .setReversed(true)
                .splineTo(new Vector2d(-36, -36), Math.toRadians(180))
                .setReversed(false)
                .clone(() -> startIntake())
                .waitSeconds(0.5)
                .splineTo(chamDo, Math.toRadians(45))
                .clone(() -> shootBalls())
                .waitSeconds(0.5)

                .splineTo(new Vector2d(36, -36), Math.toRadians(0))
                .clone(() -> startIntake())
                .waitSeconds(0.8)
                .splineTo(chamDo, Math.toRadians(135))
                .clone(() -> shootBalls())
                .build();

        waitForStart();
        if (isStopRequested()) return;

        drive.followTrajectorySequenceAsync(fullPath);

        while (opModeIsActive() && drive.isBusy()) {
            double distance = frontSensor.getDistance(DistanceUnit.CM);

            if (distance < 20.0) {
                drive.setDrivePower(new Pose2d(0, 0, 0));
                telemetry.addData("WARNING");
            } else {
                drive.update();
            }

            telemetry.addData("Distance", distance);
            telemetry.update();
        }
    }

    private void startIntake() {}
    private void shootBalls() {  }
}