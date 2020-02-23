package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class newTurnTest extends LinearOpMode {
    HardwareBot robot;
    public void runOpMode() throws InterruptedException {
        robot = new HardwareBot(this);

        waitForStart();

        robot.drive.turnIMUTest(25,0.4,false);
        sleep(700);
        robot.drive.turnIMUTest(25,0.4,true);
    }
}
