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
import org.opencv.imgproc.Imgproc;

public class GalaxyProcessorRedBehindDoor implements VisionProcessor {
    Telemetry telemetry;
    public GalaxyProcessorRedBehindDoor(Telemetry t) {
        telemetry = t;
    }
    public enum DetectedLocation {
        LEFT,
        MIDDLE,
        RIGHT,
        NONE
    }

    Scalar RED_MIN = new Scalar(111.9, 93.5, 97.8);
    Scalar RED_MAX = new Scalar(124.7, 205.4, 204);

    public DetectedLocation detectedLocation = DetectedLocation.NONE;

    Rect leftDrawRect;

    Rect leftRect;

    Rect middleDrawRect;

    Rect middleRect;
    int leftX = 0;
    int leftY = 500;
    int leftWidth = 1280 /2 - 5;
    int leftHeight = 720;

    int middleX = 1280 / 2 + 100;
    int middleY = 500;
    int middleWidth = 1280 /2 - 100;
    int middleHeight = 720;
    public GalaxyProcessorRedBehindDoor() {
        leftDrawRect = new Rect(leftX, leftY, leftWidth, leftHeight);

        leftRect = new Rect(leftX, leftY, leftWidth, 720 - leftY);

        middleDrawRect = new Rect(middleX, middleY, middleWidth, middleHeight);

        middleRect = new Rect(middleX, middleY, middleWidth, 720 - middleY);
    }
    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    int pixelMin = 500;

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);



        Mat left = frame.submat(leftRect);
        Mat middle = frame.submat(middleRect);
        //frame.setTo(new Scalar(0, 0, 0));

        Core.inRange(left, RED_MIN, RED_MAX, left);
        //Imgproc.medianBlur(left, left, 5);
        int leftPixels = Core.countNonZero(left);

        Core.inRange(middle, RED_MIN, RED_MAX, middle);
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

        Core.inRange(frame, RED_MIN, RED_MAX, frame);
       // Imgproc.medianBlur(frame, frame, 5);
        return frame;
    }
    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.RED);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(scaleCanvasDensity * 4);
        canvas.drawRect(MakeRect(leftDrawRect, scaleBmpPxToCanvasPx), rectPaint);
        canvas.drawRect(MakeRect(middleDrawRect, scaleBmpPxToCanvasPx), rectPaint);
    }

    private android.graphics.Rect MakeRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);
        return new android.graphics.Rect(left, top, right, bottom);
    }

}
