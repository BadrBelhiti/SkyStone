package org.firstinspires.ftc.teamcode.autonomous.instructions;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.autonomous.RobotControl;

public class VectorDriveInstruction extends Instruction {

    private ElapsedTime runtime;
    private double x;
    private double y;
    private int timeMillis;

    public VectorDriveInstruction(RobotControl robotControl, double x, double y, int timeMillis){
        super(robotControl);
        this.robotControl = robotControl;
        this.runtime = new ElapsedTime();
        this.x = x;
        this.y = y;
        this.timeMillis = timeMillis;
    }

    @Override
    public void start() {
        this.isActive = true;
        runtime.reset();
    }

    @Override
    public void run() {
        if (runtime.milliseconds() >= timeMillis){
            stop();
        } else {
            robotControl.strafe(x, y);
        }
    }

    @Override
    public void stop() {
        this.isActive = false;
        robotControl.strafe(0, 0);
    }

    public boolean isActive(){
        return isActive;
    }
}
