package Data_Class;

import org.litepal.crud.LitePalSupport;
/*
* 大致构思：登录界面可以进行查询和子线程中增操作
* 注册界面可以进行查询和增操作
* 登出功能进行删除操作
* */

public class User_Info extends LitePalSupport {
    private String User_phonenumber;
    private String User_fakename;
    private String User_sex="男";
    private String User_reallyname="尚未实名";
    private int User_age=0;
    private String User_birthday=null;
    private String  User_useDay =null;
    private String User_five_id_time=null;
    private String User_five_fen;
    private String User_five_ci;
    private String User_five_zheng;
    private String User_five_yi;
    private String User_five_yu;
    private String User_parcent="0";

    public String getUser_five_id_time() {
        return User_five_id_time;
    }

    public void setUser_five_id_time(String user_five_id_time) {
        User_five_id_time = user_five_id_time;
    }

    public String getUser_parcent() {
        return User_parcent;
    }

    public void setUser_parcent(String user_parcent) {
        User_parcent = user_parcent;
    }

    public String getUser_reallyname() {
        return User_reallyname;
    }

    public void setUser_reallyname(String user_reallyname) {
        User_reallyname = user_reallyname;
    }

    public String getUser_five_fen() {
        return User_five_fen;
    }

    public void setUser_five_fen(String five_fen) {
        this.User_five_fen = five_fen;
    }

    public String getUser_five_ci() {
        return User_five_ci;
    }

    public void setUser_five_ci(String user_five_ci) {
        this.User_five_ci = user_five_ci;
    }

    public String getUser_five_zheng() {
        return User_five_zheng;
    }

    public void setUser_five_zheng(String user_five_zheng) {
        this.User_five_zheng = user_five_zheng;
    }

    public String getUser_five_yi() {
        return User_five_yi;
    }

    public void setUser_five_yi(String user_five_yi) {
        this.User_five_yi = user_five_yi;
    }

    public String getUser_five_yu() {
        return User_five_yu;
    }

    public void setUser_five_yu(String user_five_yu) {
        this.User_five_yu = user_five_yu;
    }

    public String getUser_useDay() {
        return User_useDay;
    }

    public void setUser_useDay(String user_useDay) {
        User_useDay = user_useDay;
    }

    public String getUser_phonenumber() {
        return User_phonenumber;
    }

    public void setUser_phonenumber(String user_phonenumber) {
        User_phonenumber = user_phonenumber;
    }

    public String getUser_fakename() {
        return User_fakename;
    }

    public void setUser_fakename(String user_fakename) {
        User_fakename = user_fakename;
    }

    public String getUser_sex() {
        return User_sex;
    }

    public void setUser_sex(String user_sex) {
        User_sex = user_sex;
    }

    public String getUser_Reallyname() {
        return User_reallyname;
    }

    public void setUser_Reallyname(String user_Reallyname) {
        User_reallyname = user_Reallyname;
    }

    public int getUser_age() {
        return User_age;
    }

    public void setUser_age(int user_age) {
        User_age = user_age;
    }

    public String getUser_birthday() {
        return User_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        User_birthday = user_birthday;
    }
}
