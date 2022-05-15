package price;

public class Price {

    /// Attributes

    private double open;
    private double high;
    private double low;
    private double close;



    /// Methods

    //? setterek és getterek ??

    public Price() {
        this.open   = 0.0;
        this.high   = 0.0;
        this.low    = 0.0;
        this.close  = 0.0;
    }


    public Price(double open, double high, double low, double close) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }


    public double getOpen() { return this.open; };

    public void setOpen(double open) { this.open = open; }


    public double getHigh() { return this.high; };

    public void setHigh(double high) { this.high = high; }


    public double getLow() { return this.low; };

    public void setLow(double low) { this.low = low; }


    public double getClose() { return this.close; };

    public void setClose(double close) { this.close = close; }



    /* Returns the string representation of a Price object */
    @Override
    public String toString() {
        return String.format(this.open + " " + this.high + " " + this.low + " " + this.close );
    }

}



