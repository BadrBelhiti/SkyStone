package org.firstinspires.ftc.teamcode.autonomous.instructions;

import org.firstinspires.ftc.teamcode.autonomous.RobotControl;

public class CardinalVectorInstruction extends Instruction {

    private Direction direction;
    private double distance;

    public CardinalVectorInstruction(RobotControl robotControl, Direction direction, double distance) {
        super(robotControl);
        this.direction = direction;
        this.distance = distance;
    }

    @Override
    public void start() {
        this.isActive = true;
    }

    private double currentDistance = 0;
    private long last = System.currentTimeMillis();

    @Override
    public void run() {
        last = System.currentTimeMillis();
        if (currentDistance > distance){
            stop();
            return;
        }
        switch (direction){
            case FORWARD: robotControl.strafe(0, 0.5);
            break;
            case LEFT: robotControl.strafe(-0.5, 0);
            break;
            case RIGHT: robotControl.strafe(0.5, 0);
            break;
        }
        currentDistance+= direction.getConstant() * (System.currentTimeMillis() - last) / 1000.0  * getBatteryVoltage(robotControl.getHardwareMap());
    }

    @Override
    public void stop() {
        this.isActive = false;
        robotControl.strafe(0, 0);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }
}
