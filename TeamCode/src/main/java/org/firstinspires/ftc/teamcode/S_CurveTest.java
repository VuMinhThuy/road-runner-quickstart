package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="SCurve_Auto", group="Robot")
public class S_CurveTest extends LinearOpMode {

    // Khai báo động cơ
    private DcMotor frontLeft, backLeft, frontRight, backRight;
    private ElapsedTime runtime = new ElapsedTime();

    // Thông số kỹ thuật (Cần hiệu chỉnh cho robot của bạn)
    static final double COUNTS_PER_CM = 15.5;
    static final double MAX_POWER = 0.7;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        // Đi quỹ đạo S-Curve
        moveSCurveMecanum(3.0);
        // Quay 90 độ sang bên trái
        turnMecanum(90, 0.5);
        // tiến 50cm
        moveStraightMecanum(50, 0.5);
        // Quay -45 độ sang phải
        turnMecanum(-45, 0.5);

        stopRobot();
    }

    public void moveSCurveMecanum(double duration) {
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < duration) {
            double t = runtime.seconds() / duration;
            double power = (1.0 - Math.cos(2.0 * Math.PI * t)) / 2.0 * MAX_POWER;
            setAllPower(power, power, power, power);
        }
        stopRobot();
    }

    public void moveStraightMecanum(double cm, double power) {
        int target = (int)(cm * COUNTS_PER_CM);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setTarget(target, target, target, target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setAllPower(power, power, power, power);
        while (opModeIsActive() && frontLeft.isBusy())
        stopRobot();
    }

    public void turnMecanum(int angle, double power) {

        double sleepTime = Math.abs(angle) * 12;

        if (angle > 0) {
            setAllPower(-power, -power, power, power);
        } else {
            setAllPower(power, power, -power, -power);
        }
        sleep((long)sleepTime);
        stopRobot();
    }

    private void setAllPower(double fl, double bl, double fr, double br) {
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);
    }

    private void setTarget(int fl, int bl, int fr, int br) {
        frontLeft.setTargetPosition(fl);
        backLeft.setTargetPosition(bl);
        frontRight.setTargetPosition(fr);
        backRight.setTargetPosition(br);
    }

    private void setMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        backLeft.setMode(mode);
        frontRight.setMode(mode);
        backRight.setMode(mode);
    }

    private void stopRobot() {
        setAllPower(0,0,0,0);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}