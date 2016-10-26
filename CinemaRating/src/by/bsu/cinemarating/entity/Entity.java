package by.bsu.cinemarating.entity;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 12.04.16
 * Time: 3:52
 * Any expert entity with one identifier.
 */
public abstract class Entity {
    private int id;

    public Entity() {
    }

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
