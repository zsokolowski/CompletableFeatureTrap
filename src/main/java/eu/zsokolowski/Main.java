package eu.zsokolowski;

import eu.zsokolowski.completablefuturetrap.CompletableFutureDemo;
import eu.zsokolowski.completablefuturetrap.Demo;
import eu.zsokolowski.completablefuturetrap.FutureDemo;
import eu.zsokolowski.completablefuturetrap.TimeBox;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int HELP_WIDTH = 120;
    private static final String FUTURE_TYPE_OPT = "future_type";

    public static void main(String[] args) throws ParseException, URISyntaxException {
        var options = new Options();
        options.addRequiredOption("ft", FUTURE_TYPE_OPT, true, "[completable|future]");
        CommandLineParser cliParser = new DefaultParser();
        final CommandLine cmd = cliParser.parse(options, args);
        final String future_type = cmd.getOptionValue(options.getOption(FUTURE_TYPE_OPT));
        Demo demo = null;
        if (future_type.equals("completable")) {
            demo = new CompletableFutureDemo();
        } else if (future_type.equals("future")) {
            demo = new FutureDemo();
        } else {
            printHelp(options);
            System.exit(1);
        }
        demo.startProcess(TimeBox.VERY_LONG_TIME);
        LOG.info("Processing ....");
        demo.stop();
    }

    public static void printHelp(Options options) throws URISyntaxException {
        var formatter = new HelpFormatter();
        final var currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        formatter.printHelp(HELP_WIDTH, currentJar.getName(), "Command line options", options, "");
    }
}
