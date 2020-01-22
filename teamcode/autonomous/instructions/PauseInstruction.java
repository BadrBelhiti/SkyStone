package org.firstinspires.ftc.teamcode.autonomous.instructions;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PauseInstruction extends Instruction {

    private ElapsedTime runtime;
    private int timeMillis;
    private boolean isActive;

    public PauseInstruction(int timeMillis){
        this.runtime = new ElapsedTime();
        this.timeMillis = timeMillis;
    }

    public void start(){
        this.isActive = true;
        runtime.reset();
    }

    public void run(){
        if (runtime.milliseconds() >= timeMillis){
            stop();
        }
    }

    public void stop(){
        this.isActive = false;
    }

    public int getTimeMillis() {
        return timeMillis;
    }

    public boolean isActive() {
        return isActive;
    }
}
