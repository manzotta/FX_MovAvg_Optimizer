package position;

import tseries.TimeSeries;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class SellPosition extends Position {


    /// Attributes



    /// Methods

    // Pozíció létrehozása / nyitása
    public SellPosition(LocalDateTime date, double lot, double price) {
        super(date, lot, price);
        this.setType("Sell");
    }


    // Pozíció frissítése
    @Override
    public void updatePosition(LocalDateTime date, double currentPrice) {
        // Nyitott pozícióról beszélünk?
        if(this.isOpen == false)
            return;

        this.values.put( date, (this.lotSize * 100000) * (this.openPrice - currentPrice) );


    }


}
