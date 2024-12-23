package cleaner.effective;

import java.lang.ref.Cleaner;
import cleaner.AppUtil;
import cleaner.ExternalResource;

public class ResourceHolder implements AutoCloseable {
    private final ExternalResource externalResource;

    private final Cleaner.Cleanable cleanable;

    public ResourceHolder(ExternalResource externalResource) {
        this.externalResource = externalResource;
        cleanable = AppUtil.getCleaner().register(this, new CleaningAction(externalResource));
    }

    public void doSomethingWithResource() {
        System.out.printf("Do something cool with the important resource: %s \n", this.externalResource);
    }

    @Override
    public void close() {
        System.out.println("cleaning action invoked by the close method");
        cleanable.clean();
    }

    static class CleaningAction implements Runnable {
        private ExternalResource externalResource;

        CleaningAction(ExternalResource externalResource) {
            this.externalResource = externalResource;
        }

        @Override
        public void run() {
//            cleaning up the important resources
            System.out.println("Doing some cleaning logic here, releasing up very important resources");
            externalResource = null;
        }
    }

    public static void main(String[] args) {
        //        This is an effective way to use cleaners with instances that hold crucial resources
        try (ResourceHolder cleaningExample = new ResourceHolder(new ExternalResource(1))) {
            cleaningExample.doSomethingWithResource();
            System.out.println("Goodbye");
        }
/*
    In case the client code does not use the try-with-resource as expected,
    the Cleaner will act as the safety-net
*/
        ResourceHolder cleaningExample = new ResourceHolder(new ExternalResource(2));
        cleaningExample.doSomethingWithResource();
        cleaningExample = null;
        System.gc(); // to facilitate the running of the cleaning action
    }
}
