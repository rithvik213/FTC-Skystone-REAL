package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
@Autonomous
@Disabled
public class DistanceSensorValues extends LinearOpMode {
    ModernRoboticsI2cRangeSensor rangeSensor;
    ModernRoboticsI2cRangeSensor rangeSensor2;

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;

    double currentRangeL = 0;
    double currentRangeR = 0;
    double closeEnough = 0;

    public void runOpMode() throws InterruptedException {
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeL");
        rangeSensor2 = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeR");

        waitForStart();

        while (opModeIsActive())  {

            currentRangeL = rangeSensor.getDistance(DistanceUnit.INCH);
            currentRangeR = rangeSensor2.getDistance(DistanceUnit.INCH);
            closeEnough = Math.abs(currentRangeL - currentRangeR);

            telemetry.addData("Close Enough!: ", closeEnough);
            telemetry.addData("Range Left: ", currentRangeL);
            telemetry.addData("Range Right:", currentRangeR);
            telemetry.update();
        }
    }
}
