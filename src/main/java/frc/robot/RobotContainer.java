package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.ElevatorFella;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CoralEater9000;
import frc.robot.Commands.CoralNomNom;
import frc.robot.Commands.MoveElevator;
import frc.robot.Commands.CoralIntestine;
import frc.robot.Commands.CoralColon;
import frc.robot.Commands.CoralDigestion;
import frc.robot.subsystems.CoralPooper;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);
    private final CommandXboxController driverController = new CommandXboxController(0); // ✅ Using only one controller now
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    // Subsystems
    private final ElevatorFella elevatorSubsystem = new ElevatorFella();
    private final CoralEater9000 coralEater = new CoralEater9000();
    private final CoralPooper coralPooper = new CoralPooper();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverController.getLeftY() * MaxSpeed)
                    .withVelocityY(-driverController.getLeftX() * MaxSpeed)
                    .withRotationalRate(-driverController.getRightX() * MaxAngularRate)
            )
        );

        driverController.x().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.y().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));

        driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        drivetrain.registerTelemetry(logger::telemeterize);



        //CONTROLS
        driverController.y().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));


        // Elevator Controls
        driverController.rightBumper().whileTrue(new MoveElevator(elevatorSubsystem, -0.1)); // Vader Up
        driverController.leftBumper().whileTrue(new MoveElevator(elevatorSubsystem, 0.1)); // Vader Down

   
        // Coral Arm
        driverController.povUp().whileTrue(new CoralDigestion(coralEater, 0.5)); // Arm up
        driverController.povDown().whileTrue(new CoralDigestion(coralEater, -0.5)); // Arm down
    
  

        // Spin Intake Wheels
        driverController.b().whileTrue(new CoralNomNom(coralEater, 0.3)); // Coral go out
        driverController.b().onTrue(Commands.print("Coral go out"));
        driverController.a().whileTrue(new CoralNomNom(coralEater, -0.3)); // Coral go in
        driverController.a().onTrue(Commands.print("Coral go in"));

        // Coral Scoring System
        driverController.leftTrigger().whileTrue(new CoralIntestine(coralPooper, -0.2  )); // Coral Intestine (Removes coral from intake)
        driverController.povLeft().whileTrue(new CoralColon(coralPooper, 0.1)); // Coral Colon (Moves it towards intake)
        driverController.povRight().whileTrue(new CoralColon(coralPooper, -0.1)); // Coral Colon (Moves coral out of robot)
    }   


    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
