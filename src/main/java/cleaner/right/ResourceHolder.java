package cleaner.right;

import cleaner.ExternalResource;
import java.lang.ref.Cleaner;
import cleaner.AppUtil;

public class ResourceHolder {

    private final Cleaner.Cleanable cleanable;
    private final ExternalResource externalResource;

    public ResourceHolder(ExternalResource externalResource) {
        cleanable = AppUtil.getCleaner().register(this, new CleaningAction(externalResource));
        this.externalResource = externalResource;
    }

    //    you can call this method whenever is the right time to release resource
    public void releaseResource() {
        cleanable.clean();
    }

    public void doSomethingWithResource() {
        System.out.printf("Do something cool with the important resource: %s \n", this.externalResource);
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

    public static void main(String... args) {
        ResourceHolder resourceHolder = new ResourceHolder(new ExternalResource(1));
        resourceHolder.doSomethingWithResource();
/*
        After doing some important work, we can explicitly release
        resources/invoke the cleaning action
*/
        resourceHolder.releaseResource();
//        What if we explicitly invoke the cleaning action twice?
        resourceHolder.releaseResource();
    }
}
