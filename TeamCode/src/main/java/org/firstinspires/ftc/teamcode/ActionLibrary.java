package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "Action-Integrated")
public class ActionLibrary extends LinearOpMode {

    public class RollerIntake {
        private DcMotorEx intakeMotor;

        public RollerIntake(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        }

        public Action startIntake() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    intakeMotor.setPower(1.0);
                    return false;
                }
            };
        }

        public Action stop() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    intakeMotor.setPower(0);
                    return false;
                }
            };
        }
    }

    @Override
    public void runOpMode() {
        TankDrive drive = new TankDrive(hardwareMap, new Pose2d(0, 0, 0));
        RollerIntake intake = new RollerIntake(hardwareMap);
        Vector2d goalPosition = new Vector2d(72, 0);

        telemetry.addLine("Robot Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            Pose2d currentPose = drive.localizer.getPose();

            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(-gamepad1.left_stick_y, 0),
                    -gamepad1.right_stick_x
            ));

            if (gamepad1.a) {
                Actions.runBlocking(
                        drive.actionBuilder(currentPose)
                                .stopAndAdd(intake.startIntake())
                                .lineToX(24)
                                .waitSeconds(1)
                                .stopAndAdd(intake.stop())
                                .lineToX(0)
                                .build()
                );
            }

            // Tự động xoay về Goal
            if (gamepad1.y) {
                double deltaX = goalPosition.x - currentPose.position.x;
                double deltaY = goalPosition.y - currentPose.position.y;
                double angleToGoal = Math.atan2(deltaY, deltaX);

                Actions.runBlocking(
                        drive.actionBuilder(currentPose)
                                .turnTo(angleToGoal)
                                .build()
                );
            }

            // Điều khiển Intake thủ công
            if (gamepad1.right_trigger > 0.1) {
                intake.intakeMotor.setPower(1.0);
            } else if (gamepad1.left_trigger > 0.1) {
                intake.intakeMotor.setPower(-0.8);
            } else if (!gamepad1.a) {
                intake.intakeMotor.setPower(0);
            }

            // Cập nhật hệ thống định vị
            drive.updatePoseEstimate();

            telemetry.addData("X", currentPose.position.x);
            telemetry.addData("Y", currentPose.position.y);
            telemetry.addData("Heading (Deg)", Math.toDegrees(currentPose.heading.toDouble()));
            telemetry.update();
        }
    }
}