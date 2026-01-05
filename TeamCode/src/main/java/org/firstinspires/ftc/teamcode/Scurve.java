package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "S-Curve", group = "Autonomous")
public class Scurve extends LinearOpMode {

    public class IntakeAction implements Action {
        private final DcMotorEx motor;
        private final double power;

        public IntakeAction(DcMotorEx motor, double power) {
            this.motor = motor;
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            motor.setPower(power);
            return false;
        }
    }

    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(-63, -36, 0);
        TankDrive drive = new TankDrive(hardwareMap, startPose);
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");

        Action sCurveToBallZone = drive.actionBuilder(startPose)
                .lineToX(-45)
                .splineTo(new Vector2d(-15, -10), Math.toRadians(45))
                .splineTo(new Vector2d(15, -36), Math.toRadians(0))

                .lineToX(48)

                .stopAndAdd(new IntakeAction(intakeMotor, 1.0))
                .waitSeconds(2.5)
                .stopAndAdd(new IntakeAction(intakeMotor, 0))
                .build();

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(sCurveToBallZone);
    }
}