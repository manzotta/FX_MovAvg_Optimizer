package io;

import price.Price;
import tseries.TimeSeries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;



public class CSVReader {

    /// Attributes



    /// Methods
    public CSVReader() {}


    //public Treemap<K, V> HashSet jobb lenne?
    // throws Exception

    public TimeSeries read(String inputCSV) throws IOException {

        // String csvFile = "data/EURUSD_15m_2010-2016_v2.csv";
        // String csvFile = "data/EURUSD_15m_2010-2016_v5.csv";
        String line = "";
        String separator = ",";

        //ArrayList<Double[]> priceList =  new ArrayList<Double[]>();

        TimeSeries tseries = new TimeSeries();

        TreeMap<LocalDateTime, Price> tmap = new TreeMap<>();

        FileReader fr = new FileReader(inputCSV);
        BufferedReader br = new BufferedReader(fr);


        while(true) {

            line = br.readLine();

            if(line == null)
                break;
            //System.out.println(line);

            String[] values = line.split(separator);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm", Locale.US);
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm");

            //LocalDate localDate = LocalDate.parse(values[0], formatter);
            //System.out.println(localDate);

            LocalDateTime time = LocalDateTime.parse(values[0], formatter);
            //System.out.println(time);

            Price price = new Price();

            price.setOpen ( Double.parseDouble(values[1]) );
            price.setHigh ( Double.parseDouble(values[2]) );
            price.setLow  ( Double.parseDouble(values[3]) );
            price.setClose( Double.parseDouble(values[4]) );


            tmap.put(time, price);

        }

        br.close();

        tseries.setValues(tmap);

        return tseries;


    }


}
