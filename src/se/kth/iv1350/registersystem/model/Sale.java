package se.kth.iv1350.registersystem.model;

import se.kth.iv1350.registersystem.dbhandler.*;

import java.util.List;
import java.util.ArrayList;

class Sale {
    private List<Item> items = new ArrayList<>();
    private double runningTotal;
    private double discountedTotal;
    private Receipt receipt;

    Sale() {
        this.runningTotal = 0;
    }

    SaleDTO registerItem(int quantity, InventoryDTO inventory) {
        Item item = new Item(quantity, inventory);

        if (inventory == null || quantity < 1) {
            return new SaleDTO("Invalid item identifier or quantity.", 0, runningTotal);
        }

        int index = indexOfItem(item);
        double itemPrice = inventory.getPrice() * (inventory.getVat() / 100 + 1);

        if (index != -1) {
            modifyQuantity(index, quantity);
        } else {
            this.items.add(new Item(quantity, inventory));
        }

        this.runningTotal += quantity * itemPrice;

        return new SaleDTO(inventory.getDescription(), itemPrice, runningTotal);
    }

    SaleDTO applyDiscount(CustomerDiscountDTO[] discounts) {
        return applyBestDiscount(discounts);
    }

    SaleDTO submitPayment(double amount) {
     double toPay = discountedTotal != 0.0d && discountedTotal < runningTotal ? discountedTotal : runningTotal;
     double change = amount - toPay;

     if (new Double(amount).compareTo(toPay) >= 0) {
         this.receipt = new Receipt("IV1350 SuperStore", new Address("Kistagangen", "1350", "Kista"),
                 items, runningTotal, discountedTotal != 0.0d ? discountedTotal : runningTotal, calcTotalVat(), amount, change);

         return new SaleDTO(receipt);
     }

        return new SaleDTO(amount - toPay);
    }

    Receipt getReceipt() {
        return receipt;
    }

    private int indexOfItem(Item item) {
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getItemDetails().getIdentifier().equals(item.getItemDetails().getIdentifier())) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void modifyQuantity(int index, int quantity) {
        items.get(index).setQuantity(items.get(index).getQuantity() + quantity);
    }

    private SaleDTO applyBestDiscount(CustomerDiscountDTO[] discounts) {
        int type = discounts.length == 0 ? 0 : -1;
        double lowestPrice = runningTotal,
            discountedPrice = runningTotal;



        for (int i = 0; i < discounts.length; i++) {
            switch (discounts[i].getDiscountType()) {
                case 1:
                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).getItemDetails().getIdentifier().equals(Integer.toString(discounts[i].getThreshold()))) {
                            discountedPrice = runningTotal - (items.get(j).getQuantity() * items.get(j).getItemDetails().getPrice() *
                                    (items.get(j).getItemDetails().getVat() / 100 + 1)) * ((double)discounts[i].getRate() / 100);
                        }
                    }
                    break;
                case 2:
                    int numberOfItems = 0;

                    for (int j = 0; j < items.size(); j++) {
                        numberOfItems += items.get(j).getQuantity();
                    }

                    if (numberOfItems >= discounts[i].getThreshold()) {
                        discountedPrice = runningTotal * (1 - (double)discounts[i].getRate() / 100);
                    }
                    break;
                case 3:
                    if (runningTotal >= discounts[i].getThreshold()) {
                        discountedPrice = runningTotal * (1 - (double)discounts[i].getRate() / 100);
                    }
                    break;
                default:
                    // n/a
            }

            if (discountedPrice < lowestPrice) {
                type = discounts[i].getDiscountType();
                lowestPrice = discountedPrice;
            }
        }

        this.discountedTotal = lowestPrice;
        return new SaleDTO(runningTotal, discountedTotal, type);
    }

    private double calcTotalVat() {
        double vat = 0;

        for (int i = 0; i < items.size(); i++) {
            vat += items.get(i).getQuantity() * (items.get(i).getItemDetails().getVat() / 100) * items.get(i).getItemDetails().getPrice();
        }

        return vat;
    }
}
