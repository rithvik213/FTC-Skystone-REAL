package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous
public class turnTest2 extends LinearOpMode {
    HardwareBot robot;
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;

    BNO055IMU imu;
    Orientation angles;

    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.get(DcMotor.class, "lf");
        rightFront = hardwareMap.get(DcMotor.class, "rf");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.get(DcMotor.class, "lb");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        getIMUReady();
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // changed this
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // changed this
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // changed this
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // changed this

        waitForStart();

        turnIMU2(90,0.4,false);






    }
    public void getIMUReady() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }

    public void turnIMU2(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES); //was ZYX before

        double startTime = getRuntime();
        double deltaAngle = 0, initTime = 0, deltaTime = 0;
        double i = 0;
        double error = 100; //used to start while loop
        double KP = 0.006;
        double KD = 0.1;
        double previous_error = 0;

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;

        if (direction) {
            target = initialPos - angle;
        }
        else {
            target = angle + initialPos;
        }

        while(opModeIsActive() && Math.abs(error) > 1) {
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = angles.firstAngle;
            error = currentPos - target;

            double power = Range.clip(((KP * error + KD * (error - previous_error)) / deltaTime), -0.7, .7);

            telemetry.addData("Current Position: ", currentPos);
            telemetry.addData("Error: ", error);
            telemetry.addData("Power: ", power);
            telemetry.addData("Raw Power: ",(KP * error + KD * (error - previous_error)) / deltaTime);
            telemetry.update();

            if(direction) {
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
            } else {
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
            }
            deltaTime = getRuntime() - initTime;
            initTime = getRuntime();

            previous_error = error;

        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}
