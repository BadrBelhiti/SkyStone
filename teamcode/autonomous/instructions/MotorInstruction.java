package org.firstinspires.ftc.teamcode.autonomous.instructions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MotorInstruction extends Instruction {

    private DcMotor motor;
    private ElapsedTime runtime;
    private int timeMillis;
    private double power;
    private boolean isActive;

    public MotorInstruction(DcMotor motor, int timeMillis, double power){
        this.motor = motor;
        this.runtime = new ElapsedTime();
        this.timeMillis = timeMillis;
        this.power = power;
    }

    public void start(){
        this.isActive = true;
        runtime.reset();
    }

    public void run(){
        if (runtime.milliseconds() >= timeMillis){
            stop();
        } else {
            motor.setPower(power);
        }
    }

    public void stop(){
        this.isActive = false;
        motor.setPower(0);
    }

    public DcMotor getMotor() {
        return motor;
    }

    public int getTimeMillis() {
        return timeMillis;
    }

    public double getPower() {
        return power;
    }

    public boolean isActive() {
        return isActive;
    }
}
