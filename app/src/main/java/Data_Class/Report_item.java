package Data_Class;

import org.litepal.crud.LitePalSupport;

public class Report_item extends LitePalSupport {
    private int item_id;
    private String item_title;
    private String item_time;
    private String item_name;
    private int item_detail_id;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

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

    public int getItem_detail_id() {
        return item_detail_id;
    }

    public void setItem_detail_id(int item_detail_id) {
        this.item_detail_id = item_detail_id;
    }
}
