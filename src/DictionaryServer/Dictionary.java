/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This singleton class aims to be acting as a singleton dictionary, which could be used for adding.
 * deleting and searching for a word. Furthermore, this class provides read and save data as
 * JSON file when it is turned on or been shut down.
 */
public class Dictionary {
    private HashMap<String, String> hashmap;


    private static Dictionary dictionary = new Dictionary();

    /**
     * @param dictionaryFilePath The JSON dictionary file to read.
     * This method aims to read a JSON file when the server is just turned on.
     */
    public void init(String dictionaryFilePath) {
        JSONParser parser = new JSONParser();
        // If we could successfully read the file
        try (Reader reader = new FileReader(dictionaryFilePath)) {
            this.hashmap = (JSONObject) parser.parse(reader);
            DictionaryServer.printServerMsg("Dictionary Loading Successfully! \n" + this.hashmap.toString());
        // Error handling
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

    /**
     * @param dictionaryFilePath The filepath to save dictionary.
     * This methods aims to save the dictionary object in the RAM to the disk in JSON format.
     *
     */
    public void saveToDisk(String dictionaryFilePath) {
        //Write JSON file
        try (FileWriter file = new FileWriter(dictionaryFilePath)) {
            file.write(((JSONObject) this.getHashmap()).toJSONString());
            file.flush();
            DictionaryServer.printServerMsg("Dictionary saved to the disk.");
        } catch (IOException e) {
            e.printStackTrace();
            DictionaryServer.printServerMsg("Dictionary Saving Failed: I/O Exception.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            DictionaryServer.printServerMsg("Dictionary Saving Failed: The file path is not valid.");
        } catch (java.lang.ClassCastException e){
            DictionaryServer.printServerMsg("Dictionary Saving Failed: The file path given is not valid");
        }
    }

}
