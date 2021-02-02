package com.crypto.croytowallet.Model;

public class Model_Class_Add_Currency {
    String Currency_Title;
    String Title_Des;
    String Image;

    public Model_Class_Add_Currency(String currency_Title, String title_Des, String image) {
        Currency_Title = currency_Title;
        Title_Des = title_Des;
        Image = image;
    }

    public Model_Class_Add_Currency() {

    }

    public String getTitle_Des() {
        return Title_Des;
    }

    public void setTitle_Des(String title_Des) {
        Title_Des = title_Des;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCurrency_Title() {
        return Currency_Title;
    }

    public void setCurrency_Title(String currency_Title) {
        Currency_Title = currency_Title;
    }
}
