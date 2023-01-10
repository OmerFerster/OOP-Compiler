package oop.ex6.main;

public enum Result {

    LEGAL(0),
    ILLEGAL(1),
    IO_ERROR(2);


    private final int code;

    Result(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
