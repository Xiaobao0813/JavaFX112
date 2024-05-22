package models;

import java.util.TreeMap;
import models.Product;

public class ReadCategoryProduct {
    
     //準備好產品清單  
    public static TreeMap<String, Product> readProduct() {
        //read_product_from_file(); //從檔案或資料庫讀入產品菜單資訊
        //放所有產品  產品編號  產品物件
        TreeMap<String, Product> product_dict = new TreeMap();
        String[][] product_array = {
            {"p-a-101", "排餐", "主廚牛排", "170", "knuckle steak.png", "產品描述"},
            {"p-a-102", "排餐", "丁骨牛排", "290", "T-bone steak.png", "產品描述"},
            {"p-a-103", "排餐", "菲力牛排", "280", "fillet steak.png", "產品描述"},
            {"p-a-104", "排餐", "主廚豬排", "160", "pork chop.png", "產品描述"},
            {"p-a-105", "排餐", "主廚雞排", "160", "chicken chop.png", "產品描述"},
            {"p-a-106", "排餐", "主廚魚排", "170", "fish steak.png", "產品描述"},
            {"p-a-107", "排餐", "鐵板麵", "110", "noodie.png", "產品描述"},
            {"p-s-108", "點心", "薯條", "30", "fries.png", "產品描述"},
            {"p-s-109", "點心", "雞塊", "30", "chicken nuggets.png", "產品描述"},
            {"p-s-110", "點心", "餐包", "20", "bread.png", "產品描述"},
            {"p-d-112", "飲料", "可樂", "20", "cola.png", "產品描述"},
            {"p-d-113", "飲料", "雪碧", "20", "sprite.png", "產品描述"},
            {"p-d-114", "飲料", "檸檬紅茶", "20", "lemon tea.png", "產品描述"}
        };

        //一筆放入字典變數product_dict中
        for (String[] item : product_array) {
            Product product = new Product(
                    item[0], 
                    item[1], 
                    item[2], 
                    Integer.parseInt(item[3]), //價格轉為int
                    item[4], 
                    item[5]);
            //將這一筆放入字典變數product_dict中 
            product_dict.put(product.getProduct_id(), product);
        }
        return product_dict; 
    }
}
