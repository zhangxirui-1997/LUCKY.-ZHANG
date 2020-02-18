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
