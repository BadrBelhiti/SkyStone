package org.firstinspires.ftc.teamcode.autonomous.instructions;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.autonomous.RobotControl;

public class TankDriveInstruction extends Instruction {

    private ElapsedTime runtime;
    private double leftPower, rightPower;
    private int leftTimeMillis, rightTimeMillis;
    private boolean isActive;

    public TankDriveInstruction(RobotControl robotControl, double leftPower, int leftTimeMillis, double rightPower, int rightTimeMillis){
        super(robotControl);
        this.runtime = new ElapsedTime();
        this.leftPower = leftPower;
        this.leftTimeMillis = leftTimeMillis;
        this.rightPower = rightPower;
        this.rightTimeMillis = rightTimeMillis;
    }

    @Override
    public void start() {
        this.isActive = true;
        runtime.reset();
    }

    @Override
    public void run() {
        if (runtime.milliseconds() >= Math.max(leftTimeMillis, rightTimeMillis)){
            stop();
        } else {
            if (runtime.milliseconds() < leftTimeMillis){
                robotControl.leftDrive(leftPower);
            }
            if (runtime.milliseconds() < rightTimeMillis){
                robotControl.rightDrive(rightPower);
            }
        }
    }

    @Override
    public void stop() {
        this.isActive = false;
        robotControl.leftDrive(0);
        robotControl.rightDrive(0);
    }

    public boolean isActive(){
        return isActive;
    }
}
