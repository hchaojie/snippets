package cn.hchaojie.util;

import org.json.JSONException;
import org.json.JSONObject;

public class LifestreamRequestHelper {
    private static final int ERROR_CODE_REQUEST_TOO_FREQUENTLY = 100070;
    private static final int ERROR_CODE_REQUEST_TOKEN_EXPIRED = 100077;
    
    public static boolean isRequestTooFrequently(Throwable throwable) {
        return getResponseErrorCode(throwable) == ERROR_CODE_REQUEST_TOO_FREQUENTLY;
    }
    
    public static boolean isTokenExpired(Throwable throwable) {
        return getResponseErrorCode(throwable) == ERROR_CODE_REQUEST_TOKEN_EXPIRED;
    }
    
    private static int getResponseErrorCode(Throwable throwable) {
            try {
                // The error response should look like:
                // { "success": "false", "destinations":[ { "id": "5025:172:2", "destinationSet": "5025:172:2", "HTTPCode": "429", "code": "100070" }] }
                //JSONObject object = new JSONObject("{ \"success\": \"false\", \"destinations\":[ { \"id\": \"5025:172:2\", \"destinationSet\": \"5025:172:2\", \"HTTPCode\": \"429\", \"code\": \"100070\" }]}");
                
                String s = "{ \"success\": \"false\", \"destinations\":[ { \"id\": \"5025:125:1\",  \"destinationSet\": \"5025:125:1\", \"HTTPCode\": \"401\", \"code\": \"100077\" }] }";
                JSONObject object = new JSONObject(s);
                
                String code = object.getJSONArray("destinations").getJSONObject(0).optString("code");
                
                return Integer.valueOf(code);
            } catch (JSONException e1) {
                System.out.println("error parse error response, message: ");
//                Ln.w("error parse error response, message: %s", e1.getMessage());
                return -1;
            } catch (NumberFormatException e2) {
                System.out.println("error parse error response, invalid error code(non integer)!");
//                Ln.w("error parse error response, invalid error code(non integer)! %s", e2.getMessage());
                return -1;
            }
        
    }
}
