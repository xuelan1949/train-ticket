package food.init;

import food.entity.Food;
import food.entity.FoodStore;
import food.entity.TrainFood;
import food.service.FoodMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class InitData implements CommandLineRunner{
	
	private static final Logger log = Logger.getLogger("test");

    @Autowired
    FoodMapService service;

    String foodStoresPath = "/foodstores.txt";
    String trainFoodPath = "/trainfood.txt";

    @Override
    public void run(String... args) throws Exception {

        BufferedReader br1 = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(foodStoresPath)));
        try{
            String line = br1.readLine();
            while( line != null ){
                if( !line.trim().equals("") ){
                    FoodStore fs = new FoodStore();
                    fs.setId(UUID.randomUUID());
                    String[] lineTemp = line.trim().split("=");
                    fs.setStationId(lineTemp[1]);

                    lineTemp = br1.readLine().trim().split("=");
                    fs.setStoreName(lineTemp[1]);

                    lineTemp = br1.readLine().trim().split("=");
                    fs.setTelephone(lineTemp[1]);

                    lineTemp = br1.readLine().trim().split("=");
                    fs.setBusinessTime(lineTemp[1]);

                    lineTemp = br1.readLine().trim().split("=");
                    fs.setDeliveryFee( Double.parseDouble(lineTemp[1]) );

                    lineTemp = br1.readLine().trim().split("=");

                    fs.setFoodList(toFoodList(lineTemp[1]));
                    service.createFoodStore(fs,null);
                }
                line = br1.readLine();
            }

        } catch(Exception e){
			log.info("the foodstores.txt has format error!");
			log.info(e.getMessage());
            System.exit(1);
        }


        BufferedReader br2 = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(trainFoodPath)));
        try{
            String line2 = br2.readLine();
            while( line2 != null ){
                if( !line2.trim().equals("") ){
                    TrainFood tf = new TrainFood();
                    tf.setId(UUID.randomUUID());
                    String[] lineTemp = line2.trim().split("=");
                    tf.setTripId(lineTemp[1]);
                    lineTemp = br2.readLine().trim().split("=");
                    tf.setFoodList(toFoodList(lineTemp[1]));
                    service.createTrainFood(tf,null);
                }
                line2 = br2.readLine();
            }

        } catch(Exception e){
			log.info("the trainfood.txt has format error!");
			log.info(e.getMessage());
            System.exit(1);
        }
    }

    private List<Food> toFoodList(String s){
		log.info("s=" + s);
        String[] foodstring = s.split("_");
        List<Food> foodList = new ArrayList<>();
        for(int i = 0; i< foodstring.length; i++){
            String[] foodTemp = foodstring[i].split(",");
            Food food = new Food();
            food.setFoodName(foodTemp[0]);

            food.setPrice(Double.parseDouble(foodTemp[1]));

            foodList.add(food);
        }
        return foodList;
    }
}
