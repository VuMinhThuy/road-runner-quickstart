package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.LinkedList;
import java.util.Queue;

public class BallSystem {
    private DcMotor intakeMotor;
    private Servo sorterServo;

    private final double INTAKE_SPEED = 1.0;
    private final int MAX_CAPACITY = 3;

    private final double SERVO_KEEP = 0.0;
    private final double SERVO_REJECT = 0.5;

    private Queue<String> ballStock = new LinkedList<>();
    private boolean lastSensorState = false;

    public void init(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotor.class, "intake_motor");
        sorterServo = hwMap.get(Servo.class, "sorter_servo");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorterServo.setPosition(SERVO_KEEP);
    }

    public void update(boolean cameraSeesBall, String detectedColor, boolean intakeButtonPressed) {
        if (cameraSeesBall && !lastSensorState) {
            if (ballStock.size() < MAX_CAPACITY) {
                ballStock.add(detectedColor);
            }
        }
        lastSensorState = cameraSeesBall;

        if (ballStock.size() >= MAX_CAPACITY) {
            stopIntake();
        } else if (intakeButtonPressed) {
            startIntake();
        } else {
            stopIntake();
        }

        processSorter();
    }

    private void processSorter() {
        String currentBall = ballStock.peek();

        if (currentBall != null) {
            if (currentBall.equals("BLUE")) {
                sorterServo.setPosition(SERVO_REJECT);
            } else if (currentBall.equals("PURPLE")) {
                sorterServo.setPosition(SERVO_KEEP);
            }
        }
    }

    private void startIntake() {
        intakeMotor.setPower(INTAKE_SPEED);
    }

    private void stopIntake() {
        intakeMotor.setPower(0);
    }

    public void removeBall() {
        ballStock.poll();
    }
}