package ElSys;

import ElSys.Enums.ButtonLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.*;

public class SimActions
{
  private static final long SECONDS_BETWEEN_ACTIONS = 20;

  private final SimButton[][] simButtons;
  private final ArrayList<Button> up_buttons;
  private final ArrayList<Button> down_buttons;
  
  private Future scheduleFuture;

  private final Random random;

  public SimActions(final long seed, final SimButton[][] buttons, final Collection<Button> up_floor_buttons, final Collection<Button> down_floor_buttons)
  {
    simButtons = buttons;
    up_buttons = new ArrayList<>(up_floor_buttons);
    down_buttons = new ArrayList<>(down_floor_buttons);
    random = new Random(seed);
  }

  public void beginRandomActions()
  {
    final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

//    scheduleFuture = executor.scheduleAtFixedRate(this::executeRandomAction, 0, SECONDS_BETWEEN_ACTIONS, TimeUnit.SECONDS);
    executeIncreasingButtonPresses();
  }

  public void stopRandomActions()
  {
    scheduleFuture.cancel(true);
  }

  private void executeRandomAction()
  {
    final int idx = Math.abs(random.nextInt()) % (simButtons.length - 1);
    final int jdx = Math.abs(random.nextInt()) % (simButtons[idx].length - 1);

    final SimButton sb = simButtons[idx][jdx];

    if (sb == null) throw new RuntimeException("Random choice error.");

    sb.setLight(true);
  }

  private void executeIncreasingButtonPresses()
  {
    final int[] floors = {3, 6};

    Arrays.stream(floors)
            .forEach(idx -> up_buttons.get(idx).setLight(ButtonLight.ON));
  }
}
