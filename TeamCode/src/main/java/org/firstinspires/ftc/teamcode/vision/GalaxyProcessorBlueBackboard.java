package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class GalaxyProcessorBlueBackboard implements VisionProcessor {
    Telemetry telemetry;
    public GalaxyProcessorBlueBackboard(Telemetry t) {
        telemetry = t;
    }
    public enum DetectedLocation {
        LEFT,
        MIDDLE,
        RIGHT,
        NONE
    }

    Scalar BLUE_MIN = new Scalar(0, 134.6, 93.5);
    Scalar BLUE_MAX = new Scalar(11.3, 228.1, 216.8);

    public DetectedLocation detectedLocation = DetectedLocation.NONE;

    org.opencv.core.Rect leftDrawRect;

    org.opencv.core.Rect leftRect;

    org.opencv.core.Rect middleDrawRect;

    org.opencv.core.Rect middleRect;
    int leftX = 0;
    int leftY = 350;
    int leftWidth = 1280 /2 - 100;
    int leftHeight = 720;

    int middleX = 1280 / 2 + 100;
    int middleY = 350;
    int middleWidth = 1280 /2 - 100;
    int middleHeight = 720;
    public GalaxyProcessorBlueBackboard() {
        leftDrawRect = new org.opencv.core.Rect(leftX, leftY, leftWidth, leftHeight);

        leftRect = new org.opencv.core.Rect(leftX, leftY, leftWidth, 720 - leftY);

        middleDrawRect = new org.opencv.core.Rect(middleX, middleY, middleWidth, middleHeight);

        middleRect = new org.opencv.core.Rect(middleX, middleY, middleWidth, 720 - middleY);
    }
    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    int pixelMin = 500;

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2YCrCb);



        Mat left = frame.submat(leftRect);
        Mat middle = frame.submat(middleRect);
        //frame.setTo(new Scalar(0, 0, 0));

        Core.inRange(left, BLUE_MIN, BLUE_MAX, left);
        //Imgproc.medianBlur(left, left, 5);
        int leftPixels = Core.countNonZero(left);

        Core.inRange(middle, BLUE_MIN, BLUE_MAX, middle);
        //Imgproc.medianBlur(middle, middle, 5);
        int middlePixels = Core.countNonZero(middle);

        if(leftPixels > pixelMin) {
            detectedLocation = DetectedLocation.LEFT;
        }
        else if(middlePixels > pixelMin) {
            detectedLocation = DetectedLocation.MIDDLE;
        }
        else {
            detectedLocation = DetectedLocation.RIGHT;
        }

        Core.inRange(frame, BLUE_MIN, BLUE_MAX, frame);
       // Imgproc.medianBlur(frame, frame, 5);
        return frame;
    }
    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.BLUE);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(scaleCanvasDensity * 4);
        canvas.drawRect(MakeRect(leftDrawRect, scaleBmpPxToCanvasPx), rectPaint);
        canvas.drawRect(MakeRect(middleDrawRect, scaleBmpPxToCanvasPx), rectPaint);
    }

    private android.graphics.Rect MakeRect(org.opencv.core.Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);
        return new android.graphics.Rect(left, top, right, bottom);
    }

}
