package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class BlueNear extends LinearOpMode {
    HardwareBot robot;
    ElapsedTime timer;
    int pattern  = 0;

    public void runOpMode() throws InterruptedException {
        robot = new HardwareBot(this);
        timer = new ElapsedTime();
        robot.camera.setAllianceColor("blue");

        timer.reset();
        while (timer.seconds() <= 2 && !isStopRequested()) {
            telemetry.addData("Pattern: ",robot.camera.getPattern());
            pattern = robot.camera.getPattern();
            telemetry.addData("Timer: ", timer.seconds());
            telemetry.update();
        }

        waitForStart();

        switch (pattern) {
            case 1:
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.turnIMUOneSide(10,0.3,true);
                robot.drive.moveDistance(25,0.4,true);
                sleep(700);
                robot.drive.turnIMUOneSide(10,0.3,false);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(13,0.4, false);
                robot.drive.turnIMU(90,0.4,false); // initially false before 85
                robot.drive.moveDistance(61,0.4,true);  // initially true
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(10,0.4,false);
                robot.drive.moveDistance(54,0.4,false);
                robot.drive.turnIMUOneSide(90,0.3,true);
                robot.drive.moveDistance(4,0.4,true);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(12,0.4,false);
                robot.drive.turnIMU(88,0.4,false);
                robot.drive.moveDistance(48,0.4,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(8,0.4,false);

//                robot.accessories.readyToGrabOrUnlatch();
//                robot.drive.turnIMUOneSide(12,0.3,true);
//                robot.drive.moveDistance(21,0.4,true);
//                sleep(700);
//                robot.drive.turnIMUOneSide(12,0.3,false);
//                robot.accessories.Grab();
//                sleep(700);
//                robot.drive.moveDistance(13,0.4, false);
//                robot.drive.turnIMU(82,0.4,false); // initially false before 85
//                robot.drive.moveDistance(58,0.4,true);  // initially true
//                robot.accessories.readyToGrabOrUnlatch();
//                sleep(700);
//                robot.drive.moveDistance(8,0.4,false);




                //robot.drive.turnIMUOneSide(40,0.3,true);
                //robot.drive.moveDistance(43,0.4,true);
                break;

            case 2:
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.turnIMUOneSide(42,0.3,true); //prev 43
                robot.drive.moveDistance(30,0.4,true); //prev 20
                sleep(500);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(29,0.4,false);
                robot.drive.turnIMU((90 + 24),0.4,false);
                robot.drive.moveDistance(62,0.4,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(70,0.5,false);
                robot.drive.turnIMUOneSide(90,0.4,true);
                robot.drive.moveDistance(7,0.4,true);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(20,0.4,false);
                robot.drive.turnIMU(82,0.4,false);
                robot.drive.moveDistance(54,0.4,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(9,0.4,false);
//                robot.accessories.readyToGrabOrUnlatch();
//                robot.drive.turnIMUOneSide(31,0.3,true); //prev 37
//                robot.drive.moveDistance(36,0.4,true);
//                sleep(700);
//                robot.drive.turnIMUOneSide(31,0.3,false);
//                robot.accessories.Grab();
//                sleep(700);
//                robot.drive.moveDistance(10,0.4, false);
//                //PREVIOUS PIVOT TURN
//                robot.drive.turnIMU(84,0.3,false);
//                //robot.drive.turnIMUThreeWheel(90,0.3,true);
//                robot.drive.moveDistance(73,0.4,true);
//                robot.accessories.readyToGrabOrUnlatch();
//                sleep(700);
//                robot.drive.moveDistance(62,0.4,false);
//                robot.drive.turnIMUOneSide(89,0.4,true);
//                robot.drive.moveDistance(9,0.4,true);
//                robot.accessories.Grab();
//                sleep(700);
//                robot.drive.moveDistance(13,0.4,false);
//                robot.drive.turnIMU(87,0.4,false);
//                robot.drive.moveDistance(49,0.4,true);
//                robot.accessories.readyToGrabOrUnlatch();
//                sleep(700);
//                robot.drive.moveDistance(7,0.4,false);
//                /*robot.accessories.readyToGrabOrUnlatch();
//                robot.drive.turnIMUOneSide(37.5,0.3,true); //prev 37
//                robot.drive.moveDistance(23.5,0.4,true);
//                sleep(700);
//                robot.drive.turnIMUOneSide(36,0.3,false);
//                robot.accessories.Grab();
//                sleep(700);
//                robot.drive.turnIMU(17,0.3,true);
//                robot.drive.moveDistance(11,0.4, false);
//                robot.drive.turnIMU(100,0.3,false);
//                //NEED TO TEST
                /*robot.drive.turnIMU(17,0.3,true);
                robot.drive.moveDistance(11,0.4, false);
                robot.drive.turnIMU(82,0.4,false); // initially false before 85
                 */
                /*
                robot.drive.moveDistance(68,0.4,true);  // initially true
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(56,0.4,false); //false before
                robot.drive.turnIMUOneSide(91,0.4,true); // true
                robot.drive.moveDistance(7,0.4,true);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(9,0.4,false);
                robot.drive.turnIMU(83,0.4,false); //false before
                robot.drive.moveDistance(47,0.4,true); //true before
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                */
                break;

            case 3://when SKYSTONE is the FARTHEST away from the WALL

                //grabs the SKYSTONE closest to the wall (out of the two in the pattern)
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.goStraight(0);
                robot.drive.turnIMUOneSide(28,0.5,true);
                robot.drive.moveDistance(16,0.4,true);
                robot.drive.turnIMUOneSide(28,0.5,false);
                robot.drive.moveDistance(2,0.4,true);
                robot.accessories.Grab();           //sets power to auto-servo
                sleep(700);            //waits for servo to make contact before next code

                /*one tile on the field is equal to 20 units of distance (measuring form front of robot)*/

                robot.drive.moveDistance(19,0.4, false);        //moves back
                robot.drive.turnIMU(84,0.5,false);                 //turns toward the skybride
                robot.drive.moveDistance(66,0.4,true);          //moves under bridge to other side
                robot.accessories.readyToGrabOrUnlatch(); // releases the SKYSTONE
                sleep(700);

                robot.drive.moveDistance(59,0.4,false);     //moves back to grab other stone
                robot.drive.turnIMUOneSide(91,0.4,true);     // turns to grab the stone closest to bridge
                robot.drive.moveDistance(7,0.4,true);
                robot.accessories.Grab();                                           //Grabs second SKYSTONE
                sleep(700);

                robot.drive.moveDistance(17,0.4,false);     //moves back and turns
                robot.drive.turnIMU(83,0.4,false);
                robot.drive.moveDistance(46,0.4,true);
                robot.accessories.readyToGrabOrUnlatch(); // releases 2nd SKYSTONE
                sleep(700);

                robot.drive.moveDistance(14,0.4,false); // parks under the bridge closest to center
                break;

            default:
                telemetry.addData("No skystone found: ", pattern);
        }
    }
}
