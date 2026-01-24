package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.LinkedList;
import java.util.Queue;

public class BallSystem {
    private DcMotor intakeMotor;
    private final double INTAKE_SPEED = 1.0;
    private final int MAX_CAPACITY = 3;
    private Queue<String> ballStock = new LinkedList<>();
    private boolean lastSensorState = false;

    public void init(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotor.class, "intake_motor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        }
        else if (intakeButtonPressed) {
            startIntake();
        }
        else {
            stopIntake();
        }
    }

    private void startIntake() {
        intakeMotor.setPower(INTAKE_SPEED);
    }

    private void stopIntake() {
        intakeMotor.setPower(0);
    }

    public String getNextBall() {
        return ballStock.peek();
    }

    public void removeBall() {
        ballStock.poll();
    }

    public int getCount() {
        return ballStock.size();
    }
}