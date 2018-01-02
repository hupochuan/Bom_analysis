package org.chasen.crfpp;

public class CRFPP {
  
  static {
    try {
      CRFPPLoader.load();
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }
  
  /**
   * Clean up temporary file generated by crfpp-java. General users do not need
   * to call this method, since the native library extracted in crfpp-java is
   * deleted upon JVM termination (via deleteOnExit()). This method is useful
   * when using a J2EE container, which will restart servlet containers multiple
   * times without restarting JVM.
   */
  public static void cleanUp() {
    CRFPPLoader.cleanUpExtractedNativeLib();
  }
  
  public static String getVersion() {
    return CRFPPJNI.VERSION_get();
  }
  
}
