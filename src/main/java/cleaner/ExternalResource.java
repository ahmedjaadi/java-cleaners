package cleaner;

public class ExternalResource {
    private final int value;

    public ExternalResource(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExternalResource{" + "value=" + value +
               '}';
    }
}
