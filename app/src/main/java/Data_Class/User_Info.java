package Data_Class;

import org.litepal.crud.LitePalSupport;

import java.util.Date;
/*
* 大致构思：登录界面可以进行查询和子线程中增操作
* 注册界面可以进行查询和增操作
* 登出功能进行删除操作
* */

public class User_Info extends LitePalSupport {
    private String User_phonenumber;
    private String User_Fakename;
    private String User_sex="男";
    private String User_Reallyname="尚未实名";
    private int User_age=0;
    private Date User_birthday=null;
    private Date User_UseDay=null;

    public Date getUser_UseDay() {
        return User_UseDay;
    }

    public void setUser_UseDay(Date user_UseDay) {
        User_UseDay = user_UseDay;
    }

    public String getUser_phonenumber() {
        return User_phonenumber;
    }

    public void setUser_phonenumber(String user_phonenumber) {
        User_phonenumber = user_phonenumber;
    }

    public String getUser_Fakename() {
        return User_Fakename;
    }

    public void setUser_Fakename(String user_Fakename) {
        User_Fakename = user_Fakename;
    }

    public String getUser_sex() {
        return User_sex;
    }

    public void setUser_sex(String user_sex) {
        User_sex = user_sex;
    }

    public String getUser_Reallyname() {
        return User_Reallyname;
    }

    public void setUser_Reallyname(String user_Reallyname) {
        User_Reallyname = user_Reallyname;
    }

    public int getUser_age() {
        return User_age;
    }

    public void setUser_age(int user_age) {
        User_age = user_age;
    }

    public Date getUser_birthday() {
        return User_birthday;
    }

    public void setUser_birthday(Date user_birthday) {
        User_birthday = user_birthday;
    }
}
