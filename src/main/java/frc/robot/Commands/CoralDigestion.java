package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralEater9000;

public class CoralDigestion extends Command {
    private final CoralEater9000 coralEater;
    private final double targetPosition;

    public CoralDigestion(CoralEater9000 coralEater, double targetPosition) {
        this.coralEater = coralEater;
        this.targetPosition = targetPosition;
        addRequirements(coralEater);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("CoralEater reached position: " + targetPosition);
        coralEater.stop();
    }
}
