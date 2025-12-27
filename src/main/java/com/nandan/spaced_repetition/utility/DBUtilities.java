package com.nandan.spaced_repetition.utility;

import com.nandan.spaced_repetition.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

@Component
public class DBUtilities {

    @Autowired
    private QuestionsRepository questionsRepository;

    public void manualPOTDEviction(){
        Logger.info("Deleting POTD from database");
        questionsRepository.deleteByIsProblemOfTheDayTrue();
    }

}
