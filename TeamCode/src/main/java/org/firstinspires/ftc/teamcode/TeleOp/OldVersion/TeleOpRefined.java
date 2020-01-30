package org.firstinspires.ftc.teamcode.TeleOp.OldVersion;

//Importing the hardware classes used in program
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

@TeleOp
public class TeleOpRefined extends LinearOpMode {

    //HardwareBot robot;
    double left;
    double right;
    double drive;
    double turn;
    double max;

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack;
    DcMotor rightBack;

    //CRServo crservo = null;

    DcMotor slides = null;

    Servo clutch = null;
    double up = 0;
    double down = 0;
    Servo autoClutch = null;
    Servo foundLeft = null;
    Servo foundRight = null;


    Deadline gamepadRateLimit;
    private final int GAMEPAD_LOCKOUT = 500;

    public void runOpMode() throws InterruptedException {
        //robot = new HardwareBot(this);

        /*Initializing the drivetrain motors*/
        leftFront = hardwareMap.get(DcMotor.class, "lf");
        rightFront = hardwareMap.get(DcMotor.class, "rf");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.get(DcMotor.class, "lb");
        rightBack = hardwareMap.get(DcMotor.class, "rb");
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        /*Initializing the motor for the slides*/
        slides = hardwareMap.dcMotor.get("slides");
        slides.setDirection(DcMotorSimple.Direction.REVERSE);
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //need to change to RUN_USING_ENCODER

        /*Initializing the clutch servos*/
        clutch = hardwareMap.get(Servo.class,"clutch");
        autoClutch = hardwareMap.servo.get("autoClutch");

        foundLeft = hardwareMap.servo.get("foundation2");
        foundRight = hardwareMap.servo.get("foundation");

        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);

        waitForStart();

        while (!isStopRequested()) {

            drive = -gamepad1.left_stick_y - (gamepad2.left_stick_y/2);
            turn = gamepad1.right_stick_x + (gamepad2.right_stick_x/2) + (gamepad1.left_stick_x/3);

            left = drive - turn;
            right = drive + turn;

            max = Math.max(Math.abs(left), Math.abs(right)); // sets max to the greatest motor power
            if (max > 1.0) {
                left /= max;
                right /= max;
            }

            /*SET UP SLIDES*/
            up = (gamepad1.right_trigger + gamepad2.right_trigger);
            down = (gamepad1.left_trigger + gamepad2.left_trigger);
            up = Range.clip(up, 0.0, 0.6);
            down = Range.clip(down, 0.0, 0.6);

            if(up > 0.1) {
                slides.setPower(up);
            }
            else if(down > 0.1) {
                slides.setPower(-down); // the input values are positive so need to add negative
            }
            else {
                slides.setPower(0);
            }

            leftFront.setPower(left);
            leftBack.setPower(left);
            rightFront.setPower(right);
            rightBack.setPower(right);

            if(gamepad1.right_bumper || gamepad2.right_bumper) {
                clutch.setPosition(0.2); // position clutch to collect stone
            }
            else if(gamepad1.left_bumper || gamepad2.left_bumper) {
                clutch.setPosition(0.7);// position clutch to deliver stone
            }

            if(gamepad1.y || gamepad2.y) {
                autoClutch.setPosition(1);
            }
            if(gamepad1.x || gamepad2.x) {
                autoClutch.setPosition(0.4);
            }

            //brings both clutches up
           if(gamepad1.dpad_up|| gamepad2.dpad_up){
              foundLeft.setPosition(0.15);
              foundRight.setPosition(1);
            }
           if(gamepad2.dpad_left)
               foundRight.setPosition(0.6);

           if(gamepad2.dpad_right)
               foundLeft.setPosition(0.4);

           //brings both clutches down
          if (gamepad1.dpad_down|| gamepad2.dpad_down){
              foundLeft.setPosition(0.2);
              foundRight.setPosition(0.5);
            }
          if(gamepad1.dpad_right||gamepad2.dpad_right){
              foundLeft.setPosition(1);
              foundRight.setPosition(0.3);
          }

          if(gamepad2.a) {
              foundLeft.setPosition(0.2);
          }
          if(gamepad2.b) {

          }
        }
    }
}