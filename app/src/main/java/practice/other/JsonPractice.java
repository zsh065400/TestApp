package practice.other;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class JsonPractice {

    public JsonPractice() {
    }

    public static void buildJson() {
        JSONObject object1 = new JSONObject();
        try {

            JSONObject o2 = new JSONObject();
            JSONArray array = new JSONArray();
            array.put(1.21);
            array.put(1.22);
            o2.put("2", array);


            JSONObject o3 = new JSONObject();
            o3.put("1.3.1", 131);

            o2.put("3", o3);

            object1.put("1", o2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object1.toString());


        try {
            final JSONObject jsonObject1 = object1.getJSONObject("1");
            final JSONArray jsonArray = jsonObject1.getJSONArray("2");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(jsonArray.get(i));
            }
            final JSONObject jsonObject3 = jsonObject1.getJSONObject("3");
            final int json = jsonObject3.getInt("1.3.1");
            System.out.println(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
