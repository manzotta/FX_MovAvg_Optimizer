package trader;

import indicator.ExponentialMA;
import indicator.MovingAverage;
import indicator.SimpleMA;
import portfolio.Portfolio;
import position.BuyPosition;
import position.SellPosition;
import tseries.TimeSeries;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class Trader {

    /// Attributes

    private int[] paramValues = new int[3];

    private MovingAverage MA_M15_Fast;
    private MovingAverage MA_M15_Slow;

    private MovingAverage MA_H1_Fast;
    private MovingAverage MA_H1_Slow;

    private MovingAverage MA_D1_Fast;
    private MovingAverage MA_D1_Slow;

    private TimeSeries priceSeries_M15 = new TimeSeries();
    private TimeSeries priceSeries_H1 = new TimeSeries();
    private TimeSeries priceSeries_D1 = new TimeSeries();

    private LocalDateTime startTime;

    private ArrayList<Map.Entry> sampleStart = new ArrayList<>();

    private TreeMap<LocalDateTime, String> Signals = new TreeMap<>();

    private Portfolio myPortfolio;

    private double winningRatio;

    private double netProfit;

    /// Methods

    // Összesen 6db mozgóátlagot kell majd létrehozni : három idősík X 2

    // A trader fogja az indikátorokat használni, a 6. db indikátort

    // A trader hozza létre a pozíciókat

    // A trader értékeli ki a pozíciókat tehát egy ár-idősort is kapnia kell ugye ..


    public Trader() {}


    public Trader(TimeSeries priceSeries, int fast, int mid, int slow, String type) throws Exception {

        this.priceSeries_M15 = priceSeries;

        this.paramValues[0] = fast;
        this.paramValues[1] = mid;
        this.paramValues[2] = slow;


        switch(type)
        {
            case "Simple":

                // M15
                this.MA_M15_Fast = new SimpleMA(fast);
                this.MA_M15_Slow = new SimpleMA(mid);

                // H1
                this.MA_H1_Fast = new SimpleMA(fast);
                this.MA_H1_Slow = new SimpleMA(mid);

                // D1
                this.MA_D1_Fast = new SimpleMA(mid);
                this.MA_D1_Slow = new SimpleMA(slow);

                break;

            case "Exponential":

                // M15
                this.MA_M15_Fast = new ExponentialMA(fast);
                this.MA_M15_Slow = new ExponentialMA(mid);

                // H1
                this.MA_H1_Fast = new ExponentialMA(fast);
                this.MA_H1_Slow = new ExponentialMA(mid);

                // D1
                this.MA_D1_Fast = new ExponentialMA(mid);
                this.MA_D1_Slow = new ExponentialMA(slow);

                break;

            default:
                Exception e = new Exception("Helytelen input érték");
                throw e;

        }

    }


    public int[] getParamValues() {return this.paramValues; }


    public double getWinningRatio() { return this.winningRatio; }


    public double getNetProfit() { return this.netProfit; }


    // Indikátorok előkészítése
    public void prepareIndicators() {

        try {

            this.priceSeries_H1 = this.priceSeries_M15.convert("H1");
            // System.out.println(this.priceSeries_H1.getLength());

            this.priceSeries_D1 = this.priceSeries_M15.convert("D1");
            // System.out.println(this.priceSeries_D1.getLength());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Calculate MA Values
        MA_M15_Fast.calc(this.priceSeries_M15, "Close");
        MA_M15_Slow.calc(this.priceSeries_M15, "Close");

        MA_H1_Fast.calc(this.priceSeries_H1, "Close");
        MA_H1_Slow.calc(this.priceSeries_H1, "Close");

        MA_D1_Fast.calc(this.priceSeries_D1, "Close");
        MA_D1_Slow.calc(this.priceSeries_D1, "Close");


    }


    public void trade() {

        // LocalDateTime for M15
        LocalDateTime m15_startDate = LocalDateTime.of(2010, Month.JANUARY, 2, 3, 15, 00);

        // LocalDateTime for H1 (M15 előtti teljes lezárt óra)
        LocalDateTime h1_startDate = LocalDateTime.of(2010, Month.JANUARY, 2, 2, 00, 00);

        // LocalDateTime for D1 (M15 előtti teljes lezárt nap)
        LocalDateTime d1_startDate = LocalDateTime.of(2010, Month.JANUARY, 1, 0, 00, 00);


        double testSMA = this.MA_M15_Fast.getValues().get(m15_startDate);
        //System.out.println(testSMA);
        //sma_m15_fast.getValues().ceilingKey(200);


        // M15 idősíkon értelmezett iterátorok
        SortedMap<LocalDateTime, Double> sampleMA_M15_fast = MA_M15_Fast.getValues().tailMap(m15_startDate);
        Set setMA_M15_fast = sampleMA_M15_fast.entrySet();
        Iterator iterMA_M15_fast = setMA_M15_fast.iterator();
        Map.Entry entryMA_M15_fast = (Map.Entry) iterMA_M15_fast.next();
        this.sampleStart.add(entryMA_M15_fast);

        SortedMap<LocalDateTime, Double> sampleMA_M15_slow = MA_M15_Slow.getValues().tailMap(m15_startDate);
        Set setMA_M15_slow = sampleMA_M15_slow.entrySet();
        Iterator iterMA_M15_slow = setMA_M15_slow.iterator();
        Map.Entry entryMA_M15_slow = (Map.Entry) iterMA_M15_slow.next();
        this.sampleStart.add(entryMA_M15_slow);


        // H1 idősíkon értelmezett iterátorok
        SortedMap<LocalDateTime, Double> sampleMA_H1_fast = MA_H1_Fast.getValues().tailMap(h1_startDate);
        Set setMA_H1_fast = sampleMA_H1_fast.entrySet();
        Iterator iterMA_H1_fast = setMA_H1_fast.iterator();
        Map.Entry entryMA_H1_fast = (Map.Entry) iterMA_H1_fast.next();
        this.sampleStart.add(entryMA_H1_fast);

        SortedMap<LocalDateTime, Double> sampleMA_H1_slow = MA_H1_Slow.getValues().tailMap(h1_startDate);
        Set setMA_H1_slow = sampleMA_H1_slow.entrySet();
        Iterator iterMA_H1_slow = setMA_H1_slow.iterator();
        Map.Entry entryMA_H1_slow = (Map.Entry) iterMA_H1_slow.next();
        this.sampleStart.add(entryMA_H1_slow);


        // D1 idősíkon értelmezett iterátorok
        SortedMap<LocalDateTime, Double> sampleMA_D1_fast = MA_D1_Fast.getValues().tailMap(d1_startDate);
        Set setMA_D1_fast = sampleMA_D1_fast.entrySet();
        Iterator iterMA_D1_fast = setMA_D1_fast.iterator();
        Map.Entry entryMA_D1_fast = (Map.Entry) iterMA_D1_fast.next();
        this.sampleStart.add(entryMA_D1_fast);


        SortedMap<LocalDateTime, Double> sampleMA_D1_slow = MA_D1_Slow.getValues().tailMap(d1_startDate);
        Set setMA_D1_slow = sampleMA_D1_slow.entrySet();
        Iterator iterMA_D1_slow = setMA_D1_slow.iterator();
        Map.Entry entryMA_D1_slow = (Map.Entry) iterMA_D1_slow.next();
        this.sampleStart.add(entryMA_D1_slow);


        /*
        Map.Entry entryMA_M15_fast = this.sampleStart.get(0);
        Map.Entry entryMA_M15_slow = this.sampleStart.get(1);

        Map.Entry entryMA_H1_fast = this.sampleStart.get(2);
        Map.Entry entryMA_H1_slow = this.sampleStart.get(3);

        Map.Entry entryMA_D1_fast = this.sampleStart.get(4);
        Map.Entry entryMA_D1_slow = this.sampleStart.get(5);
        */

        LocalDateTime startDateTime = (LocalDateTime) entryMA_M15_fast.getKey();

        TimeSeries myPrice = this.priceSeries_M15;

        this.myPortfolio = new Portfolio(startDateTime);

        LocalDateTime currentDateTime;
        double currentPrice;


        int counter = 0;
        int crosses = 0;

        double oldM15_fast = 99.9; // elsőre ne legyen jelzés!
        double oldM15_slow = 0.01; //

        double oldH1_fast = 99.9; // elsőre ne legyen jelzés!
        double oldH1_slow = 0.01; //


        while(iterMA_M15_fast.hasNext()) {


            currentDateTime = (LocalDateTime) entryMA_M15_fast.getKey();
            currentPrice = myPrice.getValues().get(currentDateTime).getClose();

            myPortfolio.setCurrentTime(currentDateTime);


            if( (counter % 4) == 3 ) {
                entryMA_H1_fast = (Map.Entry) iterMA_H1_fast.next();
                entryMA_H1_slow = (Map.Entry) iterMA_H1_slow.next();
                //System.out.println(currentDateTime.toString());
                //LocalDateTime date_h1 = (LocalDateTime) entryMA_H1_fast.getKey();
            }

            if( (counter % 96) == 95 ) {
                entryMA_D1_fast = (Map.Entry) iterMA_D1_fast.next();
                entryMA_D1_slow = (Map.Entry) iterMA_D1_slow.next();
                //System.out.println(currentDateTime.toString());
                //LocalDateTime date_d1 = (LocalDateTime) mapEntry_d1.getKey();
            }


            // M15
            double m15_fast = (double) entryMA_M15_fast.getValue();
            double m15_slow = (double) entryMA_M15_slow.getValue();


            // H1
            double h1_fast = (double) entryMA_H1_fast.getValue();
            double h1_slow = (double) entryMA_H1_slow.getValue();


            // D1
            double d1_fast = (double) entryMA_D1_fast.getValue();
            double d1_slow = (double) entryMA_D1_slow.getValue();


            // BuyPosition nyitásának logikája stratégia logikája (egyenlőséget egyelőre nem engedünk meg)
            if( d1_fast > d1_slow && h1_fast > h1_slow && oldM15_fast < oldM15_slow && m15_fast > m15_slow ) {
                // if( oldM15_fast < oldM15_slow && m15_fast > m15_slow ) {

                // Itt nyitnánk alapból pozíciót
                // System.out.println("Buy");
                Signals.put(currentDateTime, "Buy!");

                BuyPosition buyPos = new BuyPosition(currentDateTime, 1, currentPrice);

                myPortfolio.addPosition(buyPos);

                crosses++;

            }


            // BuyPosition zárásának logikája stratégia logikája (egyenlőséget egyelőre nem engedünk meg)
            if( d1_fast > d1_slow && oldH1_fast > oldH1_slow && h1_fast < h1_slow ) {

                Signals.put(currentDateTime, "Close Buy Positions!");

                // Itt zárjuk a nyitott long pozíciókat
                myPortfolio.closeBuyPositions(currentDateTime, currentPrice);

            }


            /*
            // SellPosition nyitásának logikája stratégia logikája
            if( d1_fast < d1_slow && h1_fast < h1_slow && oldM15_fast > oldM15_slow && m15_fast < m15_slow ) {

                // Itt nyitnánk alapból pozíciót
                // System.out.println("Sell");
                Signals.put(currentDateTime, "Sell!");

                SellPosition sellPos = new SellPosition(currentDateTime, 1, currentPrice);

                myPortfolio.addPosition(sellPos);

                crosses++;

            }


            // SellPosition zárásának logikája stratégia logikája (egyenlőséget egyelőre nem engedünk meg)
            if( d1_fast < d1_slow && oldH1_fast < oldH1_slow && h1_fast > h1_slow ) {

                Signals.put(currentDateTime, "Close Sell Positions!");

                // Itt zárjuk a nyitott short pozíciókat
                myPortfolio.closeSellPositions(currentDateTime, currentPrice);

            }
            */

            // Portfolio frissítése
            myPortfolio.updatePortfolio(currentDateTime, currentPrice);

            oldH1_fast = h1_fast;
            oldH1_slow = h1_slow;

            oldM15_fast = m15_fast;
            oldM15_slow = m15_slow;

            entryMA_M15_fast = (Map.Entry) iterMA_M15_fast.next(); //M15-ös iterátor léptetése
            entryMA_M15_slow = (Map.Entry) iterMA_M15_slow.next(); //M15-ös iterátor léptetése

            counter = counter + 1;


        }

        //System.out.println(crosses);

    }


    public void stats() {

        this.winningRatio = myPortfolio.winnigRatio();
        System.out.println("WR: " + this.winningRatio);

        this.netProfit = myPortfolio.netProfit();
        System.out.println("NetProfit: " + this.netProfit);

    }


}
