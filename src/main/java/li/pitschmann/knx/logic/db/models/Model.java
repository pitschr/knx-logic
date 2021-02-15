package li.pitschmann.knx.logic.db.models;

/**
 * The root model
 *
 * @author PITSCHR
 */
public class Model {
    /**
     * If {@code id} is {@code -1} then it means that the model
     * has not been saved in the database yet.
     */
    protected int id = -1;

    public int getId() {
        return this.id;
    }
}
