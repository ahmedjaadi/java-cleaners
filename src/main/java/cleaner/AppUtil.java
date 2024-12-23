package cleaner;

import cleaner.our_cleaner.OurCleaner;
import java.lang.ref.Cleaner;

public final class AppUtil {
    private static Cleaner CLEANER = null;
    private static OurCleaner OUR_CLEANER = null;

    public static OurCleaner getOurCleaner() {
        if (OUR_CLEANER == null) {
            OUR_CLEANER = OurCleaner.create();
        }
        return OUR_CLEANER;
    }
    public static Cleaner getCleaner() {
        if (CLEANER == null) {
            CLEANER = Cleaner.create();
        }
        return CLEANER;
    }
}
