// Libraries included:
// json simple, guava, apache commons lang3, junit, jmock

import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;

// Class to convert the input files to Strings.
class CRMUtil {
    
    public static String InputText = "John: apple 2\n" +
                                     "Mary: orange 3, peach 2\n" +
                                     "Sarah: peach 1\n" +
                                     "John: peach 1, apple 1\n" +
                                     "Sarah: orange 3\n" +
                                     "Sarah: peach 1, apple 1, orange 1\n" +
                                     "Sarah: orange 1\n" +
                                     "Mary: peach 1\n" +
                                     "John: peach 1";
    
    public static String CatalogText = "apple 1.11\n" +
                                       "orange 2.11\n" +
                                       "peach 3.11";

    // Some splitting functions to help parse the data. Trim helps prevent empty
    // tokens in the array.
    public static List<String> SplitOnSpace(String text) {
        String str[] = text.trim().split(" ");
        List<String> textList = new ArrayList<String>();
        textList = Arrays.asList(str);
        return textList;
    }

    public static List<String> SplitOnComma(String text) {
        String str[] = text.trim().split(",");
        List<String> textList = new ArrayList<String>();
        textList = Arrays.asList(str);
        return textList;
    }

    public static List<String> SplitLines(String text) {
        String str[] = text.trim().split("\n");
        List<String> textList = new ArrayList<String>();
        textList = Arrays.asList(str);
        return textList;
    }

    public static List<String> SplitOnColon(String text) {
        String str[] = text.trim().split(":");
        List<String> textList = new ArrayList<String>();
        textList = Arrays.asList(str);
        return textList;
    }
}

class Main {
    public static void main(String[] args) {
        Fruit ourFruit = new Fruit();
        ourFruit.readFruits();
       
        Orders currentOrders = new Orders();
        currentOrders.readOrders();
        
        Fruit.printFruitsAndPrices(ourFruit);
        System.out.println();
        Orders.printPurchasedFruits(currentOrders);
        System.out.println();
        printAmountSpent(ourFruit, currentOrders);
        
        
    }
    
    public static void printAmountSpent(Fruit fruitList, Orders orderList){
        System.out.println("===== Total Spent ======");
        for(String key:orderList.ourOrders.keySet()){
            String customer = key;
            System.out.println(customer + ":");
            HashMap<String, Integer> sales = orderList.ourOrders.get(customer);
            for(String salesKey:sales.keySet()){
                String fruit = salesKey;
                Integer quantity = sales.get(fruit);
                Double fruitPrice = fruitList.fruitMap.get(fruit);
                Double amountSpent = fruitPrice * quantity;
                if(fruit.endsWith("e")){
                    System.out.printf("Spent $%.2f on %ss%n", amountSpent, fruit);
                } else {
                    System.out.printf("Spent $%.2f on %ses%n", amountSpent, fruit);
                }
            }
            System.out.println();
        }
        System.out.println("========================");
    }
        
}

class Orders {
    public static HashMap<String, HashMap<String, Integer>> ourOrders = new HashMap<String, HashMap<String, Integer>>();
    
    public static void readOrders(){
        List<String> orders = CRMUtil.SplitLines(CRMUtil.InputText);
        for(String order:orders){
            List<String> temp = new ArrayList<String>();
            temp = CRMUtil.SplitOnColon(order);
            String name = temp.get(0);
            if(ourOrders.containsKey(name)){
                HashMap<String, Integer> customerSales = ourOrders.get(name);
            } else {
                ourOrders.putIfAbsent(name, new HashMap<String, Integer>());
            }
            String sale = temp.get(1);
            List<String> purchases = CRMUtil.SplitOnComma(sale);
            for(String purchase:purchases){
                List<String> fruitAndQuantity = new ArrayList<String>();
                fruitAndQuantity = CRMUtil.SplitOnSpace(purchase);
                String fruit = fruitAndQuantity.get(0);
                int quantity = Integer.parseInt(fruitAndQuantity.get(1));
                HashMap<String, Integer> ourSales = ourOrders.get(name);
                
                if(ourSales.containsKey(fruit)){
                    int currentQuantity = ourSales.get(fruit);
                    ourSales.put(fruit, currentQuantity + quantity);
                } else {
                    ourSales.putIfAbsent(fruit, quantity);
                }
                   
            }
        
        }
    }
    
    public static void printPurchasedFruits(Orders allOrders){
        System.out.println("==== Current Orders ====");
        for(String key:allOrders.ourOrders.keySet()){
            String customer = key;
            System.out.println(customer + ":");
            HashMap<String, Integer> sales = allOrders.ourOrders.get(customer);
            for(String salesKey:sales.keySet()){
                String fruit = salesKey;
                Integer quantity = sales.get(fruit);
                if(quantity > 1 && fruit.endsWith("e")){
                    System.out.println("Purchased " + quantity + " " + fruit + "s");
                } else if(quantity > 1) {
                    System.out.println("Purchased " + quantity + " " + fruit + "es");
                } else {
                    System.out.println("Purchased " + quantity + " " + fruit);
                }
            }
            System.out.println();
        }
        System.out.println("========================");
    }
    
}

class Fruit {
    public static Map<String, Double> fruitMap = new HashMap<String, Double>();
    
    public static void readFruits(){
        List<String> fruitsAndPrices = CRMUtil.SplitLines(CRMUtil.CatalogText);
        for(String fruitAndPrice : fruitsAndPrices){
            List<String> temp = new ArrayList<String>();
            temp = CRMUtil.SplitOnSpace(fruitAndPrice);
            addFruit(temp.get(0), Double.parseDouble(temp.get(1)));
        }
    }
   
    public static void addFruit(String fruit, Double price){
        fruitMap.putIfAbsent(fruit, price);
    }
    
    public static void printFruitsAndPrices(Fruit aFruit){
        System.out.println("=== Our Fruit Prices ===");
        for(String key:aFruit.fruitMap.keySet()){
            String fruit = key;
            Double price = aFruit.fruitMap.get(fruit);
            System.out.println(fruit +": " + price);
        }
        System.out.println("========================");
    }
    
}





