package portfolio;

import position.BuyPosition;
import position.Position;
import position.SellPosition;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Portfolio {

    /// Attributes

    private LocalDateTime createTime;

    private LocalDateTime currentTime;

    private ArrayList<Position> positions;



    /// Methods

    public Portfolio(LocalDateTime date) {

        this.createTime = date;
        this.currentTime = date;
        this.positions = new ArrayList<>();

    }

    // Getter fv
    public LocalDateTime getCreateTime() {return this.createTime; }


    // Getter fv
    public LocalDateTime getCurrentTime() { return this.currentTime; }


    // Jelenlegi idő beállítása
    public void setCurrentTime(LocalDateTime date) { this.currentTime = date; }


    // Új pozíció hozzáadása a portfólióhoz
    public void addPosition(Position pos) {

        this.positions.add(pos);

    }


    /* A portfólióban található összes nyitott pozíció zárása
    public void closePositions(LocalDateTime closeDate, double closePrice) {

        if ( !(this.positions.isEmpty()) ) {

            for (Position pos : this.positions) {
                if (pos.getIsOpen()) {
                    pos.closePosition(closeDate, closePrice);
                }
            }

        }

    }
    */

    // A portfolióban található nyitott vételi pozíciók zárása
    public void closeBuyPositions(LocalDateTime closeDate, double closePrice) {

        if ( this.positions.isEmpty() )
            return;

        for (Position pos : this.positions) {
            if (pos.getType().equals("Buy") && pos.getIsOpen())
                    pos.closePosition(closeDate, closePrice);


        }

    }


    // A portfolióban található nyitott eladási pozíciók zárása
    public void closeSellPositions(LocalDateTime closeDate, double closePrice) {

        if ( this.positions.isEmpty() )
            return;

        for (Position pos : this.positions) {
            if (pos.getType().equals("Sell") && pos.getIsOpen())
                pos.closePosition(closeDate, closePrice);


        }

    }



    // A portfólióban levő nyitott pozíciók értékének frissítése
    public void updatePortfolio(LocalDateTime date, double price) {

        if ( this.positions.isEmpty() )
            return;

        for (Position pos : this.positions) {
                pos.updatePosition(date, price);
            }

     }



    // Ha az iteráció végén.. ami pozíció nyitva van még azt ne lehet zárni
    public void gameOver() {

        if (this.positions.isEmpty())
            return;

        for (Position pos : this.positions) {
                if ( pos.getIsOpen() )
                    pos.forcedClosePosition();
        }

    }


    // WinningRatio értékének kiszámítása
    public double winnigRatio() {

        double wr = 0.0;
        double siz = this.positions.size();
        double successful = 0;

        for (Position pos : this.positions) {
            if ( pos.getIsOpen() == false) {
                if ( pos.getSuccess() )
                    successful = successful + 1;
            }
        }

        return (successful / siz) * 100;

    }


    // Nettó profit értékének kiszámítása
    public double netProfit() {

        double profit = 0.0;
        int siz = this.positions.size();

        for (Position pos : this.positions) {
            if ( pos.getIsOpen() == false) {
                profit = profit + pos.getProfit();
            }
        }

        return profit;

    }


}
