package test;

import static org.junit.jupiter.api.Assertions.*;
import util.LinkedList;
import util.HashTable;
import util.Trie;

import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Test {

	@org.junit.jupiter.api.Test
	void hashtablesizeisemptytests(){
		HashTable<String,String> hashtab = new HashTable<>(47);
		assertEquals(hashtab.size(),0);
		assertTrue(hashtab.isEmpty());
	}

	@org.junit.jupiter.api.Test
	void hashTableRemoveTester(){
		HashTable<String,String> hashtable = new HashTable<>(10);
		hashtable.put("0","bin");
		hashtable.put("1","bin");
		hashtable.put("10","bineds");
		hashtable.put("11","bin");
		hashtable.put("100","bin");
		hashtable.put("101","bine");
		hashtable.put("110","bined");
		hashtable.remove("10");
		assertFalse(hashtable.containsKey("10"));
		assertFalse(hashtable.containsValue("bineds"));
		assertFalse(hashtable.keySet().contains("10"));
		assertEquals(hashtable.size(), 6);

		System.out.println(Arrays.toString(hashtable.keySet().toArray()));
		//assertTrue(Arrays.equals(hashtable.keySet().toArray(), new Object[] {"0", "1", "110", "11", "100", "101"}));

	}

	void otherFeatures(){


	}

	@org.junit.jupiter.api.Test

	void hashTableNullity(){

		HashTable<String,String> hashtable = new HashTable<>(10);
		hashtable.remove("hull");
		hashtable.get("hi");
		hashtable.containsValue("yes");
		assertEquals(hashtable.size(), 0);

	}

	@org.junit.jupiter.api.Test

	void hashTableNonNullintoNullity(){

		HashTable<String,String> hashtable = new HashTable<>(10);
		hashtable.put("0","bin");
		hashtable.put("10","bin");
		hashtable.remove("0");
		hashtable.remove("10");
		hashtable.get("0");
		assertEquals(hashtable.size(), 0);


	}

	@org.junit.jupiter.api.Test

	void hashTableResizing(){

		HashTable<String,String> hashtable = new HashTable<>(3);
		hashtable.put("0","bin");
		hashtable.put("1","bin");
		hashtable.put("10","bineds");
		hashtable.put("11","bin");
		hashtable.put("100","bin");
		hashtable.put("101","bine");
		hashtable.put("110","bined");

		hashtable.remove("10");
		assertFalse(hashtable.containsKey("10"));
		assertFalse(hashtable.containsValue("bineds"));
		System.out.println(hashtable.get("10"));
		assertEquals(hashtable.get("10"),null);
		assertTrue(hashtable.containsValue("bined"));
		assertFalse(hashtable.keySet().contains("10"));
		assertEquals(hashtable.size(), 6);
		Object[] a = hashtable.keySet().toArray();
		Object[] b = {"0", "1", "110", "11", "100", "101"};
		Arrays.sort(a);
		Arrays.sort(b);
		assertTrue(Arrays.equals(a,b));
	}

	@org.junit.jupiter.api.Test

	void LargeVolumehashTable(){
		int number = (1000000);

		List<String> testCases = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < 3*number; i++) {
			StringBuilder randomString = new StringBuilder();
			for (int j = 0; j < 3; j++) {
				randomString.append((char) ('a' + random.nextInt(26)));
			}
			testCases.add(randomString.toString());
		}
		HashTable<String,String> hashtab = new HashTable<>(number);
		for(int x = 0; x < 3*number; x++){
			hashtab.put(testCases.get(x),"h");
		}

		for(int x = 0; x < 3*number; x++){
			if (!hashtab.containsKey(testCases.get(x))){
				assertFalse(true,"One or more keys not present");
			}
		}
		assertTrue(true);
	}

	@org.junit.jupiter.api.Test

	void hashTablerewriting(){
		HashTable<String,String> hashtab = new HashTable<>(47);
		hashtab.put("key1","hey");
		hashtab.put("key1","dub");
		assertEquals(hashtab.get("key1"),"dub");
		assertFalse(hashtab.containsValue("hey"));
		assertEquals(hashtab.size(),1);
	}


	@org.junit.jupiter.api.Test

	void hashTableRandomType(){
		HashTable<Integer,Boolean> hashtable = new HashTable<>(3);
		hashtable.put(0,true);
		hashtable.put(1,true);
		hashtable.put(10,false);
		hashtable.put(11,true);
		hashtable.put(100,true);
		hashtable.put(101,true);
		hashtable.put(110,true);

		hashtable.remove(10);
		assertFalse(hashtable.containsKey(10));
		assertFalse(hashtable.containsValue(false));
		assertEquals(hashtable.get(10),null);
		assertTrue(hashtable.containsValue(true));
		assertFalse(hashtable.keySet().contains(10));
		assertEquals(hashtable.size(), 6);

	}
	@org.junit.jupiter.api.Test
	void nicheHashTableOperations(){
		HashTable<Integer,Boolean> hashtable = new HashTable<>(3);
		hashtable.put(0,true);
		hashtable.put(1,true);
		hashtable.put(10,false);
		hashtable.put(11,true);
		hashtable.put(100,true);
		hashtable.put(101,true);
		hashtable.put(110,true);

		HashTable<Integer,Boolean> hashtabledos = new HashTable<>(3);
		hashtabledos.putAll(hashtable);
		hashtable.clear();
	}

	@org.junit.jupiter.api.Test
	void LinkedListTest1(){
		LinkedList<Integer,String> list = new LinkedList<>();
		list.prepend(1,"Now");
		list.prepend(2,"Hey");
		list.append(3,"!");
		list.append(4,"!");
		assertTrue(list.contains("Hey"));
		assertFalse(list.contains("k"));
		assertTrue(list.contains("Now"));
		assertTrue(list.contains("!"));
		list.remove(54);
		assertEquals(list.size(),4);
		list.remove(1);
		assertEquals(list.size(),3);
		list.remove(2);
		assertEquals(list.size(),2);
		list.remove(3);
		assertEquals(list.size(),1);
		list.remove(4);
		assertEquals(list.size(),0);
	}

	@org.junit.jupiter.api.Test
	void TrieTest1(){
		Trie trie = new Trie();

		trie.insert("hello");
		trie.insert("jellyboyfox");
		trie.insert("hermosando");
		trie.insert("hermano");
		trie.insert("her");
		trie.insert("hey");
		assertTrue(trie.contains("hey"));
		assertTrue(trie.contains("jellyboyfox"));
		trie.delete("hey");
		assertFalse(trie.contains("hey"));
		assertEquals("hermano",trie.closestWordToPrefix("herm"));
	}



}