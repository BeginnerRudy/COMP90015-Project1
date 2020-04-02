package DictionaryServer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Dictionary {
    private HashMap<String, String> hashmap;


    private static Dictionary dictionary = new Dictionary();

    public void init(String dictionaryFilePath) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(dictionaryFilePath)) {
            this.hashmap = (JSONObject) parser.parse(reader);
        } catch (IOException e) {
            this.hashmap = new HashMap<>();
            DictionaryServer.printServerMsg("Dictionary Loading Failed: " + e.getMessage() + "\n Initialized with empty dictionary.");
        } catch (ParseException e) {
            this.hashmap = new HashMap<>();
            DictionaryServer.printServerMsg("Dictionary Loading Failed: The file is not in valid JSON format." + "\n Initialized with empty dictionary.");
        }

    }


    public static Dictionary getDictionary() {
        return Dictionary.dictionary;
    }

    public HashMap<String, String> getHashmap() {
        return hashmap;
    }

    public void saveToDisk(String dictionaryFilePath) {
        //Write JSON file
        try (FileWriter file = new FileWriter(dictionaryFilePath)){
            file.write(((JSONObject) this.getHashmap()).toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
            DictionaryServer.printServerMsg("Dictionary Saving Failed: I/O Exception." );
        } catch (NullPointerException e) {
            e.printStackTrace();
            DictionaryServer.printServerMsg("Dictionary Saving Failed: The file path is not valid.");

        }
    }

}
