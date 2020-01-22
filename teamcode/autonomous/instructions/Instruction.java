package org.firstinspires.ftc.teamcode.autonomous.instructions;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.firstinspires.ftc.teamcode.autonomous.RobotControl;

public abstract class Instruction {

    protected RobotControl robotControl;
    protected boolean isActive;

    protected Instruction(){}

    protected Instruction(RobotControl robotControl){
        this.robotControl = robotControl;
    }

    public abstract void start();

    public abstract void run();

    public abstract void stop();

    public abstract boolean isActive();

    protected double getBatteryVoltage(HardwareMap hardwareMap) {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

}
