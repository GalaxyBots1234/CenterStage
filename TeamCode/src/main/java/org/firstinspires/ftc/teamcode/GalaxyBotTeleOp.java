package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp(name="GalaxyBot-TeleOp", group="Opmode")
public class GalaxyBotTeleOp extends OpMode {
    private GalaxyBot robot;
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();

    private Gamepad currentGamepad2 = new Gamepad();
    private Gamepad previousGamepad2 = new Gamepad();

    private IMU imu;

    private boolean fieldCentric = false;

    private void mecanumDrive(double botHeading) {
        float LIMIT_SPEED = 0.95f;
        float y = -gamepad1.left_stick_y;
        float x = gamepad1.left_stick_x * 1.1f;
        float rx = gamepad1.right_stick_x;
            if(gamepad1.dpad_left) {

                x = -1;
            }
            if(gamepad1.dpad_right) {
                x = 1;
            }
            if(gamepad1.dpad_up) {
                y = 1;
            }
            if(gamepad1.dpad_down) {
                y = -1;
            }

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double frontLeftPower = (y + x + rx) / denominator;

        double backLeftPower = (y - x + rx) / denominator;

        double frontRightPower = (y - x - rx) / denominator;

        double backRightPower = (y + x - rx) / denominator;

        robot.drive(frontRightPower * LIMIT_SPEED, frontLeftPower * LIMIT_SPEED,
                backRightPower * LIMIT_SPEED, backLeftPower * LIMIT_SPEED);
        telemetry.addData("leftFrontPower", frontLeftPower);
        telemetry.addData("leftBackPower", backLeftPower);
        telemetry.addData("rightFrontPower", frontRightPower);
        telemetry.addData("rightBackPower", backRightPower);
        telemetry.addData("y", y);
        telemetry.addData("x", x);
        telemetry.addData("rx", rx);
        telemetry.addData("heading", botHeading);
    }

    private void copyGamepad() throws RobotCoreException {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        previousGamepad2.copy(currentGamepad2);
        currentGamepad2.copy(gamepad2);
    }

    private void spinSpinners() {
        if(gamepad2.dpad_right ) {
            robot.setSpinners(false, 0.01f);
            direction = "moving up";
        }
        else if(gamepad2.dpad_left ) {
            robot.setSpinners(true, 0.01f);
            direction = "moving down";
        }
        else {
            robot.setSpinners(true, 0.0f);
            direction = "not moving";
        }
    }
//
//    private void liftRobot() {
//        if (gamepad2.a && previousGamepad2.a) {
//             robot.lift(1.0f, -1);
//        }
//        else if(gamepad2.b && previousGamepad2.b) {
//             robot.lift(1.0f, 1);
//        }
//        else {
//            robot.lift(0.0f, 0);
//        }
//    }

    private void doClaws() {
        if(gamepad2.y && !previousGamepad2.y) {
            robot.setLeftClaw();
        }
        if(gamepad2.b && !previousGamepad2.b) {
            robot.setRightClaw();
        }
    }
    String direction = " ";
    private void liftRobotSlide() {
        if (gamepad2.dpad_up && previousGamepad2.dpad_up) {
            robot.linearSlide(.5f, 1);

        }
        else if(gamepad2.dpad_down && previousGamepad2.dpad_down) {
            robot.linearSlide(.5f, -1);
        }
        else {
            robot.stopSlide();
        }
    }

    private void intakePixel() {
        if (gamepad2.right_bumper && !previousGamepad2.right_bumper) {
            robot.intake();
        }
    }
    boolean launched = false;
    private void shootPlane() {
        if(gamepad2.left_bumper && !launched) {
            launched = true;
            robot.launchAirplane();
        }
    }
    @Override
    public void init() {
        robot = new GalaxyBot(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters myIMUparameters;
        myIMUparameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                )
        );
        imu.initialize(myIMUparameters);

    }

    @Override
    public void loop() {


        try {
            copyGamepad();
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        mecanumDrive(botHeading);
       // liftRobot();

        intakePixel();
        robot.doIntake();
        liftRobotSlide();
        doClaws();
        spinSpinners();
        shootPlane();
        telemetry.addData("claws: ", robot.leftClawOpen + " " + robot.rightClawOpen);
        telemetry.addData("spinner direction: ", direction);
        telemetry.addData("launched: ", launched);
        telemetry.update();
    }
}