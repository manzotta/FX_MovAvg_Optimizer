package tseries;

import price.Price;

import java.time.LocalDateTime;
import java.util.*;

public class TimeSeries {

    /// Attributes

        private TreeMap<LocalDateTime, Price> values;



    /// Methods

    //
    public TimeSeries() { values = new TreeMap<>(); }


    // GetValues
    public TreeMap<LocalDateTime, Price> getValues() {
        return this.values;
    }


    // SetValues
    public void setValues(TreeMap<LocalDateTime, Price> tmap) {
        this.values.putAll(tmap);
    }


    // GetLength
    public int getLength() { return this.values.size(); }


    // Mutate vs Transmute
    public TimeSeries convert(String timeFrame) throws IllegalArgumentException {

        int X = 0;

        switch(timeFrame)

        {

            case "H1":
                X = 4;
                break;

            case "D1":
                X = 96;
                break;

            default:
                throw new IllegalArgumentException("Helytelen input érték!");

        }


        Set set = this.values.entrySet();
        Iterator it = set.iterator();


        TimeSeries newSeries = new TimeSeries();

        // ez lesz az ablak
        TimeSeries tmpSeries = new TimeSeries();

        Set tmpSet = tmpSeries.values.entrySet();
        Iterator tmpIt = tmpSet.iterator();


        // Switch -- Case szerkezet timeFrame == H1 --> mod = 4

        int count = 0;

        while(it.hasNext()) {

            Map.Entry map_entry = (Map.Entry) it.next();

            LocalDateTime tmpDate = (LocalDateTime) map_entry.getKey();
            Price tmpPrice = (Price) map_entry.getValue();
            tmpSeries.values.put(tmpDate, tmpPrice);

            if( (count % X) == (X - 1) ) {
                LocalDateTime startDate = tmpSeries.values.firstKey();
                LocalDateTime endDate = tmpSeries.values.lastKey();
                Price p = (Price) tmpSeries.values.get(endDate);

                tmpSeries.values = new TreeMap<LocalDateTime, Price>();

                newSeries.values.put(startDate, p);
            }

            count = count + 1;
        }

        return newSeries;

    }


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
