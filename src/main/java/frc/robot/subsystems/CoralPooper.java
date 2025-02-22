package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class CoralPooper extends SubsystemBase {
    private final SparkMax coralIntestine; // Takes coral out of intake
    private final SparkMax coralColon; // Scores coral from the elevator

    public CoralPooper() {
        coralIntestine = new SparkMax(24, MotorType.kBrushless); // Replace with actual CAN ID
        coralColon = new SparkMax(25, MotorType.kBrushless); // Replace with actual CAN ID

     
    }

    /** Spins the Coral Intestine wheel */
    public void setIntestineSpeed(double speed) {
        coralIntestine.set(speed);
    }

    /** Spins the Coral Colon wheel */
    public void setColonSpeed(double speed) {
        coralColon.set(speed);
    }

    /** Stops both outtake wheels */
    public void stop() {
        coralIntestine.set(0);
        coralColon.set(0);
    }
}
