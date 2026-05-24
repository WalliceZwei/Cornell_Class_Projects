package modules;

public interface AutoCompleteModule {

    /**
     * Adds {@code word} to the set of words that can be returned by {@link
     * #getWordForPrefix(String)}.
     */
    public void addWord(String word);

    /**
     * Returns a word of minimal length that has {@code prefix} as a (not necessarily proper)
     * prefix.
     */
    public String getWordForPrefix(String prefix);
}
