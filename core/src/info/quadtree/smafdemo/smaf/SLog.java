package info.quadtree.smafdemo.smaf;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SLog {
    public enum Level {
        Debug,
        Info,
        Warn,
        Error
    }

    public static BiConsumer<Level, String> logCallback = null;

    public static void debug(Supplier<String> msg){
        if (logCallback != null){
            logCallback.accept(Level.Debug, msg.get());
        }
    }

    public static void info(Supplier<String> msg){
        if (logCallback != null){
            logCallback.accept(Level.Info, msg.get());
        }
    }

    public static void warn(Supplier<String> msg){
        if (logCallback != null){
            logCallback.accept(Level.Warn, msg.get());
        }
    }

    public static void error(Supplier<String> msg){
        if (logCallback != null){
            logCallback.accept(Level.Error, msg.get());
        }
    }
}
