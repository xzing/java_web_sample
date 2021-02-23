
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * create at     2019-08-13 17:58
 *
 * @author zing
 * @version 0.0.1
 */
public class ScheduleCancelTest {
    @Test
    public void testSca() throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
        scheduler.scheduleWithFixedDelay(() -> System.out.println(System.currentTimeMillis()), 0, 3, TimeUnit.SECONDS);

        Thread.sleep(20000);

        scheduler.shutdown();
        System.out.println("shutdown");

        Thread.sleep(2000);
        System.out.println("restart");

        scheduler.scheduleWithFixedDelay(() -> System.out.println(System.currentTimeMillis()), 0, 3, TimeUnit.SECONDS);


    }
}
