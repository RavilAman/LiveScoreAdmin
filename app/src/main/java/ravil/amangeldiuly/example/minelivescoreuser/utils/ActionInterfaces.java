package ravil.amangeldiuly.example.minelivescoreuser.utils;

import java.time.LocalDate;

public interface ActionInterfaces {

    interface CreateGameDialogCloseListener {
        void onDialogClosed(LocalDate toDate);
    }

    interface ManipulateEventDialogCloseListener {
        void onDialogClosed(String message);
    }
}
