package src;

/**
 * Documented class
 */
public class DocTest {
    /**
     * Document method
     */
    public void toto() {}

    public void totoUndocumented() {}

    /**
     * Documented method
     */
    protected void totoProtected() {}

    protected void totoProtectedUndocumented() {}

    /**
     * Documented field
     */
    protected boolean field;

    public String fieldNotDocumented;
}