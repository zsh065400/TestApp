package practice.other;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        try {
            object.put("1", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object.toString());
    }
}
