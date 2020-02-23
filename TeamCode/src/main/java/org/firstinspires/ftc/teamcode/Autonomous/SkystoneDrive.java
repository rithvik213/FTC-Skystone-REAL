package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class SkystoneDrive {
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;

    Servo sLeft = null;
    Servo sRight = null;

    Servo clutch = null;
    Servo autoClutch = null;
    Servo foundLeft = null;
    Servo foundRight = null;

    private final LinearOpMode opMode;

    BNO055IMU imu;
    Orientation angles;


    double distance = 0;
    double xlocation = 0;
    double ylocation = 0;

    double WHEEL_DIAMETER_INCHES = 4.0;
    double COUNTS_PER_MOTOR_REV = 1120;
    double GEAR_REDUCTION = 1.5; //should be 1.5
    double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    //Distance Sensor Initialization Variables
    ModernRoboticsI2cRangeSensor rangeSensor;
    ModernRoboticsI2cRangeSensor rangeSensor2;
    double currentRangeL = 0;
    double currentRangeR = 0;
    double closeEnough = 0;

    boolean getOut = false;

    public SkystoneDrive(LinearOpMode mode) {
        this.opMode = mode;
        leftFront = opMode.hardwareMap.get(DcMotor.class, "lf");
        rightFront = opMode.hardwareMap.get(DcMotor.class, "rf");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack = opMode.hardwareMap.get(DcMotor.class, "lb");
        rightBack = opMode.hardwareMap.get(DcMotor.class, "rb");
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        getIMUReady();
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // changed this
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);  // changed this
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // changed this
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // changed this

        rangeSensor = opMode.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeL");
        rangeSensor2 = opMode.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeR");

        clutch = opMode.hardwareMap.get(Servo.class,"clutch");
        autoClutch = opMode.hardwareMap.servo.get("autoClutch");

        foundLeft = opMode.hardwareMap.servo.get("foundation");
        foundRight = opMode.hardwareMap.servo.get("foundation2");



    }
    /*if the direction is true then it moves forward
     * esle it moves backward */
    public void moveDistance(double distance, double power, boolean direction) {
        //resetEncoders();
        //runUsingEncoders();

        double target;
        double counts;

        counts = ((distance / (GEAR_REDUCTION * WHEEL_DIAMETER_INCHES * 3.1415))) * COUNTS_PER_MOTOR_REV;
        target = Math.abs(rightFront.getCurrentPosition()) + counts;
        //target = rightFront.getCurrentPosition() + Math.round(Math.abs(distance) * COUNTS_PER_INCH);

        if (direction) {
            while (Math.abs(Math.abs(rightFront.getCurrentPosition()) - Math.abs(target)) > 45 && !opMode.isStopRequested()) {
                goStraight(-power);
                opMode.telemetry.addData("Current Position", Math.abs(rightFront.getCurrentPosition()));
                opMode.telemetry.addData("Distance to go", Math.abs(Math.abs(rightFront.getCurrentPosition()) - Math.abs(target)));
                opMode.telemetry.addData("Left front encoder: ", leftFront.getCurrentPosition());
                opMode.telemetry.update();
            }
            goStraight(0);
        } else {
            target = Math.abs(rightFront.getCurrentPosition()) - counts;
            //target = -1 * (-rightFront.getCurrentPosition() - Math.round(Math.abs(distance) * COUNTS_PER_INCH));

            while ((Math.abs(rightFront.getCurrentPosition()) - target) > 45 && !opMode.isStopRequested()) {
                goStraight(power);
                opMode.telemetry.addData("Target: ", Math.abs(rightFront.getCurrentPosition()) - counts);
                opMode.telemetry.addData("Current Position", Math.abs(rightFront.getCurrentPosition()));
                opMode.telemetry.addData("Distance to go", (Math.abs(rightFront.getCurrentPosition()) - target));
                opMode.telemetry.update();
            }
            goStraight(0);
        }
            /*else {
                target = rightFront.getCurrentPosition() - Math.round(Math.abs(distance) * COUNTS_PER_INCH);

                while ((-rightFront.getCurrentPosition() + target) > 45 && !opMode.isStopRequested()) {
                    leftFront.setPower(power);
                    leftBack.setPower(power);
                    rightFront.setPower(power);
                    rightBack.setPower(power);

                    opMode.telemetry.addData("Target: ",-1 * (-rightFront.getCurrentPosition() - Math.round(Math.abs(distance) * COUNTS_PER_INCH)) );
                    opMode.telemetry.addData("Current Position", (rightFront.getCurrentPosition()));
                    opMode.telemetry.addData("Distance to go",(target - rightFront.getCurrentPosition()));
                    opMode.telemetry.update();
                }
            }*/
        goStraight(0);
    }

    public void goStraight(double power) {
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);
    }
    public void getIMUReady() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }
    public void turnIMUTest(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime = 0, deltaTime = 0;
        double i = 0;
        double error = 100; //used to start while loop
        double KP = 0.008;
        //double KD = 0;
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

        while(opMode.opModeIsActive() && Math.abs(error) > 1) {
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = angles.firstAngle;
            error = currentPos - target;

            double power = Range.clip((error/20) + i,-.7,.7);

            opMode.telemetry.addData("Current Position: ", currentPos);
            opMode.telemetry.addData("Distance to go: ", error);
            opMode.telemetry.addData("Power: ", power);
            opMode.telemetry.update();

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
            deltaTime = opMode.getRuntime() - initTime;
            initTime = opMode.getRuntime();

            if (Math.abs(error) < 30) {
                i += .025 * Math.abs(error) * deltaTime;
            }
            if (i > 0.3) {
                i = 0.3;
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

}


