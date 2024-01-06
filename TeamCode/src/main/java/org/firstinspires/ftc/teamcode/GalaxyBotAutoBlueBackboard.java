package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.vision.GalaxyProcessorBlueBackboard;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Arrays;
import java.util.List;

@Autonomous(name="GalaxyBotAutoBlueBackBoard", group="Opmode")
public class GalaxyBotAutoBlueBackboard extends LinearOpMode {
    private GalaxyBot robot;
    private SampleMecanumDrive drive;

    private VisionPortal visionPortal;

    private GalaxyProcessorBlueBackboard galaxyProcessorBlueBackboard;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new GalaxyBot(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(webcamName);
        galaxyProcessorBlueBackboard = new GalaxyProcessorBlueBackboard();
        builder.addProcessor(galaxyProcessorBlueBackboard);
        builder.setCameraResolution(new Size(1280, 720));
        visionPortal = builder.build();

        Pose2d startPose = new Pose2d(12, 60, Math.toRadians(-90));
        drive.setPoseEstimate(startPose);
        TrajectoryVelocityConstraint rightConstraint =
                new MinVelocityConstraint(Arrays.asList(
                        new TranslationalVelocityConstraint(10),
                        new AngularVelocityConstraint(1.5)
                ));

        TrajectoryVelocityConstraint leftConstraint =
                new MinVelocityConstraint(Arrays.asList(
                        new TranslationalVelocityConstraint(10),
                        new AngularVelocityConstraint(1.5)
                ));
        TrajectorySequence middle = drive.trajectorySequenceBuilder(startPose).
                resetConstraints().
                forward(27).
                build();

        TrajectorySequence middleReturn = drive.trajectorySequenceBuilder(startPose).
                resetConstraints()
                .splineTo(new Vector2d(12, 42), Math.toRadians(-90)).
                turn(Math.toRadians(90)).
                build();

        TrajectorySequence right = drive.trajectorySequenceBuilder(startPose).
                resetConstraints().
                setVelConstraint(rightConstraint).
                //splineTo(new Vector2d(10, 34), Math.toRadians(-45))

                splineTo(new Vector2d(6, 35), Math.toRadians(-135))
                .build();

        TrajectorySequence left = drive.trajectorySequenceBuilder(startPose).
                resetConstraints().
                setVelConstraint(leftConstraint).
                splineTo(new Vector2d(17.5, 36), Math.toRadians(-45))

                .build();

        Pose2d middleReturnPose = new Pose2d(new Vector2d(12, 42), Math.toRadians(-90));
        TrajectorySequence leftTag = drive.trajectorySequenceBuilder(middleReturnPose)
                .resetConstraints()
                .splineTo(new Vector2d(48, 32), Math.toRadians(0))
                .build();


        TrajectorySequence middleTag = drive.trajectorySequenceBuilder(middleReturnPose)
                .resetConstraints()
                .splineTo(new Vector2d(48, 26), Math.toRadians(0))
                .build();


        TrajectorySequence rightTag = drive.trajectorySequenceBuilder(middleReturnPose)
                .resetConstraints()
                .splineTo(new Vector2d(48, 18), Math.toRadians(0))
                .build();

        GalaxyProcessorBlueBackboard.DetectedLocation detectedLocation = GalaxyProcessorBlueBackboard.DetectedLocation.NONE;
        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();
        while(visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {};

        sleep(2500);
         detectedLocation = galaxyProcessorBlueBackboard.detectedLocation;

        telemetry.addData("Location", detectedLocation);
        telemetry.update();

        switch (detectedLocation) {
            case MIDDLE:
                drive.followTrajectorySequence(middle);
                //drive.followTrajectorySequence(middleReturn);
                //drive.followTrajectorySequence(middleTag);
                //raiseSlides();
                break;

            case LEFT:
                drive.followTrajectorySequence(left);
                //drive.followTrajectorySequence(middleReturn);
                //drive.followTrajectorySequence(leftTag);
                //raiseSlides();
                break;

            case RIGHT:
                drive.followTrajectorySequence(right);
                //drive.followTrajectorySequence(middleReturn);
                //drive.followTrajectorySequence(rightTag);
                //raiseSlides();
                break;
        }
        if (isStopRequested()) {
                return;
        }

    }

    void raiseSlides() {
        robot.linearSlide(.5f, 1);
        sleep(1500);
        robot.stopSlide();
        robot.setSpinners(1.0f);

        robot.setSpinners(1.0f);
        robot.setSpinners(1.0f);
        robot.setSpinners(1.0f);
        robot.setSpinners(1.0f);
        robot.setSpinners(1.0f);
        robot.setSpinners(1.0f);
    }
}