package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

public class GalaxyBot {

    private DcMotor leftBack;
    private DcMotor leftFront;
    private DcMotor rightBack;
    private DcMotor rightFront;
    private DcMotor linearActuator;
    private DcMotor intake;
    private HardwareMap hwMap;

    private boolean intakeOn = false;
    private ElapsedTime runtime = new ElapsedTime();
    public GalaxyBot(HardwareMap hwMap) {
        this.hwMap = hwMap;
        map();
    }
    private void map() {
        leftFront = hwMap.get(DcMotor.class, "left_front");
        rightFront = hwMap.get(DcMotor.class, "right_front");
        leftBack = hwMap.get(DcMotor.class, "left_back");
        rightBack = hwMap.get(DcMotor.class, "right_back");
       // linearActuator = hwMap.get(DcMotor.class, "linear_actuator");
        intake = hwMap.get(DcMotor.class, "intake");
        //linearActuator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void drive(double speed) {
        leftBack.setPower(speed);
        leftFront.setPower(speed);
        rightBack.setPower(speed);
        rightFront.setPower(speed);
    }

    public void drive(double frontRightPower, double frontLeftPower, double backRightPower, double backLeftPower) {
        leftBack.setPower(backLeftPower);
        leftFront.setPower(frontLeftPower);
        rightBack.setPower(backRightPower);
        rightFront.setPower(frontRightPower);
    }
/*
    public void lift(float intensity, float direction) {
        linearActuator.setPower(intensity * direction);
    }
*/
    public void intake() {
        if(intakeOn) {
            intake.setPower(1.0f);
        }
        else {
            intake.setPower(0.0f);
        }
    }

    public void setIntakeOn() {
        intakeOn = !intakeOn;
    }
    public double getElapsedTime() {
        return runtime.time(TimeUnit.MILLISECONDS);
    }

}