package position;

import price.Price;
import tseries.TimeSeries;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class Position {


    /// Attributes

    protected double lotSize;

    protected LocalDateTime openDate;

    protected LocalDateTime closeDate;

    protected double openPrice;

    protected double closePrice;

    protected boolean isOpen;

    protected double profit;

    protected boolean success;

    protected TreeMap<LocalDateTime, Double> values = new TreeMap<LocalDateTime, Double>();

    protected String type;



    /// Methods

    // Pozíció létrehozása / nyitása
    public Position(LocalDateTime date, double lot, double price) {
        this.openDate = date;
        this.openPrice = price;
        this.lotSize = lot;
        this.isOpen = true;
        this.profit = 0.0;
        this.values.put(date, 0.0);
    }


    //Nyitott még a pozíció?
    public boolean getIsOpen() { return this.isOpen; }


    //Sikeres a már zárt nyitott?
    public boolean getSuccess() { return this.success; }


    // Pozíció típusának lekérdezése
    public String getType() { return this.type; }


    // Pozíció típusának beállítása
    public void setType(String type) { this.type = type; }


    // Pozíción elért profit lekérdezése
    public double getProfit() { return this.profit; }


    // Pozíció frissítése
    public void updatePosition(LocalDateTime date, double currentPrice) {}


    // Pozíció zárása
    public void closePosition(LocalDateTime date, double currentPrice) {

        // Egy utolsó update
        updatePosition(date, currentPrice);

        this.closeDate = date;
        this.closePrice = currentPrice;

        this.profit = this.values.get(date);

        if(this.profit > 0.0)
            this.success = true;
        else
            this.success = false;

        this.isOpen = false;
    }


    // Pozíció zárása
    // Ha véget érne a minta
    public void forcedClosePosition() {

        // Itt már nem kell az utolsó update

        this.closeDate = this.values.lastKey();
        this.profit = this.values.get(closeDate);

        if(this.profit > 0.0)
            this.success = true;
        else
            this.success = false;

        this.isOpen = false;

    }


}
