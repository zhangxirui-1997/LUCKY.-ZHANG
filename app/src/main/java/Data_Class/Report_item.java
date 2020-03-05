package Data_Class;

import org.litepal.crud.LitePalSupport;

public class Report_item extends LitePalSupport {
    //private int Only_id;
    private String item_title;
    private String item_time;
    private String item_name;
    private String zhengpath;
    private String cepath;
    private String statue_now;
    private boolean judge;
//最后一个是状态位，未完成是还没有收到，完成是代表已经收到了


    public boolean isJudge() {
        return judge;
    }

    public void setJudge(boolean judge) {
        this.judge = judge;
    }

    public String getZhengpath() {
        return zhengpath;
    }

    public void setZhengpath(String zhengpath) {
        this.zhengpath = zhengpath;
    }

    public String getCepath() {
        return cepath;
    }

    public void setCepath(String cepath) {
        this.cepath = cepath;
    }

    public String getStatue_now() {
        return statue_now;
    }

    public void setStatue_now(String statue_now) {
        this.statue_now = statue_now;
    }


    /*public int getItem_id() {
        return Only_id;
    }

    public void setItem_id(int item_id) {
        this.Only_id = item_id;
    }*/

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_time() {
        return item_time;
    }

    public void setItem_time(String item_time) {
        this.item_time = item_time;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

}
