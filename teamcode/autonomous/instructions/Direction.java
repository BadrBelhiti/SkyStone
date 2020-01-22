package org.firstinspires.ftc.teamcode.autonomous.instructions;

public enum Direction {

    FORWARD(0.2767 * 7), RIGHT(0.2286 * 7), LEFT(0.1846 * 7);

    private double constant;

    Direction(double constant){
        this.constant = constant;
    }

    public double getConstant() {
        return constant;
    }
}
