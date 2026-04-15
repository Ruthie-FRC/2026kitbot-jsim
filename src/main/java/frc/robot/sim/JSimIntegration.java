package frc.robot.sim;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.wpi.first.wpilibj.RobotBase;
import com.revrobotics.spark.SparkMax;

// JSim concrete imports (available when vendordeps are downloaded)
import jsim.PhysicsWorld;
import jsim.PhysicsBody;
import jsim.jni.JSimJNI;
import jsim.Vec3;

/**
 * Concrete integration with JSim when vendordeps are present.
 *
 * This class initialises the JSim native library and creates a PhysicsWorld for
 * the simulation. It provides a very small mapping API to create physics bodies
 * for motors and steps the physics world during simulationPeriodic.
 *
 * Current behavior:
 * - Calls JSimJNI.forceLoad() and initialize()
 * - Creates a single PhysicsWorld instance and steps it each simulation tick
 * - registerSparkMax creates a trivial physics body stub for the motor (so it
 *   shows up in the physics world) but it does not yet model motor torque —
 *   that can be expanded later.
 */
public final class JSimIntegration {
  private static volatile boolean jSimAvailable = false;
  private static PhysicsWorld world = null;
  private static final Map<String, jsim.PhysicsBody> motorBodyMap = new ConcurrentHashMap<>();

  private JSimIntegration() {}

  public static void init() {
    if (!RobotBase.isSimulation()) {
      return;
    }
    if (jSimAvailable) {
      return;
    }

    try {
      // Try to force-load JNI library and initialize
      JSimJNI.forceLoad();
      int rc = JSimJNI.initialize();
      // initialize() may return a status code; we ignore for now but log
      System.out.println("[JSimIntegration] JSimJNI.initialize() returned " + rc);

      // Create a PhysicsWorld with 20ms step and enable some default behavior
      world = new PhysicsWorld(0.02, true);
      // default gravity (m/s^2) — downward on Z
      world.setGravity(new Vec3(0.0, 0.0, -9.81));

      jSimAvailable = true;
      System.out.println("[JSimIntegration] JSim initialized and PhysicsWorld created.");
    } catch (UnsatisfiedLinkError e) {
      System.out.println("[JSimIntegration] Native JSim library not available: " + e.getMessage());
    } catch (NoClassDefFoundError e) {
      System.out.println("[JSimIntegration] JSim Java classes not available on classpath: " + e.getMessage());
    } catch (Throwable t) {
      System.out.println("[JSimIntegration] Error initializing JSim: " + t);
    }
  }

  /**
   * Create a small physics body to represent a SparkMax-driven mechanism. This
   * currently creates a body and stores its id; it does not yet map motor output
   * to physics forces — that's a follow-up enhancement.
   */
  public static void registerSparkMax(String name, SparkMax spark) {
    if (!RobotBase.isSimulation() || !jSimAvailable || world == null) {
      return;
    }

    try {
  jsim.PhysicsBody body = world.createBody(1.0); // mass placeholder
  motorBodyMap.put(name, body);
  // place body at origin
  body.setPosition(0.0, 0.0, 0.0);
  body.setGravityEnabled(true);
  System.out.println("[JSimIntegration] Created physics body for " + name + " id=" + body.bodyIndex());
    } catch (Throwable t) {
      System.out.println("[JSimIntegration] Error creating physics body for " + name + ": " + t);
    }
  }

  public static void periodic() {
    if (!RobotBase.isSimulation() || !jSimAvailable || world == null) {
      return;
    }
    try {
      // Step the physics world once per call. If the simulator calls this at
      // a different rate, consider accumulating and stepping with an integer
      // number of steps.
      world.step();
    } catch (Throwable t) {
      System.out.println("[JSimIntegration] Error stepping physics world: " + t);
    }
  }
}
