package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

import static java.lang.Thread.sleep;

@Disabled
public class Drive {

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

    public Drive(LinearOpMode mode) {
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

    public void straightMove() {

    }

        public void orientationCorrect() {
            while (opMode.opModeIsActive() && !getOut) {
                currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
                currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
                closeEnough = Math.abs(currentRangeL - currentRangeR);
//            telemetry.addData("Close Enough!: ",Math.abs(currentRangeL - currentRangeR) );
//            telemetry.addData("Range Left: ",rangeSensor.getDistance(DistanceUnit.INCH));
//            telemetry.addData("Range Right:", rangeSensor2.getDistance(DistanceUnit.INCH));
//            telemetry.update();

                if (currentRangeL < currentRangeR) {
                    currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
                    currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
                    closeEnough = Math.abs(currentRangeL - currentRangeR);

                    leftFront.setPower(-0.25);
                    leftBack.setPower(-0.25);
                    rightFront.setPower(0.25);
                    rightBack.setPower(0.25);

                    opMode.telemetry.addData("Range Left: ", currentRangeL);
                    opMode.telemetry.addData("Range Right:", currentRangeR);
                    opMode.telemetry.update();
                } else if (currentRangeR < currentRangeL) {
                    currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
                    currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
                    closeEnough = Math.abs(currentRangeL - currentRangeR);

                    leftFront.setPower(0.25);
                    leftBack.setPower(0.25);
                    rightFront.setPower(-0.25);
                    rightBack.setPower(-0.25);

                    opMode.telemetry.addData("Range Left: ", currentRangeL);
                    opMode.telemetry.addData("Range Right:", currentRangeR);
                    opMode.telemetry.update();
                } else if (closeEnough < 0.1) {
                    currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
                    currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
                    closeEnough = Math.abs(currentRangeL - currentRangeR);

                    opMode.telemetry.addData("Close Enough!: ", closeEnough);
                    opMode.telemetry.addData("Range Left: ", currentRangeL);
                    opMode.telemetry.addData("Range Right:", currentRangeR);
                    opMode.telemetry.update();
                    opMode.telemetry.update();

                    leftFront.setPower(0);
                    leftBack.setPower(0);
                    rightFront.setPower(0);
                    rightBack.setPower(0);

                    getOut = true;
                }
            }
        }

    public void moveDistance2(int distance, double leftpower, double rightpower, boolean direction) {
        resetEncoders();
        runUsingEncoders();

        double target = leftFront.getCurrentPosition() + Math.round(Math.abs(distance) * COUNTS_PER_INCH);

            while (Math.abs(target - leftFront.getCurrentPosition()) > 45 && !opMode.isStopRequested()) {
                leftFront.setPower(leftpower);
                leftBack.setPower(leftpower);
                rightFront.setPower(rightpower);
                rightBack.setPower(rightpower);
                opMode.telemetry.addData("Current Position", leftFront.getCurrentPosition());
                opMode.telemetry.addData("Distance to go", target + leftFront.getCurrentPosition());
                opMode.telemetry.update();
            }
        setPowerSides(0.0);
    }

    public void moveDistanceRUNTOPOS(int distance, double power, boolean direction) {
        double target = rightFront.getCurrentPosition() + Math.round(Math.abs(distance) * COUNTS_PER_INCH);

    }

    public void setPowerSides(double power) {
        leftFront.setPower(power);
        rightFront.setPower(power);
        leftBack.setPower(power);
        rightBack.setPower(power);
    }

    public void goStraight(double power) {
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);
    }

    public void resetEncoders() {
        //leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void runUsingEncoders() {
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runWithoutEncoders() {
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public void testMethods() {
        leftBack.setPower(-0.2);
        rightBack.setPower(-0.2);
        opMode.sleep(2000);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    public double[] getXYlocation() {
        /*
        getIMUReady();
        resetEncoders();
        runUsingEncoders();
        */
        double startPos_L = leftFront.getCurrentPosition();
        double startPos_R = rightFront.getCurrentPosition();
        double[] coordinates = {0,0};
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        distance = ((leftFront.getCurrentPosition() - startPos_L) + (rightFront.getCurrentPosition() - startPos_R)) / 2.0;
        distance/=COUNTS_PER_MOTOR_REV;

        opMode.telemetry.addData("Distance Traveled", distance);

        xlocation += distance * Math.cos(angles.firstAngle);
        coordinates[0] = xlocation;
        opMode.telemetry.addData("X_Loc:", coordinates[0]);
        ylocation += distance * Math.sin(angles.firstAngle);
        coordinates[1] = ylocation;
        opMode.telemetry.addData("Y_Loc:", coordinates[1]);
        opMode.telemetry.update();

        return coordinates;
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

    public void turnIMU(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime, deltaTime;
        double i = 0;

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;
        angle-=2; //compensation for innacurate turns

        if (direction) {
            target = angle - initialPos;
        }
        else {
            target = angle + initialPos;
        }

        while(opMode.opModeIsActive() && ((Math.abs(target - Math.abs(currentPos))) > 1)) {
            initTime = opMode.getRuntime();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES); //ZYX
            currentPos = Math.abs(angles.firstAngle);

            double power = Range.clip((Math.abs((currentPos - target) / (125.0)) + i), .3, .7);

            opMode.telemetry.addData("Current Position: ", currentPos);
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
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

            if (Math.abs(currentPos - target) < 30)
                i += .025 * Math.abs(currentPos - target) * deltaTime;

            if (i > 0.3) {
                i = 0.3;
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void turnIMU2(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime = 0, deltaTime = 0;
        double i = 0;
        double error = 100; //used to start while loop
        double KP = 0.3;
        double KD = 0;
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

            double power = Range.clip((KP * error + KD * (error - previous_error) / deltaTime), .3, .7);

            opMode.telemetry.addData("Current Position: ", currentPos);
            opMode.telemetry.update();
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
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

            previous_error = error;

        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
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

            double power = Range.clip((Math.abs(error)/125),.3,.7); //double power = Range.clip((Math.abs(error)/125) + i,.3,.7);

            if (error < 0) {
                power = -power;
            }
            else {
                power = +power;
            }

            opMode.telemetry.addData("Current Position: ", currentPos);
            opMode.telemetry.addData("Distance to go: ", error);
            opMode.telemetry.addData("Target: ", (int) target);
            opMode.telemetry.addData("Power: ", power);
            opMode.telemetry.update();

            leftFront.setPower(power);
            leftBack.setPower(power);
            rightFront.setPower(-power);
            rightBack.setPower(-power);

            deltaTime = opMode.getRuntime() - initTime;
            initTime = opMode.getRuntime();

            previous_error = error;

            /*if (Math.abs(error) < 30)
                i += .025 * Math.abs(currentPos - target) * deltaTime;

            if (i > 0.3) {
                i = 0.3;
            }*/

        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }


    public void turnIMUThreeWheel(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime, deltaTime;
        double i = 0;

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;

        if (direction) {
            target = angle - initialPos;
        }
        else {
            target = angle + initialPos;
        }

        while(opMode.opModeIsActive() && ((Math.abs(target - Math.abs(currentPos))) > 1)) {
            initTime = opMode.getRuntime();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = Math.abs(angles.firstAngle);

            double power = Range.clip((Math.abs((currentPos - target) / (100.0)) + i), .3, .7);

            opMode.telemetry.addData("Current Position: ", currentPos);
            opMode.telemetry.update();
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
            opMode.telemetry.update();

            if(direction) {
                leftFront.setPower(-power);
                //leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
            } else {
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
            }

            deltaTime = opMode.getRuntime() - initTime;

            if (Math.abs(currentPos - target) < 30)
                i += .01 * Math.abs(currentPos - target) * deltaTime;

            if (i > 0.3) {
                i = 0.3;
            }
        }
        leftFront.setPower(0);
        //leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void turnIMUOneSide(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime, deltaTime;
        double i = 0;

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;

        if (direction) {
            target = Math.abs(angle - Math.abs(initialPos));
        }
        else {
            target = Math.abs(angle + initialPos);
        }

        opMode.telemetry.addData("Angle when target calculated: " ,Math.abs(initialPos));
        opMode.telemetry.addData("Target angle: ", target);
        opMode.telemetry.update();

        while(opMode.opModeIsActive() && ((Math.abs(target - Math.abs(currentPos))) > 2)) {
            initTime = opMode.getRuntime();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = Math.abs(angles.firstAngle);

            double power = Range.clip(Math.abs((currentPos - target) / (100.0)), .3, .7);

            opMode.telemetry.addData("Current Position: ", Math.abs(currentPos));
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
            opMode.telemetry.update();

            if(direction) {
                leftFront.setPower(power);
                leftBack.setPower(power);
                //rightFront.setPower(power);
                //rightBack.setPower(power);
            } else {
                //leftFront.setPower(power);
                //leftBack.setPower(power);
                rightFront.setPower(power);
                rightBack.setPower(power);
            }

            deltaTime = opMode.getRuntime() - initTime;

            if (((Math.abs(target - Math.abs(currentPos)) < 20)))
                i += .01 * Math.abs(currentPos - target) * deltaTime;

            if (i > 0.3) {
                i = 0.3;
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void turnIMUSimple(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        //double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime;


        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;

        if (direction) {
            target = Math.abs(angle - Math.abs(initialPos));
        }
        else {
            target = Math.abs(angle + initialPos);
        }
        opMode.telemetry.addData("Angle when target calculated: " ,Math.abs(initialPos));
        opMode.telemetry.addData("Target angle: ", target);
        opMode.telemetry.update();

        while(opMode.opModeIsActive() && ((Math.abs(target - Math.abs(currentPos))) > 2)) {
            initTime = opMode.getRuntime();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = Math.abs(angles.firstAngle);

            double power = Range.clip(Math.abs((currentPos - target) / (100.0)), .3, .7);

            opMode.telemetry.addData("Current Position: ", Math.abs(currentPos));
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
            opMode.telemetry.update();

            if(direction) {
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(0);
                rightBack.setPower(0);
            } else {
                leftFront.setPower(0);
                leftBack.setPower(0);
                rightFront.setPower(power);
                rightBack.setPower(power);
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }


    /*public void turnIMUOneSideBlue(double angle, double speed, boolean direction) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime, deltaTime;
        double i = 0;

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;

        if (direction) {
            target = Math.abs(angle + initialPos);
        }
        else {
            target = Math.abs(angle - Math.abs(initialPos));
        }

        opMode.telemetry.addData("Angle when target calculated: " ,Math.abs(initialPos));
        opMode.telemetry.addData("Target angle: ", target);
        opMode.telemetry.update();

        while(opMode.opModeIsActive() && ((Math.abs(target - Math.abs(currentPos))) > 2)) {
            initTime = opMode.getRuntime();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = Math.abs(angles.firstAngle);

            double power = Range.clip((Math.abs((currentPos - target) / (100.0)) + i), .3, .7);

            opMode.telemetry.addData("Current Position: ", Math.abs(currentPos));
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
            opMode.telemetry.update();

            if(direction) {
                leftFront.setPower(-power);
                leftBack.setPower(-power);
                rightFront.setPower(power);
                rightBack.setPower(power);
            } else {
                leftFront.setPower(power);
                leftBack.setPower(power);
                rightFront.setPower(-power);
                rightBack.setPower(-power);
            }

            deltaTime = opMode.getRuntime() - initTime;

            if (((Math.abs(target - Math.abs(currentPos)) < 20)))
                i += .01 * Math.abs(currentPos - target) * deltaTime;

            if (i > 0.3) {
                i = 0.3;
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
    */

    public void Auto2ndTurnRed(double angle, double speed) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double startTime = opMode.getRuntime();
        double deltaAngle = 0, initTime, deltaTime;
        double i = 0;

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;
        double target = 0;

            //target = Math.abs(angle - Math.abs(initialPos));
            target = Math.abs(angle + initialPos);

        opMode.telemetry.addData("Angle when target calculated: " ,Math.abs(initialPos));
        opMode.telemetry.addData("Target angle: ", target);
        opMode.telemetry.update();

        while(opMode.opModeIsActive() && ((Math.abs(target - Math.abs(currentPos))) > 2)) {
            initTime = opMode.getRuntime();
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = Math.abs(angles.firstAngle);

            double power = Range.clip((Math.abs((currentPos - target) / (100.0)) + i), .3, .7);

            opMode.telemetry.addData("Current Position: ", Math.abs(currentPos));
            opMode.telemetry.addData("Distance to go: ", (Math.abs(target - Math.abs(currentPos))));
            opMode.telemetry.update();

            rightFront.setPower(power);
            rightBack.setPower(power);

            deltaTime = opMode.getRuntime() - initTime;

            if (((Math.abs(target - Math.abs(currentPos)) < 20)))
                i += .01 * Math.abs(currentPos - target) * deltaTime;

            if (i > 0.3) {
                i = 0.3;
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void PTurnIMU(double angle, double speed) {
        getIMUReady();
        double P_COEFFICIENT = 0.005;
        double error = 0;
        double output = 0;

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double initialPos = angles.firstAngle;
        double currentPos = initialPos;

        double target = angle + initialPos;
        do {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentPos = Math.abs(angles.firstAngle);
            error = target - (currentPos + initialPos);

            output = Range.clip(error * P_COEFFICIENT,-1,1);

            leftFront.setPower(output);
            leftBack.setPower(output);
            rightFront.setPower(-output);
            rightBack.setPower(-output);

            opMode.telemetry.addData("Distance left: ", error);
            opMode.telemetry.addData("Current Position: ", currentPos);
            opMode.telemetry.addData("Motor Output: ", output);
            opMode.telemetry.update();

        } while (Math.abs(error) > 1 && opMode.opModeIsActive());
    }

   /* public  void goToPosition(final double x, final double y, final double movementSpeed, final double preferredAngle, final double turnSpeed) {
        final double distanceToTarget = Math.hypot(x - xlocation, y - ylocation);
        final double absoluteAngleToTarget = Math.atan2(y - ylocation, x - xlocation);
        final double relativeAngleToPoint = AngleWrap(absoluteAngleToTarget - (worldAngle_rad - Math.toRadians(90.0)));
        final double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
        final double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;
        final double movementXPower = relativeXToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
        final double movementYPower = relativeYToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
        movement_x = movementXPower * movementSpeed;
        movement_y = movementYPower * movementSpeed;
        final double relativeTurnAngle = relativeAngleToPoint - Math.toRadians(180.0) + preferredAngle;
        movement_turn = Range.clip(relativeTurnAngle / Math.toRadians(30.0), -1.0, 1.0) * turnSpeed;
        if (distanceToTarget < 10.0) {
            movement_turn = 0.0;
        }

    }
*/
    public  double AngleWrap(double angle) {
        while (angle < -180) {
            angle += 2 * 180;
        }

        while (angle > 180) {
            angle-= 2 * 180;
        }

        return angle;
    }

    public void foundationClamp() {
        sLeft.setPosition(0.4);
        sRight.setPosition(0.5);
    }


}
