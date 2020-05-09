package Data_Class;

import org.litepal.crud.LitePalSupport;

public class Ten_Items extends LitePalSupport {
    private String title;
    private String data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
