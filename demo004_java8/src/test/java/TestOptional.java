import org.junit.jupiter.api.Test;

import java.util.Optional;


/**
 * create at     2019/9/2 6:57 下午
 *
 * @author zing
 * @version 0.0.1
 */
public class TestOptional {
    @Test
    public void testOptional() {
        Optional<String> demo = Optional.empty();
        demo.ifPresent(System.out::println);
        System.out.println(demo.orElseGet(() -> "N"));

        demo = Optional.ofNullable(null);
        System.out.println(demo.orElseGet(() -> "H"));

        demo = Optional.of("M");
        System.out.println(demo.orElseGet(() -> "P"));


    }
}
