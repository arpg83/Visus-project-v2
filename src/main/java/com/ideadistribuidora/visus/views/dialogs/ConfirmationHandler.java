package com.ideadistribuidora.visus.views.dialogs;

@FunctionalInterface
public interface ConfirmationHandler<T> {
    void handle(T parameter);
}
