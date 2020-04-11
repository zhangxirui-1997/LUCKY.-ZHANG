package Data_Class;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;

public class AboutRankingList extends LitePalSupport {
    public int point0;
    public int point05;
    public int point10;
    public int point15;
    public int point20;
    public int point25;
    public int point30;
    public int point35;
    public int point40;
    public int point45;
    public int point50;
    public int point55;
    public int point60;
    public int point65;
    public int point70;
    public int point75;
    public int point80;
    public int point85;
    public int point90;
    public int point95;
    public int point00;
    public ArrayList<Integer> list =new ArrayList<Integer>();;
    public double calculate(double d){
        if(d==0d)return point0;
        if(d==100d)return point00;

        for(int i=0;i<21;i++){
            if (i*5<d&&d<(i+1)*5){
                return (d-i*5)/5*(list.get(i + 1) - list.get(i))+list.get(i);
            }
        }
        return -1d;
    }

    public void initJSON(String string){
        try {
            JSONObject jsonObject=new JSONObject(string);
            this.point0= Integer.parseInt((jsonObject.getString("point0")));
            this.point05= Integer.parseInt((jsonObject.getString("point05")));
            this.point10= Integer.parseInt((jsonObject.getString("point10")));
            this.point15= Integer.parseInt((jsonObject.getString("point15")));
            this.point20= Integer.parseInt((jsonObject.getString("point20")));
            this.point25= Integer.parseInt((jsonObject.getString("point25")));
            this.point30= Integer.parseInt((jsonObject.getString("point30")));
            this.point35= Integer.parseInt((jsonObject.getString("point35")));
            this.point40= Integer.parseInt((jsonObject.getString("point40")));
            this.point45= Integer.parseInt((jsonObject.getString("point45")));
            this.point50= Integer.parseInt((jsonObject.getString("point50")));
            this.point55= Integer.parseInt((jsonObject.getString("point55")));
            this.point60= Integer.parseInt((jsonObject.getString("point60")));
            this.point65= Integer.parseInt((jsonObject.getString("point65")));
            this.point70= Integer.parseInt((jsonObject.getString("point70")));
            this.point75= Integer.parseInt((jsonObject.getString("point75")));
            this.point80= Integer.parseInt((jsonObject.getString("point80")));
            this.point85= Integer.parseInt((jsonObject.getString("point85")));
            this.point90= Integer.parseInt((jsonObject.getString("point90")));
            this.point95= Integer.parseInt((jsonObject.getString("point95")));
            this.point00= Integer.parseInt((jsonObject.getString("point00")));
            list.clear();
            list.add(point0);
            list.add(point05);
            list.add(point10);
            list.add(point15);
            list.add(point20);
            list.add(point25);
            list.add(point30);
            list.add(point35);
            list.add(point40);
            list.add(point45);
            list.add(point50);
            list.add(point55);
            list.add(point60);
            list.add(point65);
            list.add(point70);
            list.add(point75);
            list.add(point80);
            list.add(point85);
            list.add(point90);
            list.add(point95);
            list.add(point00);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getPoint0() {
        return point0;
    }

    public void setPoint0(int point0) {
        this.point0 = point0;
    }

    public int getPoint05() {
        return point05;
    }

    public void setPoint05(int point05) {
        this.point05 = point05;
    }

    public int getPoint10() {
        return point10;
    }

    public void setPoint10(int point10) {
        this.point10 = point10;
    }

    public int getPoint15() {
        return point15;
    }

    public void setPoint15(int point15) {
        this.point15 = point15;
    }

    public int getPoint20() {
        return point20;
    }

    public void setPoint20(int point20) {
        this.point20 = point20;
    }

    public int getPoint25() {
        return point25;
    }

    public void setPoint25(int point25) {
        this.point25 = point25;
    }

    public int getPoint30() {
        return point30;
    }

    public void setPoint30(int point30) {
        this.point30 = point30;
    }

    public int getPoint35() {
        return point35;
    }

    public void setPoint35(int point35) {
        this.point35 = point35;
    }

    public int getPoint40() {
        return point40;
    }

    public void setPoint40(int point40) {
        this.point40 = point40;
    }

    public int getPoint45() {
        return point45;
    }

    public void setPoint45(int point45) {
        this.point45 = point45;
    }

    public int getPoint50() {
        return point50;
    }

    public void setPoint50(int point50) {
        this.point50 = point50;
    }

    public int getPoint55() {
        return point55;
    }

    public void setPoint55(int point55) {
        this.point55 = point55;
    }

    public int getPoint60() {
        return point60;
    }

    public void setPoint60(int point60) {
        this.point60 = point60;
    }

    public int getPoint65() {
        return point65;
    }

    public void setPoint65(int point65) {
        this.point65 = point65;
    }

    public int getPoint70() {
        return point70;
    }

    public void setPoint70(int point70) {
        this.point70 = point70;
    }

    public int getPoint75() {
        return point75;
    }

    public void setPoint75(int point75) {
        this.point75 = point75;
    }

    public int getPoint80() {
        return point80;
    }

    public void setPoint80(int point80) {
        this.point80 = point80;
    }

    public int getPoint85() {
        return point85;
    }

    public void setPoint85(int point85) {
        this.point85 = point85;
    }

    public int getPoint90() {
        return point90;
    }

    public void setPoint90(int point90) {
        this.point90 = point90;
    }

    public int getPoint95() {
        return point95;
    }

    public void setPoint95(int point95) {
        this.point95 = point95;
    }

    public int getPoint00() {
        return point00;
    }

    public void setPoint00(int point00) {
        this.point00 = point00;
    }
}
