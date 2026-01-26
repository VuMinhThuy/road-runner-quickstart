package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name="FTC_Test_4: Quỹ Đạo Góc Vuông", group="Test")
public class SquareCycleAuto extends LinearOpMode {
    @Override
    public void runOpMode() {

        MecanumDrive drive = new MecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(-36, -60, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        Vector2d chamDo = new Vector2d(-12, -12);

        TrajectorySequence squarePath = drive.trajectorySequenceBuilder(startPose)

                .addDisplacementMarker(() -> startIntake())
                .lineTo(new Vector2d(-36, -48))
                .lineToLinearHeading(new Pose2d(chamDo.getX(), chamDo.getY(), Math.toRadians(45)))
                .addDisplacementMarker(() -> shootBalls())
                .waitSeconds(0.5)

                .lineTo(new Vector2d(-36, -12))
                .addDisplacementMarker(() -> startIntake())
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(chamDo.getX(), chamDo.getY(), Math.toRadians(45)))
                .addDisplacementMarker(() -> shootBalls())
                .waitSeconds(0.5)

                .lineTo(new Vector2d(-36, -36))
                .addDisplacementMarker(() -> startIntake())
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(chamDo.getX(), chamDo.getY(), Math.toRadians(45)))
                .addDisplacementMarker(() -> shootBalls())
                .waitSeconds(0.5)

                .lineToLinearHeading(new Pose2d(36, -36, Math.toRadians(0)))
                .addDisplacementMarker(() -> startIntake())
                .waitSeconds(0.8)
                .lineToLinearHeading(new Pose2d(chamDo.getX(), chamDo.getY(), Math.toRadians(135)))
                .addDisplacementMarker(() -> shootBalls())
                .build();

        waitForStart();
        if (isStopRequested()) return;
        drive.followTrajectorySequence(squarePath);
    }

    private void startIntake() { }
    private void shootBalls() { }
}