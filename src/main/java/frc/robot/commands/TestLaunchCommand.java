/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SerializerSubsystem;

public class TestLaunchCommand extends CommandBase {

  private SerializerSubsystem serializer;
  private LauncherSubsystem launcher;

  public TestLaunchCommand(SerializerSubsystem serializer1, LauncherSubsystem launcher1) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.serializer = serializer1;
    this.launcher = launcher1;
    addRequirements(serializer);
    addRequirements(launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // checks if balls have not been moved forward
    if (!this.serializer.hasBeenMovedForward) {
      // moves belts forward
      this.serializer.moveBeltsForward();
      this.serializer.hasBeenMovedForward = true;
    }
    // starts launcher and serializer
    this.launcher.startLauncher();
    // checks if launcher Motor is up to speed
    if (this.launcher.launcherMotor.getActiveTrajectoryVelocity() > 7
        && this.launcher.launcherMotor2.getActiveTrajectoryVelocity() > 7) {
      // starts rollers and serializer
      this.serializer.runSerializer(-1);
      this.launcher.startRollers();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // stops rollers, launchers and serializer
    this.launcher.stopRollers();
    this.launcher.stopLauncher();
    this.serializer.stopSerializer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return this.serializer.ballCount != this.serializer.previousBallCount;
  }
}
