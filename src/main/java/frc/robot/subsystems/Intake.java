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

public class Intake extends SubsystemBase {
   private final SparkMax intakeMotor = new SparkMax(Constants.IdConstants.INTAKE_MOTOR_ID,MotorType.kBrushed);
  /** Creates a new CoralSubsystem. */
  public Intake() {
    // Register with JSim (safe no-op if JSim isn't present)
    JSimIntegration.registerSparkMax("intakeMotor", intakeMotor);
  }
       public Command intakeForwardCommand(){
    return runEnd(()->{
      intakeMotor.set(Constants.SpeedConstants.INTAKE_FORWARD_SPEED);
    },() -> {
      intakeMotor.stopMotor();
    });
  }

  //  public Command intakeSlowForwardCommand(){
  //   return runEnd(()->{
  //     intakeMotor.set(Constants.SpeedConstants.INTAKE_SLOW_FORWARD_SPEED);
  //   },() -> {
  //     intakeMotor.stopMotor();
  //   });
  // }

  public Command intakeBackwardCommand(){
    return runEnd(()->{
      intakeMotor.set(Constants.SpeedConstants.INTAKE_BACKWARD_SPEED);
    },() -> {
      intakeMotor.stopMotor();
    });
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
