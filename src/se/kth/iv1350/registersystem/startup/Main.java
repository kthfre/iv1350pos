package se.kth.iv1350.registersystem.startup;

import se.kth.iv1350.registersystem.controller.Controller;
import se.kth.iv1350.registersystem.view.View;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Controller ctrl = new Controller();
        View view = new View(ctrl);
        view.simulateSale(new Scanner(System.in));
    }
}
