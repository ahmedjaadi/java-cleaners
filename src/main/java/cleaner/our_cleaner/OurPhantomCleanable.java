package cleaner.our_cleaner;

import cleaner.our_cleaner.OurCleaner.OurCleanable;
import java.lang.ref.PhantomReference;

/**
 * Mimic implementation of {@link jdk.internal.ref.PhantomCleanable}
 */
final class OurPhantomCleanable extends PhantomReference<Object> implements OurCleanable {
    private final Runnable cleaningAction;
    private final OurCleaner cleaner;

    OurPhantomCleanable(Object referent, OurCleaner cleaner, Runnable cleaningAction) {
        super(referent, cleaner.queue);
        this.cleaner = cleaner;
        this.cleaningAction = cleaningAction;
        cleaner.list.add(this);
    }

    @Override
    public void clean() {
//        Only run if OurPhantomCleanable is on the list
        if (cleaner.list.contains(this)) {
//            Remove it from the list, so it won't be called anymore
            cleaner.list.remove(this);
            cleaningAction.run();
        } else System.out.println("Cleaning action already run");
    }
}

