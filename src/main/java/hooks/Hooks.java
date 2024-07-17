package hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {
  private final Logger ltLogger = LogManager.getLogger(Hooks.class);

  // BEFORE HOOKS - Will be executed as per the Order - 1, 2, 3 ...

  // AFTER HOOKS - Will be executed as per the Order - 3, 2, 1 ...
}
