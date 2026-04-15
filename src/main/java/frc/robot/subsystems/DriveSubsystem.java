// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import java.util.function.DoubleSupplier;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.sim.JSimIntegration;

public class DriveSubsystem extends SubsystemBase {
  
  private final SparkMax leftFrontMotor = new SparkMax(Constants.IdConstants.LEFT_FRONT_MOTOR_ID,MotorType.kBrushed);
  private final SparkMax rightFrontMotor = new SparkMax(Constants.IdConstants.RIGHT_FRONT_MOTOR_ID,MotorType.kBrushed);
  private final SparkMax leftBackMotor = new SparkMax(Constants.IdConstants.LEFT_BACK_MOTOR_ID,MotorType.kBrushed);
  private final SparkMax rightBackMotor = new SparkMax(Constants.IdConstants.RIGHT_BACK_MOTOR_ID,MotorType.kBrushed);
  private final DifferentialDrive differentialDrive;
  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    differentialDrive = new DifferentialDrive(rightFrontMotor, leftFrontMotor);
    // Register motors with JSim (no-op if JSim isn't present)
    JSimIntegration.registerSparkMax("leftFrontMotor", leftFrontMotor);
    JSimIntegration.registerSparkMax("rightFrontMotor", rightFrontMotor);
    JSimIntegration.registerSparkMax("leftBackMotor", leftBackMotor);
    JSimIntegration.registerSparkMax("rightBackMotor", rightBackMotor);
  }
  
  public Command driveCommand(DoubleSupplier speed, DoubleSupplier rotation){
    return runEnd(()->{
      differentialDrive.arcadeDrive(speed.getAsDouble(), rotation.getAsDouble());
    },() -> {
      differentialDrive.stopMotor();
    });
  } 
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

 public Command exampleMethodCommand() {
//
    throw new UnsupportedOperationException("Unimplemented method 'exampleMethodCommand'");
 }
}
