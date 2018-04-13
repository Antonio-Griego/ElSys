package ElSys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.*;

public class SimActions
{
  private static final long SECONDS_BETWEEN_ACTIONS = 15;

  private final ArrayList<SimButton> simButtons;

  private Future scheduleFuture;

  private final Random random;



  public SimActions(final long seed, final Collection<SimButton> buttons)
  {
    simButtons = new ArrayList<>(buttons);
    random = new Random(seed);
  }

  public void beginRandomActions()
  {
    final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    scheduleFuture = executor.scheduleAtFixedRate(this::executeRandomAction, 0, SECONDS_BETWEEN_ACTIONS, TimeUnit.SECONDS);
  }

  public void stopRandomActions()
  {
    scheduleFuture.cancel(true);
  }

  private void executeRandomAction()
  {
    final int idx = Math.abs(random.nextInt()) % (simButtons.size() - 1);

    final SimButton sb = simButtons.get(idx);

    if (sb == null) throw new RuntimeException("Random choice error.");

    sb.setLight(true);
  }
}
