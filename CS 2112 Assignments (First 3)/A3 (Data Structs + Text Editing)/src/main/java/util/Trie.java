package util;

/** A mutable collection of strings. */
public class Trie {

    private final TrieNode root;
    private int totalNodes = 0;

    /**
     * Create an empty trie.
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Adds the specified string {@code elem} to the trie.
     *
     * @param elem the string to be added
     */
    public void insert(String elem) {
        TrieNode cur = root;
        for (int i = 0; i < elem.length(); i++) {
            char letter = elem.charAt(i);
            if (cur.children.containsKey(letter)) {
                cur = cur.children.get(letter);
            } else {
                TrieNode next = new TrieNode();
                cur.addChild(letter, next);
                cur = next;
            }
        }
        cur.terminal = true;
        cur.terminalNum = totalNodes;
        totalNodes++;
    }

    /**
     * Removes the specified string {@code elem} from the trie if it exists.
     *
     * @param elem the string to be removed
     */
    public void delete(String elem) {
        TrieNode cur = root;
        TrieNode breakpoint = cur;
        if (elem.isEmpty()) {
            return;
        }
        char deleteChar = elem.charAt(0);
        for (int i = 0; i < elem.length(); i++) {
            char letter = elem.charAt(i);
            if (cur.children.containsKey(letter)) {
                if (cur.children.size() > 1 || cur.terminal) {
                    breakpoint = cur;
                    deleteChar = letter;
                }
                cur = cur.children.get(letter);
            } else {
                return;
            }
        }
        if (cur.children.isEmpty()) {
            breakpoint.removeChild(deleteChar);
        } else {
            cur.terminal = false;
            totalNodes--;
        }
    }

    /**
     * Returns {@code true} if the trie contains the specified string {@code elem}.
     *
     * @param elem the string to be searched for
     * @return {@code true} if the string is present, {@code false} otherwise
     */
    public boolean contains(String elem) {
        TrieNode cur = root;
        if (elem.isEmpty()) {
            return false;
        }
        for (int i = 0; i < elem.length(); i++) {
            char letter = elem.charAt(i);
            if (cur.children.containsKey(letter)) {
                cur = cur.children.get(letter);
            } else {
                return false;
            }
        }
        return cur.terminal;
    }

    /**
     * Returns the index of the string {@code elem} if it is found in the trie.
     *
     * @param elem the string to be searched for
     * @return the index if the string is found, or -1 if not
     */
    public int containsIndex(String elem) {
        TrieNode cur = root;
        if (elem.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < elem.length(); i++) {
            char letter = elem.charAt(i);
            if (cur.children.containsKey(letter)) {
                cur = cur.children.get(letter);
            } else {
                return -1;
            }
        }
        return cur.terminalNum;
    }


    /**
     * Return a word contained in the trie of minimal length with {@code prefix}. If no such word
     * exists, return null.
     *
     * @param prefix the prefix to search for
     * @return the shortest word starting with the prefix, or an empty string if no word is found
     */
    public String closestWordToPrefix(String prefix) {
        TrieNode cur = root;
        for (int i = 0; i < prefix.length(); i++) {
            char letter = prefix.charAt(i);
            if (cur.children.containsKey(letter)) {
                cur = cur.children.get(letter);
            } else {
                return "";
            }
        }
        if (cur.terminal) {
            return prefix;
        }
        int length = cur.children.keySet().size();
        TrieNode[] queue = new TrieNode[length * 10];
        String[] completions = new String[length * 10];

        int head = 0;
        int tail = 0;

        Object[] childrenArray = cur.children.keySet().toArray();
        for (Object obj : childrenArray) {
            if (obj != null) {
                Character character = (Character) obj;
                queue[tail] = cur.children.get(character);
                completions[tail++] = String.valueOf(character);
            }
        }

        // breadth first search
        while (head < tail) {
            TrieNode currentNode = queue[head];

            if (tail >= queue.length) {
                TrieNode[] newQueue = new TrieNode[queue.length * 2];
                String[] newCompletions = new String[completions.length * 2];
                System.arraycopy(queue, 0, newQueue, 0, queue.length);
                System.arraycopy(completions, 0, newCompletions, 0, completions.length);
                queue = newQueue;
                completions = newCompletions;
            }

            if (currentNode.terminal) {
                return prefix + completions[head];
            }
            childrenArray = currentNode.children.keySet().toArray();
            for (Object obj : childrenArray) {
                if (obj != null) {
                    Character character = (Character) obj;
                    queue[tail] = currentNode.children.get(character);
                    completions[tail++] = completions[head] + character;
                }
            }
            head++;
        }
        return "";
    }

    private class TrieNode {
        boolean terminal = false;
        int terminalNum = 0;
        HashTable<Character, TrieNode> children = new HashTable<>(10);

        public void addChild(char c, TrieNode node) {
            children.put(c, node);
        }

        public void removeChild(char c) {
            children.remove(c);
        }
    }
}