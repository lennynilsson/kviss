package se.bylenny.kviss;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;


public class Assets {

    /**
     * Read a data file from assets
     *
     * @param <T>      The type
     * @param fileName The file name
     * @param type     The data type
     * @param context  a context
     * @return The data object
     * @throws IOException Unable to load asset
     */
    public static <T> T read(String fileName, Type type, Context context) {
        InputStream inputStream = null;
        T output = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            output = new GsonBuilder().create().fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Don't care
                }
            }
        }
        return output;
    }
}
