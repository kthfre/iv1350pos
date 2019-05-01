package se.kth.iv1350.registersystem.model;

import se.kth.iv1350.registersystem.dbhandler.SaleDTO;

class SaleLog {
    private Log lastSale;
    private int noSales = 0;

    SaleLog() {

    }

    private class Log {
        private Sale sale;
        private Log previousSale;

        Log(Sale sale) {
            this.sale = sale;
        }

        void setPrevious(Log previous) {
            previousSale = previous;
        }
    }

    void logSale(Sale sale) {
        Log temp = lastSale;

        if (sale == null || sale.getReceipt() == null) {
            return;
        }

        lastSale = new Log(sale);
        this.noSales++;

        if (temp != null) {
            lastSale.setPrevious(temp);
        }
    }

    SaleDTO retrieveLog(int index) {
        Log log = lastSale;

        if (index < 0 || lastSale == null) {
            return null;
        }

        for (int i = 0; i < index; i++) {
            if (log.previousSale != null) {
                log = log.previousSale;
            } else {
                return null;
            }
        }

        return new SaleDTO(log.sale.getReceipt());
    }

    int retrieveLogCount() {
        return noSales;
    }

    private void updateExternalAccountingSystem() {
        // remote request
    }

    private void updateExternalInventorySystem() {
        // remote request
    }
}
