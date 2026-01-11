package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Config
@Autonomous(name = "Heading PID Tuning", group = "Tuning")
public class FeedforwardTuning extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TankDrive drive = new TankDrive(hardwareMap, new Pose2d(0, 0, 0));

        telemetry.addLine("Cân chỉnh PID Heading");
        telemetry.addLine("Robot xoay các góc: 45, 90, 180 độ.");
        telemetry.addLine("Nhấn START để bắt đầu.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0, 0, 0))
                        .turn(Math.toRadians(45))
                        .waitSeconds(2)
                        .turn(Math.toRadians(45))
                        .waitSeconds(2)
                        .turn(Math.toRadians(-180))
                        .waitSeconds(2)
                        .build()
        );

        telemetry.addLine("Hoàn thành thử nghiệm.");
        telemetry.update();
    }
}