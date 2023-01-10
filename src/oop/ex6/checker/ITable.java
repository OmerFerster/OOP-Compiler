package oop.ex6.checker;

public interface ITable<T> {

    boolean exists(String name);

    T getByName(String name);
}
