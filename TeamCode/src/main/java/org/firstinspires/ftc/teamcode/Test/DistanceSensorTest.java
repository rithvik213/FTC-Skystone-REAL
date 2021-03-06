package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
@Autonomous
@Disabled
public class DistanceSensorTest extends LinearOpMode {

    ModernRoboticsI2cRangeSensor rangeSensor;
    ModernRoboticsI2cRangeSensor rangeSensor2;

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;

    double currentRangeL = 0;
    double currentRangeR = 0;
    double closeEnough = 0;

    boolean getOut = false;

    private DistanceSensor sensorRange;

    public void runOpMode() throws InterruptedException {
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeL");
        rangeSensor2 = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeR");
        leftFront = hardwareMap.get(DcMotor.class, "lf");
        rightFront = hardwareMap.get(DcMotor.class, "rf");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.get(DcMotor.class, "lb");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        //sensorRange = hardwareMap.get(DistanceSensor.class,"range2");
        waitForStart();

        while (opModeIsActive() && !getOut) {
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

                telemetry.addData("Close Enough!: ", closeEnough);
                telemetry.addData("Range Left: ", currentRangeL);
                telemetry.addData("Range Right:", currentRangeR);
                telemetry.update();
            } else if (currentRangeR < currentRangeL) {
                currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
                currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
                closeEnough = Math.abs(currentRangeL - currentRangeR);

                leftFront.setPower(0.25);
                leftBack.setPower(0.25);
                rightFront.setPower(-0.25);
                rightBack.setPower(-0.25);

                telemetry.addData("Close Enough!: ", closeEnough);
                telemetry.addData("Range Left: ", currentRangeL);
                telemetry.addData("Range Right:", currentRangeR);
                telemetry.update();
            } else if (closeEnough < 0.2) {
                currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
                currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
                closeEnough = Math.abs(currentRangeL - currentRangeR);

                telemetry.addData("Close Enough!: ", closeEnough);
                telemetry.addData("Range Left: ", currentRangeL);
                telemetry.addData("Range Right:", currentRangeR);
                telemetry.update();

                leftFront.setPower(0);
                leftBack.setPower(0);
                rightFront.setPower(0);
                rightBack.setPower(0);

                getOut = true;
            }

        }

    }
}
