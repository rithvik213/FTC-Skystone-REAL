package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class RightTurn90 extends LinearOpMode {
    HardwareBot robot;
    public void runOpMode() throws InterruptedException {
        robot = new HardwareBot(this);

        waitForStart();

        robot.drive.turnIMU(90,0.4,true);
        sleep(800);

    }
}
