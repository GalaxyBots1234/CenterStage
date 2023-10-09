package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="GalaxyBot-TeleOp", group="Opmode")
public class GalaxyBotTeleOp extends OpMode {
    private GalaxyBot robot;
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();

    private Gamepad currentGamepad2 = new Gamepad();
    private Gamepad previousGamepad2 = new Gamepad();

    private BNO055IMU imu;

    private boolean fieldCentric = false;

    private void mecanumDrive(double rotation) {
        final double LIMIT_POWER = 0.5;

        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double rotX = x * Math.cos(rotation) - y * Math.sin(rotation);
        double rotY = x * Math.sin(rotation) + y * Math.cos(rotation);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double frontLeftPower;
        double backLeftPower;
        double frontRightPower;
        double backRightPower;

        if (gamepad1.left_bumper) {
            fieldCentric = !fieldCentric;
        }
        if (fieldCentric) {
            frontLeftPower = (rotY + rotX + rx) / denominator * LIMIT_POWER;
            backLeftPower = (rotY - rotX + rx) / denominator * LIMIT_POWER;
            frontRightPower = (rotY - rotX - rx) / denominator * LIMIT_POWER;
            backRightPower = (rotY + rotX - rx) / denominator * LIMIT_POWER;
        }
        else {
            frontLeftPower = (y + x + rx) / denominator * LIMIT_POWER;
            backLeftPower = (y - x + rx) / denominator * LIMIT_POWER;
            frontRightPower = (y - x - rx) / denominator * LIMIT_POWER;
            backRightPower = (y + x - rx) / denominator * LIMIT_POWER;
        }
        robot.drive(frontRightPower, frontLeftPower, backRightPower, backLeftPower);
        telemetry.addData("leftFrontPower", frontLeftPower);
        telemetry.addData("leftBackPower", backLeftPower);
        telemetry.addData("rightFrontPower", frontRightPower);
        telemetry.addData("rightBackPower", backRightPower);
        telemetry.addData("y", y);
        telemetry.addData("x", x);
        telemetry.addData("rx", x);
    }

    private void copyGamepad() throws RobotCoreException {
        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        previousGamepad2.copy(currentGamepad2);
        currentGamepad2.copy(gamepad2);
    }

    @Override
    public void init() {
        robot = new GalaxyBot(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
    }

    @Override
    public void loop() {
        double botHeading = -imu.getAngularOrientation().firstAngle;

        try {
            copyGamepad();
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }

        mecanumDrive(botHeading);
        telemetry.update();
    }
}