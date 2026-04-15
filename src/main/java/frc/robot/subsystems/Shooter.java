// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.sim.JSimIntegration;

public class Shooter extends SubsystemBase {
   private final SparkMax shooterMotor = new SparkMax(Constants.IdConstants.SHOOTER_MOTOR_ID,MotorType.kBrushed);
  /** Creates a new CoralSubsystem. */
  public Shooter() {
    // Register with JSim (safe no-op if JSim isn't present)
    JSimIntegration.registerSparkMax("shooterMotor", shooterMotor);
  }

         public Command shooterCornerForwardCommand(){
    return runEnd(()->{
      shooterMotor.set(Constants.SpeedConstants.SHOOTER_TRENCH_FORWARD_SPEED);
    },() -> {
      shooterMotor.stopMotor();
    });
  }

  public Command shooterCornerhBackwardCommand(){
    return runEnd(()->{
      shooterMotor.set(Constants.SpeedConstants.SHOOTER_TRENCH_BACKWARD_SPEED);
    },() -> {
      shooterMotor.stopMotor();
    });
  }

         public Command shooterForwardCommand(){
    return runEnd(()->{
      shooterMotor.set(Constants.SpeedConstants.SHOOTER_NORMAL_FORWARD_SPEED);
    },() -> {
      shooterMotor.stopMotor();
    });
  }

  public Command shooterBackwardCommand(){
    return runEnd(()->{
      shooterMotor.set(Constants.SpeedConstants.SHOOTER_NORMAL_BACKWARD_SPEED);
    },() -> {
      shooterMotor.stopMotor();
    });
  }

       public Command shooterTrenchForwardCommand(){
    return runEnd(()->{
      shooterMotor.set(Constants.SpeedConstants.SHOOTER_TRENCH_FORWARD_SPEED);
    },() -> {
      shooterMotor.stopMotor();
    });
  }

  public Command shooterTrenchBackwardCommand(){
    return runEnd(()->{
      shooterMotor.set(Constants.SpeedConstants.SHOOTER_TRENCH_BACKWARD_SPEED);
    },() -> {
      shooterMotor.stopMotor();
    });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
