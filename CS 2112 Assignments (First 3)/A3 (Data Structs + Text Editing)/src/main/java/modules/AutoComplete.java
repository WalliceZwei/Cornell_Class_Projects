package modules;
import util.Trie;

public class AutoComplete implements AutoCompleteModule{

    Trie WordTree = new Trie();
    public void addWord(String word){
        WordTree.insert(word);
    }

    public String getWordForPrefix(String prefix){
        return WordTree.closestWordToPrefix(prefix);
    }

}
