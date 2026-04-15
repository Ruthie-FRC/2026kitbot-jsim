package frc.robot.sim;

import edu.wpi.first.wpilibj.RobotBase;
import com.revrobotics.spark.SparkMax;

/**
 * Lightweight, reflection-based integration point for JSim.
 *
 * This class purposefully avoids a compile-time dependency on JSim by using
 * Class.forName / reflection. If JSim is present on the classpath at runtime
 * (provided by vendordeps), it will try to call common initialization points
 * and register SparkMax devices. If JSim is not present, all calls are no-ops.
 */
public final class JSimIntegration {
  private static boolean jSimAvailable = false;
  private JSimIntegration() {}

  /** Attempt to initialize JSim. Safe to call multiple times. */
  public static void init() {
    if (!RobotBase.isSimulation()) {
      return;
    }

    if (jSimAvailable) {
      return;
    }

    try {
      // Try a few likely entry points; vendors may differ in naming.
      Class<?> cls;
      try {
        cls = Class.forName("jsim.JSim");
      } catch (ClassNotFoundException e) {
        cls = Class.forName("jsim.api.JSim");
      }

      try {
        // Common static init method name used by many libs
        cls.getMethod("init").invoke(null);
      } catch (NoSuchMethodException e) {
        try {
          cls.getMethod("initialize").invoke(null);
        } catch (NoSuchMethodException ex) {
          // If no init methods exist, that's fine — JSim may not need explicit init.
        }
      }

      jSimAvailable = true;
      System.out.println("[JSimIntegration] JSim detected and (attempted) initialization.");
    } catch (ClassNotFoundException e) {
      // JSim not present on classpath — nothing to do.
      System.out.println("[JSimIntegration] JSim not found on classpath; continuing without JSim integration.");
    } catch (Exception e) {
      System.out.println("[JSimIntegration] Unexpected error while initializing JSim: " + e);
      // don't rethrow — fail gracefully
    }
  }

  /**
   * Register a SparkMax with JSim if possible. This method will silently return
   * if JSim isn't available or the expected registration API isn't found.
   */
  public static void registerSparkMax(String name, SparkMax spark) {
    if (!RobotBase.isSimulation()) {
      return;
    }

    // Ensure we attempted init first
    if (!jSimAvailable) {
      init();
      if (!jSimAvailable) {
        return;
      }
    }

    try {
      // Many simulation libs expose a registration API; attempt to call a reasonable name.
      // We use reflection so compilation does not require JSim on the classpath.
      Class<?> apiClass = Class.forName("jsim.api.JSimAPI");
      try {
        java.lang.reflect.Method m = apiClass.getMethod("registerSparkMax", String.class, SparkMax.class);
        m.invoke(null, name, spark);
        System.out.println("[JSimIntegration] Registered SparkMax with JSim: " + name);
        return;
      } catch (NoSuchMethodException ignored) {}

      try {
        java.lang.reflect.Method m2 = apiClass.getMethod("registerMotorController", String.class, Object.class);
        m2.invoke(null, name, spark);
        System.out.println("[JSimIntegration] Registered motor controller with JSim: " + name);
        return;
      } catch (NoSuchMethodException ignored) {}

      // If the apiClass exists but no known register method, fallthrough silently.
    } catch (ClassNotFoundException e) {
      // JSim API class not found — nothing to do.
    } catch (Exception e) {
      System.out.println("[JSimIntegration] Error while registering SparkMax: " + e);
    }
  }

  /** Periodic update hook; kept for future expansion. */
  public static void periodic() {
    // No-op for now. If JSim needs tick calls, add them here via reflection.
  }
}
