package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class NewRedNear extends LinearOpMode {
    HardwareBot robot;
    ElapsedTime timer;
    int pattern  = 0;

    //all patern constants

        //forward moves
        final int firstForwardMove =  10;
        final int thirdForwardMove = 20;
        final int secondStoneFirstForwardMove = 9;

        //backward move
        final int firstBackwardMove = 14;
        final int skybridgeParkMove = 9;

        //left and right turns
        final double rightTurn = 90;
        final double leftTurn = 90;

        //stone width
        final int stoneWidth = 4;

        //stone length
        final int stoneLength = 8;

        //constant forward/backward power
        final double power = 0.4;

        //constant sleep
        final int sleepAngle  = 700;

        //skybridge distance
        final int skybridgeDistance = 66; // ( 22" * 3 = 66)
        final int robotWidth = 18;
        final int bufferMove = 2;
        final int robotInitialPos = 24;

    // pattern wise constants

        // 3rd Skystone used as reference for all path moves
        final double secondForwardMove = robotInitialPos + (robotWidth / 2) - (2.5 * stoneLength); // 13
        final double fourthForwardMove = skybridgeDistance - 2.5 * stoneLength + robotWidth + bufferMove; // 66
        final double secondStoneSecondForwardMove = skybridgeDistance - 0.5 * stoneLength + robotWidth + bufferMove; // may be 45
        final double secondStoneBackMove = skybridgeDistance - 0.5 * stoneLength + robotWidth + bufferMove; // may be 42

    public void runOpMode() throws InterruptedException {
        //INITIALIZE the robot

        robot = new HardwareBot(this);
        timer = new ElapsedTime(); // sets up the timer to track seconds
        robot.camera.setAllianceColor("red"); // gets the

        waitForStart();

        timer.reset();
        // prints the pattern and timer to the driver station
        while (timer.seconds() <= 2 && !isStopRequested()) {
            telemetry.addData("Pattern: ",robot.camera.getPattern());
            pattern = robot.camera.getPattern();
            telemetry.addData("Timer: ", timer.seconds());
            telemetry.update();
        }

        switch (pattern) {
            case 1:
                //First Stone
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.moveDistance(firstForwardMove,power,true);
                robot.drive.turnIMUTest(leftTurn,power,false);
                sleep(sleepAngle);
                robot.drive.moveDistance(secondForwardMove - stoneLength,power,true);
                robot.drive.turnIMUTest(rightTurn,power,true);
                sleep(sleepAngle);
                robot.drive.moveDistance(thirdForwardMove,power,true);
                robot.accessories.Grab();
                sleep(sleepAngle);
                robot.drive.moveDistance(firstBackwardMove,power,false);
                robot.drive.turnIMUTest(rightTurn,power,true);
                robot.drive.moveDistance(fourthForwardMove - stoneLength,power,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(sleepAngle);

                //Second Stone same as third path
                robot.drive.moveDistance(secondStoneBackMove,power,false);
                robot.drive.turnIMUTest(leftTurn,power,false);
                robot.drive.moveDistance(secondStoneFirstForwardMove,power,true);
                robot.accessories.Grab();
                sleep(sleepAngle);
                robot.drive.moveDistance(firstBackwardMove,power,true);
                robot.drive.turnIMUTest(rightTurn,power,true);
                robot.drive.moveDistance(secondStoneSecondForwardMove,power,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(sleepAngle);
                robot.drive.moveDistance(skybridgeParkMove,power,false);

            case 2:
                //First Stone
                robot.drive.moveDistance(fourthForwardMove,power,true);
                robot.drive.turnIMUTest(rightTurn,power,true);
                sleep(sleepAngle);
                robot.drive.moveDistance((2 * stoneLength) - secondForwardMove,power,true);
                robot.drive.turnIMUTest(leftTurn,power,false);
                sleep(sleepAngle);
                robot.drive.moveDistance(thirdForwardMove,power,true);
                robot.accessories.Grab();
                sleep(sleepAngle);
                robot.drive.moveDistance(firstBackwardMove,power,false);
                robot.drive.turnIMUTest(rightTurn,power,true);
                robot.drive.moveDistance(70 - (2 * stoneLength),power,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(sleepAngle);

                //Second stone
                robot.drive.moveDistance(fourthForwardMove + stoneLength,power,false);
                robot.drive.turnIMUTest(leftTurn,power,false);
                robot.drive.moveDistance(thirdForwardMove,power,true);
                robot.accessories.Grab();
                sleep(sleepAngle);
                robot.drive.moveDistance(firstBackwardMove,power,false);
                robot.drive.turnIMUTest(leftTurn,power,false);
                robot.drive.moveDistance(fourthForwardMove + stoneLength,power,false);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(sleepAngle);

            case 3:
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.moveDistance(firstForwardMove,power,true);
                robot.drive.turnIMUTest(leftTurn,power,false);
                sleep(sleepAngle);
                robot.drive.moveDistance(secondForwardMove,power,true);
                robot.drive.turnIMUTest(rightTurn,power,true);
                sleep(sleepAngle);
                robot.drive.moveDistance(thirdForwardMove,power,true);
                robot.accessories.Grab();
                sleep(sleepAngle);
                robot.drive.moveDistance(firstBackwardMove,power,false);
                robot.drive.turnIMUTest(rightTurn,power,true);
                robot.drive.moveDistance(fourthForwardMove,power,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(sleepAngle);

                //Second Stone
                robot.drive.moveDistance(secondStoneBackMove,power,false);
                robot.drive.turnIMUTest(leftTurn,power,false);
                robot.drive.moveDistance(secondStoneFirstForwardMove,power,true);
                robot.accessories.Grab();
                sleep(sleepAngle);
                robot.drive.moveDistance(firstBackwardMove,power,true);
                robot.drive.turnIMUTest(rightTurn,power,true);
                robot.drive.moveDistance(secondStoneSecondForwardMove,power,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(sleepAngle);
                robot.drive.moveDistance(skybridgeParkMove,power,false);
        }
    }
}
