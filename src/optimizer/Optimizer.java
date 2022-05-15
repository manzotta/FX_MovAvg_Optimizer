package optimizer;

import io.CSVReader;
import trader.Trader;
import tseries.TimeSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Optimizer {

    /// Attributes

    private int[] fastMAParams;

    private int[] midMAParams;

    private int[] slowMAParams;

    private String MAType;

    private TimeSeries pirceSeries;

    private ArrayList<Trader> candidateTraders = new ArrayList<>();

    private double bestWR = 0.0;

    private double bestNetProfit = -10000000;

    private Trader bestTrader = new Trader();



    /// Methods

    public Optimizer(int fastMin, int fastMax, int MidMin, int MidMax, int SlowMin, int SlowMax, String str) {

        this.fastMAParams = IntStream.range(fastMin, (fastMax + 1)).toArray();
        System.out.println("Fast MA Params: " + Arrays.toString(this.fastMAParams));

        this.midMAParams = IntStream.range(MidMin, (MidMax + 1)).toArray();
        System.out.println("Mid MA Params: " + Arrays.toString(this.midMAParams));

        this.slowMAParams = IntStream.range(SlowMin, (SlowMax + 1)).toArray();
        System.out.println("Slow MA Params: " + Arrays.toString(this.slowMAParams));

        this.MAType = str;

    }


    public void importData(String inputFile) {

        CSVReader myReader = new CSVReader();

        this.pirceSeries = new TimeSeries();

        try {
            pirceSeries= myReader.read(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void initTraders() {

        for (int i = 0; i < slowMAParams.length; i++) {

            for (int j = 0; j < midMAParams.length; j++) {

                for (int k = 0; k < fastMAParams.length; k++) {


                    try {
                        Trader myTrader = new Trader(this.pirceSeries, this.fastMAParams[k], this.midMAParams[j], this.slowMAParams[i], this.MAType);
                        this.candidateTraders.add(myTrader);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }


    }


    public void maxProfit() {

        int siz = this.candidateTraders.size();

        int ind = 0;

        Iterator<Trader> i = this.candidateTraders.iterator();

        while (i.hasNext()) {

            Trader myTrader = i.next(); // must be called before you can call i.remove()

            myTrader.prepareIndicators();
            myTrader.trade();
            myTrader.stats();

            if (myTrader.getNetProfit() > this.bestNetProfit) {
                this.bestNetProfit = myTrader.getNetProfit();
                this.bestTrader = myTrader;
            }

            i.remove();

            ind++;

            System.out.println(siz + " iterációból " + ind + " elkészült.");

        }

        this.bestWR = this.bestTrader.getWinningRatio();

    }


    public void maxWR() {

        int siz = this.candidateTraders.size();

        int ind = 0;

        Iterator<Trader> i = this.candidateTraders.iterator();

        while (i.hasNext()) {

            Trader myTrader = i.next(); // must be called before you can call i.remove()

            myTrader.prepareIndicators();
            myTrader.trade();
            myTrader.stats();

            if (myTrader.getWinningRatio() > this.bestWR) {
                this.bestWR = myTrader.getWinningRatio();
                this.bestTrader = myTrader;
            }

            i.remove();

            ind++;

            System.out.println(siz + " iterációból " + ind + " elkészült.");

        }

        this.bestNetProfit = this.bestTrader.getNetProfit();

    }

    // Eredmények rövid szöveges értékelése
    public void getResults() {
        System.out.println("My best TradingBot produced " + this.bestNetProfit + "USD profit.");
        System.out.println("Its WinningRatio is " + this.bestWR);
        System.out.println("The best parameter-coniguration for this strategy is: " + bestTrader.getParamValues()[0] + ", " + bestTrader.getParamValues()[1] + ", " + bestTrader.getParamValues()[2]);


    }


    public double getBestNetProfit() {
        return bestNetProfit;
    }


    public double getBestWR() {
        return bestWR;
    }


    public void setMAType(String MAType) {
        this.MAType = MAType;
    }


    public String getMAType() {
        return MAType;
    }

}

    /* Store the signals
        FileOutputStream f = new FileOutputStream("data/signals.txt");
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(Signals);
        out.close();
        */

    /* Display content of price_data using Iterator */






