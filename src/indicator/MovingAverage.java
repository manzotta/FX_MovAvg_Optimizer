package indicator;

import tseries.TimeSeries;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class MovingAverage {

    // Attributes

    protected int windowSize;

    protected TreeMap<LocalDateTime, Double> values;



    // Methods

    // Default constructor
    public MovingAverage() {}


    // GetValues
    public TreeMap<LocalDateTime, Double> getValues() {
        return this.values;
    }


    // GetWindow size
    public int getWindowSize() {
        return this.windowSize;
    }


    // Pure Virtual(?)
    public abstract void calc(TimeSeries tseries, String str);


    // Print
    public void printValues() {
        Set set = this.values.entrySet();
        Iterator it = set.iterator();

        while(it.hasNext()) {
            Map.Entry map_entry = (Map.Entry)it.next();
            System.out.print("Date: " + map_entry.getKey() + " & Value: ");
            System.out.println(map_entry.getValue());
        }
    }



}
