package com.sg.flooring;

import com.sg.flooring.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args){
        ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        FlooringMasteryController flooringMasteryController = appContext.getBean("flooringMasteryController", FlooringMasteryController.class);
        flooringMasteryController.run();
    }
}
