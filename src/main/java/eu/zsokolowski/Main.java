package eu.zsokolowski;

import eu.zsokolowski.completablefuturetrap.Demo;
import eu.zsokolowski.completablefuturetrap.TimeBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        var demo = new Demo();
        demo.startProcess(TimeBox.VERY_LONG_TIME);
        LOG.info("Processing ....");
        demo.stop();
    }
}
