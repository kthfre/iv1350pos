package se.kth.iv1350.registersystem.view;

import se.kth.iv1350.registersystem.controller.*;
import se.kth.iv1350.registersystem.dbhandler.*;
import se.kth.iv1350.registersystem.model.ItemQuantityException;
import se.kth.iv1350.registersystem.utils.ErrorMessageHandler;

import java.util.Scanner;

/**
 * Sample view for the program.
 */

public class View {
    private Controller ctrl;

    /**
     * Creates a new View instancce with the passed Controller instance.
     *
     * @param ctrl The Controller instance to be attached to this View instance.
     */

    public View(Controller ctrl) {
        this.ctrl = ctrl;
        ctrl.addRegisterObserver(new TotalRevenueView());
    }

    /**
     * Simulates a sale from start to finish.
     *
     * @param inData The Scanner instance that reads from specified source.
     */

    public void simulateSale(Scanner inData) {
        SaleDTO item = null;
        String input;
        String[] itemInput;
        ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
        String isDone = "(?i)^\\s*done\\s*$";
        String showLogs = "(?i)^\\s*(show|logs)\\s*$";
        String nextSale = "(?i)^\\s*(next|sale)\\s*$";
        String isItemIdentifier = "^\\s*([0-9]{3}(;[0-9]{1,3})?)\\s*$";
        String isQuit = "(?i)^\\s*(q|quit)\\s*$";
        String isIdentification = "^\\s*(0[1-9]|[1-9][0-9])(0[1-9]|1[0-2])(0[1-9]|[1-2][1-9]|3[0-1])[0-9]{4}\\s*$";
        String isPayment = "\\s*[0-9]+(\\.[0-9]{0,2})?\\s*";

        ctrl.initiateSale();
        System.out.println("New sale started.");
        System.out.println("Enter item identifier (optional quantity as a suffix separated by ; 123;3 etc.):");

        input = readInput(inData);

        while (!ctrl.interpretInput(input, isDone)) {
            if (ctrl.interpretInput(input, isItemIdentifier)) {
                /*itemInput = splitItemQuantifier(input);
                int quantity = itemInput.length == 2 ? Integer.parseInt(itemInput[1]) : 1;
                item = ctrl.scanItem(quantity, itemInput[0]);

                if (item.getDescription().equals("Invalid item identifier or quantity.")) {
                    System.out.printf("%s", item.getDescription());
                } else {
                    System.out.printf("%s. Price: %.2f incl VAT. Total price: %.2f.\n",
                            item.getDescription(), item.getPrice(), item.getRunningTotal());
                    System.out.println("Enter new item (123, 123;3 etc), or \"done\":");
                } */

                try {
                    itemInput = splitItemQuantifier(input);
                    int quantity = itemInput.length == 2 ? Integer.parseInt(itemInput[1]) : 1;
                    item = ctrl.scanItem(quantity, itemInput[0]);

                    System.out.printf("%s. Price: %.2f incl VAT. Total price: %.2f.\n",
                            item.getDescription(), item.getPrice(), item.getRunningTotal());
                    System.out.println("Enter new item (123, 123;3 etc), or \"done\":");
                } catch (GeneralFailureException e) {
                    errorMessageHandler.printError("The operation failed with reason: " + e.getMessage() + ". Please try again.");
                }
                catch (Exception e) {
                    errorMessageHandler.printError("Connectivity issues. Please try again later. Exiting.");
                    return;
                }
            } else {
                System.out.println("Cashier appears to have indelicate fingers, please enter a valid item identifier (123, 123;3 for optional quantity, etc).");
            }

            input = readInput(inData);
        }

        if (item == null) {
            while (!(ctrl.interpretInput(input, isQuit) || ctrl.interpretInput(input, nextSale))) {
                System.out.println("A sale has to include items. Type \"q\" or \"quit\" to quit, \"next\" or \"sale\" to start a new sale.");
                input = readInput(inData);
            }

            if (ctrl.interpretInput(input, nextSale)) {
                simulateSale(new Scanner(System.in));
            }

            return;
        }

        System.out.println("Item registration done.");
        System.out.printf("\nThe final amount to pay is: %.2f", item.getRunningTotal());
        System.out.println("\n\nDo you want to apply a discount? Enter customer identification (YYMMDDXXXX), or \"done\":");
        input = readInput(inData);

        while (!ctrl.interpretInput(input, isDone)) {
            if (ctrl.interpretInput(input, isIdentification)) {
                item = ctrl.applyDiscount(input);

                if (item.getDiscountType() == 0) {
                    System.out.println("Customer appears to be misguided. Full price for you my friend, or do you have an ID which isn't fake? (personal number YYMMDDXXXX or \"done\").");
                } else {
                    String discountType = item.getDiscountType() == 1 ? "per item" : item.getDiscountType() == 2 ? "number of items" : item.getDiscountType() == 3 ? "total amount" : "non-existant";
                    System.out.printf("\nThe most favorable discount type has been applied to the sale on a %s basis.\n" +
                                    "\nThe new total amount to pay is %.2f. The total discount rate is %.0f percent.\n\n",
                            discountType, item.getDiscountedTotal(), (1 - item.getDiscountedTotal() / item.getRunningTotal()) * 100);

                    break;
                }
            } else {
                System.out.println("Invalid ID, enter new (YYMMDDXXXX) or \"done\".");
            }

            input = readInput(inData);
        }

        input = "";

        while (!ctrl.interpretInput(input, isQuit)) {
            while (!(ctrl.interpretInput(input, isPayment))) {
                System.out.println("Show me the money! (250, 250.xx for pennies, etc)");
                input = readInput(inData);
            }

            item = ctrl.pay(Double.parseDouble(input));

            if (item.getChange() < 0) {
                System.out.printf("Your payment is unfortunately below the total amount to be paid. You are %.2f short. Do you want to try again? (enter sum (250, 250.xx etc) or \"q\"/\"quit\".)\n", -1 * item.getChange());
                input = readInput(inData);
            } else {
                System.out.printf("Your payment has been processed, you'll be receiving a change of %.2f. Here is your receipt.\n", item.getChange());
                printReceipt(item);
                break;
            }
        }

        System.out.println("\nType \"next\" or \"sale\" to start a new sale, type \"logs\" or \"show\" to print out the receipt for the logged sales, type anything else to be logged out.");
        input = readInput(inData);

        if (ctrl.interpretInput(input, nextSale)) {
            simulateSale(new Scanner(System.in));
        } else if (ctrl.interpretInput(input, showLogs)) {
            for (int i = 0; i < ctrl.retrieveLogCount(); i++) {
                item = ctrl.retrieveLog(i);
                System.out.println();
                printReceipt(item);
            }
        }
        else {
            System.out.println("Thank you for your service at our wonderful cashier system. You are now logged out.");
        }
    }

    private String centerText(String text) {
        StringBuilder offset = new StringBuilder();
        int totalWidth = 50;
        int offsetBy = (totalWidth - text.length()) / 2;

        for (int i = 0; i < offsetBy; i++) {
            offset.append(" ");
        }

        return text + offset;
    }

    static String toTwoDec(double num) {
        String[] separatedNumber = Double.toString(num).split("\\.");
        StringBuilder twoDecimalRepresentation = new StringBuilder(separatedNumber[0] + ".");

        if (separatedNumber[1].length() == 0) {
            twoDecimalRepresentation.append("00");
        } else if (separatedNumber[1].length() == 1) {
            twoDecimalRepresentation.append(separatedNumber[1].charAt(0)).append("0");
        } else if (separatedNumber[1].length() == 2) {
            twoDecimalRepresentation.append(separatedNumber[1].charAt(0)).append(separatedNumber[1].charAt(1));
        } else {
            int correctRounding = Character.getNumericValue(separatedNumber[1].charAt(2)) > 4 ? Character.getNumericValue(separatedNumber[1].charAt(1)) + 1 : Character.getNumericValue(separatedNumber[1].charAt(1));
            twoDecimalRepresentation.append(separatedNumber[1].charAt(0));
            twoDecimalRepresentation.append(correctRounding);
        }

        return twoDecimalRepresentation.toString();
    }

    private void printReceipt(SaleDTO item) {
        String separator = "|--------------------------------------------------|";

        System.out.printf("\n" + separator + "\n" +
                "|%50s|\n|%50s|\n|%50s|\n" +
                separator + "\n", centerText(item.getStoreName()), centerText(item.getStoreAddress()), centerText(item.getDateTime()));

        for (int i = 0; i < item.getItems().length; i++) {
            int j = item.getItems()[i].getQuantity();
            double k = item.getItems()[i].getItemDetails().getPrice() * (item.getItems()[i].getItemDetails().getVat() / 100 + 1);

            System.out.printf("| %-22s %16s %8s |\n", item.getItems()[i].getItemDetails().getDescription(), j == 1 ? "" : Integer.toString(j) + " x " + toTwoDec(k), toTwoDec(j * k));
        }

        System.out.printf(separator + "\n| Discounts: %37.2f |" +
                "\n| To pay: %40.2f |" +
                "\n| Total VAT: %37.2f |" +
                "\n| Amount received: %31.2f |" +
                "\n| Change returned: %31.2f |\n" + separator, item.getRunningTotal() - item.getDiscountedTotal(), item.getDiscountedTotal(), item.getVat(), item.getPaid(), item.getChange());
    }

    private String readInput(Scanner inData) {
        return inData.nextLine();
    }

    private String[] splitItemQuantifier(String input) {
        return input.split(";");
    }
}
