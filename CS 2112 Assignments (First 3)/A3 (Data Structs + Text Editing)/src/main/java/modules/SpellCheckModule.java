package modules;

public interface SpellCheckModule {

    /**
     * Adds {@code word} to the set of words that can be returned by {@link #isValidWord(String)}.
     */
    public void addWord(String word);

    /**
     * Returns true if {@code word} has been added to the list of known words through a call to
     * {@link #addWord(String)}.
     */
    public boolean isValidWord(String word);
}
