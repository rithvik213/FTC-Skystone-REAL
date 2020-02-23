package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RedNear extends LinearOpMode {
    HardwareBot robot;
    ElapsedTime timer;
    int pattern  = 0;

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
            case 1: //when SKYSTONE is CLOSEST to the WALL
                //test
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.turnIMUOneSide(10,0.3,false);
                robot.drive.moveDistance(24,0.4,true);
                sleep(700);
                robot.drive.turnIMUOneSide(10,0.3,true);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(12,0.4, false);
                robot.drive.turnIMU(90,0.4,true); // initially false before 85
                robot.drive.moveDistance(61,0.4,true);  // initially true
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(10,0.4,false);
                robot.drive.moveDistance(46,0.4,false);
                robot.drive.turnIMUOneSide(90,0.3,false);
                robot.drive.moveDistance(6,0.4,true);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(12,0.4,false);
                robot.drive.turnIMU(88,0.4,true);
                robot.drive.moveDistance(48,0.4,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(9,0.4,false);

                break;

            case 2: //when SKYSTONE is in the MIDDLE
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.turnIMUOneSide(37,0.3,false); //prev 43
                robot.drive.moveDistance(33,0.4,true); //prev 20
                sleep(500);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(18,0.4,false);
                robot.drive.turnIMU((90 + 32),0.4,true);
                robot.drive.moveDistance(68,0.4,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(68,0.4,false);
                robot.drive.turnIMUOneSide(90,0.4,false);
                robot.drive.moveDistance(7,0.4,true);
                robot.accessories.Grab();
                sleep(700);
                robot.drive.moveDistance(14,0.4,false);
                robot.drive.turnIMU(86,0.4,true);
                robot.drive.moveDistance(60,0.4,true);
                robot.accessories.readyToGrabOrUnlatch();
                sleep(700);
                robot.drive.moveDistance(9,0.4,false);

                break;

            case 3://when SKYSTONE is the FARTHEST away from the WALL

                //grabs the SKYSTONE closest to the wall (out of the two in the pattern)
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.moveDistance(4,0.4,true);
                robot.drive.turnIMU(24,0.4,false);
                robot.drive.moveDistance(20,0.4,true);
                robot.drive.turnIMU(25,0.4,true);
                robot.drive.moveDistance(2,0.4,true);
                robot.accessories.Grab();           //sets power to auto-servo
                sleep(700);            //waits for servo to make contact before next code

                /*one tile on the field is equal to 20 units of distance (measuring form front of robot)*/

                robot.drive.moveDistance(17,0.4, false);        //moves back
                robot.drive.turnIMU(89,0.4,true);                 //turns toward the skybride
                robot.drive.moveDistance(66,0.4,true);          //moves under bridge to other side
                robot.accessories.readyToGrabOrUnlatch(); // releases the SKYSTONE
                sleep(700);

                robot.drive.moveDistance(42,0.4,false);     //moves back to grap other stone
                robot.drive.turnIMU(91,0.4,false);     // turns to grab the stone closest to bridge
                robot.drive.moveDistance(13,0.4,true);
                robot.accessories.Grab();                                           //Grabs second SKYSTONE
                sleep(700);

                robot.drive.moveDistance(14,0.4,false);
                robot.drive.turnIMU(87,0.4,true);
                robot.drive.moveDistance(45,0.4,true);
                robot.accessories.readyToGrabOrUnlatch(); // releases 2nd SKYSTONE
                sleep(700);

                robot.drive.moveDistance(8,0.4,false); // parks under the bridge closest to center

                /*
                //when SKYSTONE is the FARTHEST away from the WALL

                //grabs the SKYSTONE closest to the wall (out of the two in the pattern)
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.turnIMUOneSide(24,0.4,false);
                robot.drive.moveDistance(18,0.4,true);
                robot.drive.turnIMUOneSide(24,0.4,true);
                robot.drive.moveDistance(2,0.4,true);
                robot.accessories.Grab();           //sets power to auto-servo
                sleep(700);            //waits for servo to make contact before next code

                /*one tile on the field is equal to 20 units of distance (measuring form front of robot)*/
                /*
               //when SKYSTONE is the FARTHEST away from the WALL

                //grabs the SKYSTONE closest to the wall (out of the two in the pattern)
                robot.accessories.readyToGrabOrUnlatch();
                robot.drive.turnIMUOneSide(24,0.4,false);
                robot.drive.moveDistance(18,0.4,true);
                robot.drive.turnIMUOneSide(24,0.4,true);
                robot.drive.moveDistance(2,0.4,true);
                robot.accessories.Grab();           //sets power to auto-servo
                sleep(700);            //waits for servo to make contact before next code

                /*one tile on the field is equal to 20 units of distance (measuring form front of robot)

                robot.drive.moveDistance(17,0.4, false);        //moves back
                robot.drive.turnIMU(89,0.4,true);                 //turns toward the skybride
                robot.drive.moveDistance(66,0.4,true);          //moves under bridge to other side
                robot.accessories.readyToGrabOrUnlatch(); // releases the SKYSTONE
                sleep(700);

                robot.drive.moveDistance(42,0.4,false);     //moves back to grap other stone
                robot.drive.turnIMU(91,0.4,false);     // turns to grab the stone closest to bridge
                robot.drive.moveDistance(13,0.4,true);
                robot.accessories.Grab();                                           //Grabs second SKYSTONE
                sleep(700);

                robot.drive.moveDistance(14,0.4,false);
                robot.drive.turnIMU(87,0.4,true);
                robot.drive.moveDistance(45,0.4,true);
                robot.accessories.readyToGrabOrUnlatch(); // releases 2nd SKYSTONE
                sleep(700);

                robot.drive.moveDistance(8,0.4,false); // parks under the bridge closest to center
                 */
                break;

            default:
                telemetry.addData("No skystone found: ", pattern);
        }
    }
}
