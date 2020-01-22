package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Utils {

    private static final double MIN = 0.0001;

    public static boolean isZero(double value){
        return Math.abs(value) < MIN;
    }

    public static boolean leftStickInUse(Gamepad gamepad){
        return !isZero(gamepad.left_stick_x) || !isZero(gamepad.left_stick_y);
    }

    public static boolean rightStickInUse(Gamepad gamepad){
        return !isZero(gamepad.right_stick_x) || !isZero(gamepad.right_stick_y);
    }

}
