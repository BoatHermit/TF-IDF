package org.example;

import org.example.controller.AssessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Yin Zihang
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    AssessController assessController;

    @Autowired
    ApplicationRunnerImpl(AssessController assessController) {
        this.assessController = assessController;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        assessController.getAP();
        assessController.getMAP();
    }

}
