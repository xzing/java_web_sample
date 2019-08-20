package com.zing.rx.d001_event;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * create at     2019-08-20 15:43
 *
 * @author zing
 * @version 0.0.1
 */
@Slf4j
public class EventBusTest {
    @Subscriber(topic = "QAQ")
    public void subscriber1(String msg) {
        log.info("subscriber1 message:{}", msg);
    }

    @Subscriber(topic = "QAQ", order = 3)
    public void subscriber2(String msg) {
        log.info("subscriber2 message:{}", msg);

    }

    @Subscriber(topic = "QAQ", order = 2)
    public void subscriber3(String msg) {
        log.info("subscriber3 message:{}", msg);

    }

    @Subscriber(topic = "QAQ")
    public void subscriber3(Integer msg) {
        log.info("subscriber4 message:{}", msg);

    }

    @Test
    public void test() {
        EventBusTest test = new EventBusTest();
        EventBus.getInstance().register(test);
        EventBus.pushEvent("QAQ", 1);
        EventBus.pushEvent("QAQ", "sha");
    }
}
