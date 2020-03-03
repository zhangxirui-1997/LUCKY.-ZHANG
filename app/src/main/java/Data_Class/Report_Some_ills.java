package Data_Class;

import android.graphics.Bitmap;

import org.litepal.crud.LitePalSupport;

public class Report_Some_ills {
    private String ills_name;//什么病
    private Float ills_statue;//什么状态
    private String ills_tips;//一句话牵动你的心
    private Bitmap bitmap;//截图
    private int ill_max;//那个滑动条的最大值

    public int getIll_max() {
        return ill_max;
    }

    public void setIll_max(int ill_max) {
        this.ill_max = ill_max;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getIlls_name() {
        return ills_name;
    }

    public void setIlls_name(String ills_name) {
        this.ills_name = ills_name;
    }

    public Float getIlls_statue() {
        return ills_statue;
    }

    public void setIlls_statue(Float ills_statue) {
        this.ills_statue = ills_statue;
    }

    public String getIlls_tips() {
        return ills_tips;
    }

    public void setIlls_tips(String ills_tips) {
        this.ills_tips = ills_tips;
    }
}
