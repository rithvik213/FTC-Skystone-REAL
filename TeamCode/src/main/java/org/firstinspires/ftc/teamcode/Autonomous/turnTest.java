package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Autonomous.HardwareBot;

@Autonomous
@Disabled
public class turnTest extends LinearOpMode {
    HardwareBot robot;
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;
    public void runOpMode() throws InterruptedException{
        robot = new HardwareBot(this);

        waitForStart();

        robot.drive.turnIMUOneSide(45,0.4,true);
        sleep(1500);
        robot.drive.turnIMUOneSide(45,0.4,false);


        leftFront = hardwareMap.get(DcMotor.class, "lf");
        rightFront = hardwareMap.get(DcMotor.class, "rf");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.get(DcMotor.class, "lb");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


        //leftBack.setPower(0.4);
        //leftFront.setPower(0.4);
        rightBack.setPower(0.4);
        rightFront.setPower(0.4);
        sleep(700);
    }

}
