package io.fourfinanceit.validation;

public interface Validator<T> {

    boolean validate(T value);
}
