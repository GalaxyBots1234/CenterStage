package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.vision.GalaxyProcessorBlueBackboard;
import org.firstinspires.ftc.teamcode.vision.GalaxyProcessorRedBehindDoor;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.Arrays;

@Autonomous(name="GalaxyBotAutoRedBehindDoor", group="Opmode")
public class GalaxyBotAutoRedBehindDoor extends LinearOpMode {
    private GalaxyBot robot;
    private SampleMecanumDrive drive;

    private VisionPortal visionPortal;

    private GalaxyProcessorRedBehindDoor galaxyProcessorRedBehindDoor;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new GalaxyBot(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(webcamName);
        galaxyProcessorRedBehindDoor = new GalaxyProcessorRedBehindDoor();
        builder.addProcessor(galaxyProcessorRedBehindDoor);
        builder.setCameraResolution(new Size(1280, 720));
        visionPortal = builder.build();

        Pose2d startPose = new Pose2d(-36, -60, Math.toRadians(90));
        drive.setPoseEstimate(startPose);
        TrajectoryVelocityConstraint leftConstraint =
                new MinVelocityConstraint(Arrays.asList(
                        new TranslationalVelocityConstraint(10),
                        new AngularVelocityConstraint(1.6)
                ));

        TrajectoryVelocityConstraint rightConstraint =
                new MinVelocityConstraint(Arrays.asList(
                        new TranslationalVelocityConstraint(10),
                        new AngularVelocityConstraint(1.6)
                ));
        TrajectorySequence middle = drive.trajectorySequenceBuilder(startPose).
                resetConstraints().
                forward(26).
                build();

        TrajectorySequence right = drive.trajectorySequenceBuilder(startPose).
                resetConstraints().
                setVelConstraint(rightConstraint).
                //splineTo(new Vector2d(10, 34), Math.toRadians(-45))

                        splineTo(new Vector2d(-30, -36), Math.toRadians(45))
                .build();


        TrajectorySequence left = drive.trajectorySequenceBuilder(startPose).
                resetConstraints().
                setVelConstraint(leftConstraint).
                splineTo(new Vector2d(-42, -36), Math.toRadians(135))

                .build();

        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();
        sleep(2500);
        GalaxyProcessorRedBehindDoor.DetectedLocation detectedLocation = galaxyProcessorRedBehindDoor.detectedLocation;

        telemetry.addData("Location", detectedLocation);
        telemetry.update();

        switch (detectedLocation) {
            case MIDDLE:
                drive.followTrajectorySequence(middle);
                requestOpModeStop();
                break;

            case LEFT:
                drive.followTrajectorySequence(left);
                requestOpModeStop();
                break;

            case RIGHT:
                drive.followTrajectorySequence(right);
                requestOpModeStop();
                break;
        }
        requestOpModeStop();
        if(isStopRequested())
            return;

    }
}