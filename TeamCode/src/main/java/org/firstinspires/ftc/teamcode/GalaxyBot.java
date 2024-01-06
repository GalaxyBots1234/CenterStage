package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

public class GalaxyBot {

    private DcMotor leftBack;


    private DcMotor leftFront;
    private DcMotor rightBack;
    private DcMotor rightFront;
   // private DcMotor linearActuator;

    private DcMotor linearSlideLeft;
    private DcMotor linearSlideRight;
    private DcMotor intake;
    public Servo box;
    boolean boxed = false;
    public Servo leftSpinner;

    public Servo rightSpinner;

    //private DcMotor hanger;

    //private Servo airplaneLauncher;

    private HardwareMap hwMap;
    boolean intaking = false;
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

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //linearActuator = hwMap.get(DcMotor.class, "linearActuator");
        linearSlideLeft = hwMap.get(DcMotor.class, "leftLinearSlide");
        linearSlideRight = hwMap.get(DcMotor.class, "rightLinearSlide");
        //airplaneLauncher = hwMap.get(Servo.class, "airplane_launcher");
        intake = hwMap.get(DcMotor.class, "intake");
        box = hwMap.get(Servo.class, "box");
        leftSpinner = hwMap.get(Servo.class, "left_spinner");
        rightSpinner = hwMap.get(Servo.class, "right_spinner");

        //hanger = hwMap.get(DcMotor.class, "hanger");
        //hanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightSpinner.setDirection(Servo.Direction.REVERSE);

        //linearActuator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        linearSlideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        linearSlideRight.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void drive(double speed) {
        leftBack.setPower(speed);
        leftFront.setPower(speed);
        rightBack.setPower(speed);
        rightFront.setPower(speed);
    }
//
//    public void launchAirplane() {
//        airplaneLauncher.setPosition(1.0);
//    }
    public void setSpinners(boolean down, float amount) {
        if (!down) {
            leftSpinner.setPosition(leftSpinner.getPosition() + amount);
            rightSpinner.setPosition(rightSpinner.getPosition() + amount);
        }
        else if (down ){
            leftSpinner.setPosition(leftSpinner.getPosition() - amount);
            rightSpinner.setPosition(rightSpinner.getPosition() - amount);
        }
    }

    public void setSpinners(float amount) {
        leftSpinner.setPosition(amount);
        rightSpinner.setPosition(amount);
    }

    //public void moveHanger(float intensity, int power) {
        //hanger.setPower(power * intensity);
   // }

    public void openBox() {
        box.setPosition(1.0);
    }

    public void closeBox() {
        box.setPosition(0.0);
    }

    public void drive(double frontRightPower, double frontLeftPower, double backRightPower, double backLeftPower) {
        leftBack.setPower(backLeftPower);
        leftFront.setPower(frontLeftPower);
        rightBack.setPower(backRightPower);
        rightFront.setPower(frontRightPower);
    }
//
//    public void lift(float intensity, float direction) {
//        linearActuator.setPower(intensity * direction);
//    }

    public void linearSlide(float intensity, float direction) {
        linearSlideRight.setPower(intensity * direction);
        linearSlideLeft.setPower(intensity * direction);
    }
    public void stopSlide() {
        linearSlideRight.setPower(0.0);
        linearSlideLeft.setPower(0.0);
    }
    public void intake() {
        intaking = !intaking;
    }

  public void doIntake() {
        if (intaking) {
            intake.setPower(-.8);
        }
        else {
            intake.setPower(0.0);
        }
    }

    public double getElapsedTime() {
        return runtime.time(TimeUnit.MILLISECONDS);
    }

}