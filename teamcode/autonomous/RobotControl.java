package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.instructions.*;

import java.util.HashMap;
import java.util.LinkedList;

public class RobotControl {

    public static final int TANK_DRIVE_CHAIN = 0;
    public static final int OMNI_CHAIN = 1;

    private final double LIFT_POWER = 0.5;
    private final double CLAMP_POWER = 0.3;
    private final double TURBO = 1;
    private final double NORMAL = 0.6;

    private final int BUTTON_COOLDOWN = 1000;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime buttonCooldown = new ElapsedTime();
    private ElapsedTime servoCooldown = new ElapsedTime();
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor clamp;
    private DcMotor lift;
    private Servo hook; // straight down =  1.0/3
    private OpMode opMode;
    private Gamepad gamepad;
    private Telemetry telemetry;
    private boolean autonomous;
    private boolean turbo = false;
    private double hookPosition = 0;

    private HashMap<Integer, LinkedList<Instruction>> instructionQueue = new HashMap<>();

    public RobotControl(OpMode opMode, boolean autonomous){
        this.opMode = opMode;
        this.gamepad = opMode.gamepad1;
        this.autonomous = autonomous;
        this.telemetry = opMode.telemetry;


        this.lift = opMode.hardwareMap.get(DcMotor.class, "lift");
        this.clamp = opMode.hardwareMap.get(DcMotor.class, "clamp");
        this.hook = opMode.hardwareMap.get(Servo.class, "hook");

        this.frontRight  = opMode.hardwareMap.get(DcMotor.class, "frontRight");
        this.backRight = opMode.hardwareMap.get(DcMotor.class, "backRight");
        this.backLeft = opMode.hardwareMap.get(DcMotor.class, "backLeft");
        this.frontLeft = opMode.hardwareMap.get(DcMotor.class, "frontLeft");

        //frontRight.setDirection(DcMotor.Direction.FORWARD);
        //backRight.setDirection(DcMotor.Direction.REVERSE);
        //backLeft.setDirection(DcMotor.Direction.REVERSE);
        //frontLeft.setDirection(DcMotor.Direction.REVERSE); // Double reverse because of wiring issue

        runtime.reset();
    }

    public void run(){
        if (autonomous){
            for (LinkedList<Instruction> chain : instructionQueue.values()) {
                if (!chain.isEmpty()) {
                    Instruction first = chain.getFirst();
                    if (first.isActive()) {
                        first.run();
                    } else {
                        chain.removeFirst();
                        if (!chain.isEmpty()) {
                            chain.getFirst().start();
                        }
                    }
                }
            }
        } else {
            runTeleOp();
        }
    }

    public void addInstruction(Instruction instruction, int chain){
        LinkedList<Instruction> queue = instructionQueue.get(chain);
        if (queue == null){
            queue = new LinkedList<>();
        }
        queue.addLast(instruction);
        instructionQueue.put(chain, queue);

        if (queue.getFirst().equals(instruction)) {
            instruction.start();
        }
    }

    public void addDriveInstruction(double leftPower, int leftTime, double rightPower, int rightTime){
        TankDriveInstruction tankDriveInstruction = new TankDriveInstruction(this, leftPower, leftTime, rightPower, rightTime);
        addInstruction(tankDriveInstruction, TANK_DRIVE_CHAIN);
    }

    public void addVectorDriveInstruction(double x, double y, int time){
        VectorDriveInstruction vectorDriveInstruction = new VectorDriveInstruction(this, x, y, time);
        addInstruction(vectorDriveInstruction, OMNI_CHAIN);
    }

    public void pause(int chain, int time){
        PauseInstruction pauseInstruction = new PauseInstruction(time);
        addInstruction(pauseInstruction, chain);
    }

    public void addCardinalVectcorDriveInstruction(Direction direction, double distance){
        CardinalVectorInstruction cardinalVectorInstruction = new CardinalVectorInstruction(this, direction, distance);
        addInstruction(cardinalVectorInstruction, OMNI_CHAIN);
    }

    private void runTeleOp(){
        double lfSpeed = gamepad.left_stick_y - gamepad.left_stick_x;
        double lbSpeed = gamepad.left_stick_y + gamepad.left_stick_x;
        double rfSpeed = gamepad.right_stick_y + gamepad.left_stick_x;
        double rbSpeed = gamepad.right_stick_y - gamepad.left_stick_x;

        lfSpeed = Range.clip(lfSpeed, -1, 1);
        lbSpeed = Range.clip(lbSpeed, -1, 1);
        rfSpeed = Range.clip(rfSpeed, -1, 1);
        rbSpeed = Range.clip(rbSpeed, -1, 1);

        frontRight.setPower(-rfSpeed);
        frontLeft.setPower(lfSpeed);
        backRight.setPower(rbSpeed);
        backLeft.setPower(lbSpeed);

        if (lift != null) {
            if (gamepad.dpad_up || gamepad.dpad_down) {
                lift.setPower(gamepad.dpad_down ? -LIFT_POWER : LIFT_POWER);
            } else {
                lift.setPower(0);
            }
        }

        if (clamp != null) {
            if (gamepad.dpad_right || gamepad.dpad_left) {
                clamp.setPower(gamepad.dpad_left ? -CLAMP_POWER : CLAMP_POWER);
            } else {
                clamp.setPower(0);
            }
        }

        if (listen(gamepad.x)){
            turbo = !turbo;
        }

        // Hook
        if (servoCooldown.milliseconds() > 100 && (gamepad.y || gamepad.a)) {

            if (gamepad.y) {
                // Increase position with an upper bound of 1
                hookPosition = Math.min(1, hookPosition + 0.2);
            } else {
                // Decrease position with a lower bound of 0
                hookPosition = Math.max(0, hookPosition - 0.2);
            }
            hook.setPosition(hookPosition);
            servoCooldown.reset();
        }

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Y", gamepad.y);
        telemetry.addData("A", gamepad.a);
        telemetry.update();
    }

    public void leftDrive(double power){
        backLeft.setPower(power);
    }

    public void rightDrive(double power){
        frontRight.setPower(power);
    }

    public void strafe(double x, double y){
        /*
        verticalDrive(y);
        horizontalDrive(x);
        */

        double lfSpeed = y - x;
        double lbSpeed = y + x;
        double rfSpeed = y + x;
        double rbSpeed = y - x;

        lfSpeed = Range.clip(lfSpeed, -1, 1);
        lbSpeed = Range.clip(lbSpeed, -1, 1);
        rfSpeed = Range.clip(rfSpeed, -1, 1);
        rbSpeed = Range.clip(rbSpeed, -1, 1);

        frontRight.setPower(-rfSpeed);
        frontLeft.setPower(lfSpeed);
        backRight.setPower(rbSpeed);
        backLeft.setPower(lbSpeed);
    }

    public void verticalDrive(double power){
        frontRight.setPower(power);
        frontLeft.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void horizontalDrive(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);

    }

    public DcMotor getFrontRight() {
        return frontRight;
    }

    public DcMotor getBackRight() {
        return backRight;
    }

    public DcMotor getBackLeft() {
        return backLeft;
    }

    public DcMotor getFrontLeft() {
        return frontLeft;
    }

    public DcMotor getClamp() {
        return clamp;
    }

    public DcMotor getLift() {
        return lift;
    }

    public HardwareMap getHardwareMap(){
        return opMode.hardwareMap;
    }

    public boolean listen(boolean button){
        if (buttonCooldown.milliseconds() < BUTTON_COOLDOWN){
            return false;
        }
        if (button){
            buttonCooldown.reset();
        }
        return button;
    }

}
