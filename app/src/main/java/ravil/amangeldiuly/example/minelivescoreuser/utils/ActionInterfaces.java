package ravil.amangeldiuly.example.minelivescoreuser.utils;

import java.time.LocalDate;

import ravil.amangeldiuly.example.minelivescoreuser.enums.EventEnum;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AssistDTO;

public interface ActionInterfaces {

    interface CreateGameDialogCloseListener {
        void onDialogClosed(LocalDate toDate);
    }

    interface ManipulateEventDialogCloseListener {
        void onDialogClosed(String message);
    }

    interface ManipulateEventDialogOpenListener {
        void onDialogOpen(String teamLogo, long teamId, EventEnum eventEnum, Long playerId, Integer minute, Long eventId, AssistDTO assistDTO);
    }
}
