package cn.hchaojie.snippets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class AssetHelper {
    public static JSONObject readJson(Context ctx, String file) throws JSONException, IOException {
        return new JSONObject(readText(ctx, file));
    }

    public static String readText(Context ctx, String file) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        String result = null;
        int BUF_SIZE = 2048;
        byte[] bytes = new byte[BUF_SIZE];

        try {
            is = ctx.getAssets().open(file);
            os = new ByteArrayOutputStream();

            int count = -1;
            while ((count = is.read(bytes, 0, BUF_SIZE)) != -1) {
                os.write(bytes, 0, count);
            }

            result = os.toString();
        } catch (IOException e) {
            throw new IOException("Error reading text file: " + file + " message is: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new IOException("Error closing input stream." + e.getMessage());
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new IOException("Error closing output stream." + e.getMessage());
                }
            }
        }

        return result;
    }
}
