package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Arrays;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        TrajectoryVelocityConstraint rightConstraint =
                new MinVelocityConstraint(Arrays.asList(
                        new TranslationalVelocityConstraint(10),
                        new AngularVelocityConstraint(2)
                ));

        TrajectoryVelocityConstraint leftConstraint =
                new MinVelocityConstraint(Arrays.asList(
                        new TranslationalVelocityConstraint(10),
                        new AngularVelocityConstraint(1.9)
                ));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                .setConstraints(49.394643027, 49.394643027, Math.toRadians(200), Math.toRadians(200), 14.25).followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 42, Math.toRadians(0)))
                                .splineToConstantHeading(new Vector2d(48, 36), Math.toRadians(0))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}