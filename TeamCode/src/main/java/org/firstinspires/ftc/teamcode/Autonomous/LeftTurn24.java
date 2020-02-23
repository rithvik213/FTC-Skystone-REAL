package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Autonomous
public class LeftTurn24 extends LinearOpMode {
    HardwareBot robot;
    public void runOpMode() throws InterruptedException {
        robot = new HardwareBot(this);

        waitForStart();

        robot.drive.turnIMU(24,0.4,false);
        sleep(800);

    }
}
