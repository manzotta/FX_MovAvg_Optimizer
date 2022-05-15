package indicator;

import price.Price;
import tseries.TimeSeries;

import java.time.LocalDateTime;
import java.util.*;

public class ExponentialMA extends MovingAverage {

    /// Attributes


    private double alpha;

    private Double oldValue = null;



    /// Methods

    // Constructor v1
    public ExponentialMA(int siz) {

        this.windowSize = siz;
        this.alpha = 2.0 / (siz + 1);
        values = new TreeMap<>();

    }


    // Constructor v2
    public ExponentialMA(double smooth) {

        this.alpha = smooth;
        values = new TreeMap<>();

    }


    // Getter
    public Double getAlpha() {
        return alpha;
    }


    // Calc EMA Values
    public void calc(TimeSeries inputSeries, String appliedPrice) throws IllegalArgumentException {

        Set set = inputSeries.getValues().entrySet();
        Iterator it = set.iterator();

        double smoothParam = 2.0 / (this.windowSize + 1);

        double newValue = 0.0;
        double priceValue;


        while ( it.hasNext() ) {

            Map.Entry map_entry = (Map.Entry)it.next();

            LocalDateTime date = (LocalDateTime) map_entry.getKey();
            Price p = (Price) map_entry.getValue();

            if (appliedPrice.equals("Close"))
                priceValue = p.getClose();

            else {
                throw new IllegalArgumentException("Az eljárás egyelőre csak záróárra [Close] alkalmazható!");
            }

            if(this.oldValue == null) {
                this.values.put(date, priceValue);
                this.oldValue = priceValue;
            }

            // Lényegi lépés, fontos milyen formulát használunk
            newValue = (priceValue - this.oldValue) * smoothParam + this.oldValue;
            this.values.put(date, newValue);
            this.oldValue = newValue;

        }


    }


}
