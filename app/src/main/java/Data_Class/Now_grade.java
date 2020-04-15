package Data_Class;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;

public class Now_grade extends LitePalSupport {
    private double d0;
    private double d1;
    private double d2;
    private double d3;
    private double d4;
    private double d5;
    private double d6;
    private double d7;
    private double d8;

    public double find_double(int posint){
        ArrayList<Double> list=new ArrayList<>();
        list.add(d0);
        list.add(d1);
        list.add(d2);
        list.add(d3);
        list.add(d4);
        list.add(d5);
        list.add(d6);
        list.add(d7);
        list.add(d8);
        return list.get(posint);
    }


    public double getD0() {
        return d0;
    }

    public void setD0(double d0) {
        this.d0 = d0;
    }

    public double getD1() {
        return d1;
    }

    public void setD1(double d1) {
        this.d1 = d1;
    }

    public double getD2() {
        return d2;
    }

    public void setD2(double d2) {
        this.d2 = d2;
    }

    public double getD3() {
        return d3;
    }

    public void setD3(double d3) {
        this.d3 = d3;
    }

    public double getD4() {
        return d4;
    }

    public void setD4(double d4) {
        this.d4 = d4;
    }

    public double getD5() {
        return d5;
    }

    public void setD5(double d5) {
        this.d5 = d5;
    }

    public double getD6() {
        return d6;
    }

    public void setD6(double d6) {
        this.d6 = d6;
    }

    public double getD7() {
        return d7;
    }

    public void setD7(double d7) {
        this.d7 = d7;
    }

    public double getD8() {
        return d8;
    }

    public void setD8(double d8) {
        this.d8 = d8;
    }
}
