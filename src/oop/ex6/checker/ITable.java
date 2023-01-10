package oop.ex6.checker;

/**
 * An interface used to handle tables tracking different data types in the sjava code
 *
 * @param <T>
 */
public interface ITable<T> {

    boolean exists(String name);

    T getByName(String name);
}
