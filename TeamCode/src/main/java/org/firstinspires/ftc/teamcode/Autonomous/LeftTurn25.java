package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Autonomous
public class LeftTurn25 extends LinearOpMode {
    HardwareBot robot;
    public void runOpMode() throws InterruptedException {
        robot = new HardwareBot(this);

        waitForStart();

        robot.drive.turnIMUTest(25,0.4,false);
        sleep(800);
        robot.drive.turnIMUTest(25,0.4,true);

    }
}
