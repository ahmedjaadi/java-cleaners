package cleaner.simple;

import java.lang.ref.Cleaner;

public class ResourceHolder {
    private static final Cleaner CLEANER = Cleaner.create();

    public ResourceHolder() {
        CLEANER.register(this, () -> System.out.println("I'm doing some clean up"));
    }
    public static void main(String... args) {
        ResourceHolder resourceHolder = new ResourceHolder();
        resourceHolder = new ResourceHolder();
        System.gc();
    }

}
