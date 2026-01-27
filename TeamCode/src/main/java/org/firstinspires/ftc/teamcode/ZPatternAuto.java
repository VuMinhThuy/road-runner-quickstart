package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="ZAUTO", group="Competition")
public class Testauto extends LinearOpMode {

    private DcMotorEx intakeMotor;
    private DcMotorEx shooterMotor;

    @Override
    public void runOpMode() {

        Pose2d startPose = new Pose2d(-36, -60, Math.toRadians(90));
        Vector2d ganbuc = new Vector2d(-12, -12);
        Vector2d gatbong = new Vector2d(-58, -12);

        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                drive.actionBuilder(startPose)

                        .stopAndAdd(() -> intakeMotor.setPower(1.0))
                        .lineToY(-48)
                        .splineTo(ganbuc, Math.toRadians(45))
                        .stopAndAdd(() -> shootBalls())
                        .waitSeconds(0.5)

                        .setTangent(Math.toRadians(180))
                        .lineToX(gatbong.x)
                        .waitSeconds(0.2)
                        .lineToX(-40)

                        .setReversed(true)
                        .splineTo(new Vector2d(-36, -12), Math.toRadians(180))
                        .setReversed(false)
                        .splineTo(ganbuc, Math.toRadians(45))
                        .stopAndAdd(() -> shootBalls())
                        .waitSeconds(0.5)

                        .setTangent(Math.toRadians(180))
                        .lineToX(gatbong.x)
                        .waitSeconds(0.2)
                        .lineToX(-40)

                        .splineTo(new Vector2d(36, -36), Math.toRadians(0))
                        .stopAndAdd(() -> intakeMotor.setPower(0))
                        .build()
        );
    }

    void shootBalls() {
        shooterMotor.setPower(1.0);
        try { Thread.sleep(800); } catch (InterruptedException e) {}
        shooterMotor.setPower(0);
    }
}