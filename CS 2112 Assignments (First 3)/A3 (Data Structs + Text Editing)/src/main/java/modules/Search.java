package modules;
import util.Trie;
public class Search implements SearchModule {

    public int find(String query, String text){
        Trie wordTree= new Trie();
        for(int x=0; x < text.length()-query.length()+1; x++) {
            wordTree.insert(text.substring(x,x+query.length()));
        }
        if(wordTree.contains(query)){
            return wordTree.containsIndex(query);
        }
        else{
            return -1;
        }
    }
}
