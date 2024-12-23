package cleaner.our_cleaner;

import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Simplified mimic implementation of {@link  java.lang.ref.Cleaner},
 * you can replace {@link java.lang.ref.Cleaner} with this class in all our examples.
 * <br>
 * {@link java.lang.ref.Cleaner.Cleanable} should also be replaced with {@link OurCleanable}
 **/
public class OurCleaner implements Runnable {
    /*
     * The real linked list used by Cleaner is not of this type, LinkedList was used
     * to make this implementation as simple as possible
     * */
    final LinkedList<OurPhantomCleanable> list;
    final ReferenceQueue<Object> queue;

    public static OurCleaner create() {
        OurCleaner cleaner = new OurCleaner();
/*
        This makes sure the linked list is not empty when the thread starts,
        and the thread will run at least until the cleaner itself is reclaimable by GC.
        You can remove the line of code and see what happens
*/
        new OurPhantomCleanable(cleaner, cleaner, () -> {
        });

        Thread thread = new Thread(cleaner);
        thread.setDaemon(true);
        thread.start();

        return cleaner;
    }

    public OurCleanable register(Object resourceHolder, Runnable cleaningAction) {
        Objects.requireNonNull(resourceHolder, "resourceHolder");
        Objects.requireNonNull(cleaningAction, "cleaningAction");
        return new OurPhantomCleanable(resourceHolder, this, cleaningAction);
    }

    private OurCleaner() {
        list = new LinkedList<>();
        queue = new ReferenceQueue<>();
    }

    @Override
    public void run() {
        while (!list.isEmpty()) {
            try {
//                You can replace remove() with poll() and observe their difference
                OurPhantomCleanable removed = (OurPhantomCleanable) queue.remove();
                removed.clean();
            } catch (Throwable ignored) {
//                Ignore Exception from clean up action
            }
        }
    }

    public interface OurCleanable {
        void clean();
    }
}
