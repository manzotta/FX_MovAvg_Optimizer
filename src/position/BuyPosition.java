package position;

import tseries.TimeSeries;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class BuyPosition extends Position {


    /// Attributes



    /// Methods

    // Pozíció létrehozása / nyitása
    public BuyPosition(LocalDateTime date, double lot, double price) {
        super(date, lot, price);
        this.setType("Buy");
    }


    // Pozíció frissítése
    @Override
    public void updatePosition(LocalDateTime date, double currentPrice) {
        // Nyitott pozícióról beszélünk?
        if(this.isOpen == false)
            return;

        /* Primitív StopLoss
        if ( (this.lotSize * 1000) * (currentPrice - this.openPrice) < - 10 ) {
            this.values.put(date, (this.lotSize * 1000) * (currentPrice - this.openPrice));

            this.closeDate = date;
            this.closePrice = currentPrice;

            this.profit = this.values.get(date);

            if(this.profit > 0.0)
                this.success = true;
            else
                this.success = false;

            this.isOpen = false;

        }
        */

        this.values.put(date, (this.lotSize * 100000) * (currentPrice - this.openPrice));


    }


}
