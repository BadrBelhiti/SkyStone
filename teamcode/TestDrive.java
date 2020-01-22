package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.firstinspires.ftc.teamcode.autonomous.RobotControl;

@TeleOp(name="Test Drive", group="Linear Opmode")
//@Disabled
public class TestDrive extends LinearOpMode {

    private static final double K = 0.27;

    private RobotControl robotControl;

    public void runOpMode() {
        this.robotControl = new RobotControl(this, false);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (robotControl.listen(gamepad1.left_trigger > 0)){
                double k = 0;
                double acc = 0;
                long last = System.currentTimeMillis();
                while (gamepad1.left_trigger > 0){
                    robotControl.horizontalDrive(-0.50);
                    acc+= (System.currentTimeMillis() - last) / 1000.0 * getBatteryVoltage();
                    last = System.currentTimeMillis();
                }
                robotControl.horizontalDrive(0);
                k = 1.0 / acc;
                telemetry.addData("Constant %.5f", k);
                telemetry.update();
            }

            if (robotControl.listen(gamepad1.right_trigger > 0)){
                double k = 0;
                double acc = 0;
                long last = System.currentTimeMillis();
                while (gamepad1.right_trigger > 0){
                    robotControl.horizontalDrive(0.50);
                    acc+= (System.currentTimeMillis() - last) / 1000.0 * getBatteryVoltage();
                    last = System.currentTimeMillis();
                }
                robotControl.horizontalDrive(0);
                k = 1.0 / acc;
                telemetry.addData("Constant %.5f", k);
                telemetry.update();
            }

            if (robotControl.listen(gamepad1.left_bumper)){
                double k = 0;
                double acc = 0;
                long last = System.currentTimeMillis();
                while (gamepad1.left_bumper){
                    robotControl.verticalDrive(-0.50

                    );
                    acc+= (System.currentTimeMillis() - last) / 1000.0 * getBatteryVoltage();
                    last = System.currentTimeMillis();
                }
                robotControl.verticalDrive(0);
                k = 1.0 / acc;
                telemetry.addData("Constant %.5f", k);
                telemetry.update();
            }

            if (robotControl.listen(gamepad1.right_bumper)){
                double k = 0;
                double acc = 0;
                long last = System.currentTimeMillis();
                while (gamepad1.right_bumper){
                    robotControl.verticalDrive(0.75);
                    acc+= (System.currentTimeMillis() - last) / 1000.0 * getBatteryVoltage();
                    last = System.currentTimeMillis();
                }
                robotControl.verticalDrive(0);
                k = 1.0 / acc;
                telemetry.addData("Constant %.5f", k);
                telemetry.update();
            }

            /*
            if (robotControl.listen(gamepad1.a)) {
                double distance = 0;
                long last = System.currentTimeMillis();
                while (distance < 1) {
                    robotControl.verticalDrive(0.5);
                    distance += K * (System.currentTimeMillis() - last) / 1000.0 * getBatteryVoltage();
                    last = System.currentTimeMillis();
                }
                robotControl.verticalDrive(0);
            }
            */
        }
    }

    double getBatteryVoltage() {
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
