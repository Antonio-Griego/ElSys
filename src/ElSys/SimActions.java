package ElSys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.*;

public class SimActions
{
  private final ArrayList<SimButton> simButtons;

  private Future scheduleFuture;

  private final Random random;

  SimActions(final long seed, final Collection<SimButton> buttons)
  {
    simButtons = new ArrayList<>(buttons);
    random = new Random(seed);
  }

  void beginRandomActions()
  {
    final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    scheduleFuture = executor.schedule(this::executeRandomActions, 1, TimeUnit.SECONDS);
  }

  void stopRandomActions()
  {
    scheduleFuture.cancel(true);
  }

  private void executeRandomActions()
  {
    final int idx = random.nextInt() % (simButtons.size() - 1);

    final SimButton sb = simButtons.get(idx);

    if (sb == null) throw new RuntimeException("Random choice error.");

    sb.setLight(true);
  }
}
