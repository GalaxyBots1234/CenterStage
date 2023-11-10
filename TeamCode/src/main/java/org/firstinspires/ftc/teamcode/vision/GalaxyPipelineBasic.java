package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class GalaxyPipelineBasic extends OpenCvPipeline {
    Telemetry telemetry;

    public GalaxyPipelineBasic(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    @Override
    public Mat processFrame(Mat input) {
        // HSV is better for comparing ranges than RGB
        Mat mat = input;
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
        // if the input is empty (something is wrong) do not make any assumptions (keep using old information)
        if(mat.empty()) {
            return input;
        }

        telemetry.update();
        return mat;
    }

}