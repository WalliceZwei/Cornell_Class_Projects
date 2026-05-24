package modules;
import util.HashTable;
import util.LinkedList;

public class SpellCheck implements SpellCheckModule{
    // need to find the correct spellchecker number
    private HashTable<String,String> spellChecker = new HashTable<>(100000);

    public void addWord(String word){
        spellChecker.put(word,null);
    }
    // Use a hash table
    public boolean isValidWord(String word){
        if (spellChecker.containsKey(word)){
            return true;
        }
        return false;
    }
}
