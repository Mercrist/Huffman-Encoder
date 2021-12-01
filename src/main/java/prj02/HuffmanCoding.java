package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;

import HashTable.*;
import List.*;
import SortedList.*;
import Tree.*;


/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author Yariel Mercado &lt;841-19-1434&gt; (Implementation)
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("input5.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/*We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it*/
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/*If input file is empty just return an empty string, if not just extract the data*/
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return line;
	}

	/**
	 * Computes the frequency distribution of the input text and stores the result in a map.
	 * The keys are strings representing each distinct character in the input text and the values are
	 * integers denoting the amount of times that character appears in the input text. The map is implemented with
	 * a hash table: {@code HashTableSC}.
	 *
	 * @param inputString The input string received from the given text file.
	 * @return Map with each unique character as a key and its number of occurrences as the value.
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		HashTableSC<String, Integer> map = new HashTableSC<>(new SimpleHashFunction<>());

		for(int i = 0; i < inputString.length(); i++){
			/* Making characters a String makes it easier to combine values in the huffman_tree method. */
			String character = String.valueOf(inputString.charAt(i));
			if(!map.containsKey(character)){
				map.put(character, 1); //first occurrence of that character
			}

			else{
				map.put(character, map.get(character) + 1); //update frequency of that character
			}
		}
		return map;
	}

	/**
	 * Given the frequency distribution map of the input text, creates a Huffman tree. The tree is built
     * from the leaves of the tree to its root by repeatedly combining the nodes with the lowest frequencies
     * into a single root node. The root node's key is the combined values of the subtrees' frequencies, and its value, the
	 * combined characters. Similarly, the nodes' are key-value pairs with the characters' frequency as the keys and their
	 * corresponding character as the values. A {@code SortedList} automatically sorts the nodes.
	 *
	 * @param fD A frequency distribution map from the input text.
	 * @return The root node of the completed Huffman tree.
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {
		SortedLinkedList<BTNode<Integer, String>> sll = orderFrequencies(fD);

		while(sll.size() > 1){ //the last element is the root node
			/* Since the Sorted List is already ordered, the two lowest values have the lowest frequencies. */
			BTNode<Integer, String> node1 = sll.removeIndex(0);
			BTNode<Integer, String> node2 = sll.removeIndex(0);
			BTNode<Integer, String> rootNode = new BTNode<>(node1.getKey() + node2.getKey(),
					node1.getValue() + node2.getValue()); //combine these into a root node

			/* Creates the tree. SLL uses BTNode's comparator which accounts for the frequency tie-breaker. */
			rootNode.setLeftChild(node1);
			rootNode.setRightChild(node2);
			/* Children should have the same parent, connects tree. */
			node1.setParent(rootNode);
			node2.setParent(rootNode);
			/* Update SLL by adding the root node. */
			sll.add(rootNode);
		}

		return sll.removeIndex(0); //stores less memory by removing the root node
	}

	/**
	 * Given the root of the Huffman tree, traverses it to obtain the corresponding Huffman code of each character. The
	 * Huffman codes are stored in a map with keys representing each character. Each characters' value are mapped to their
	 * corresponding Huffman codes, of type string. Utilizes a helper function to traverse the
	 * Huffman tree.
	 *
	 * @param huffmanRoot The root of the constructed Huffman tree.
	 * @return A map containing each unique character and their corresponding Huffman code.
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		return traversal_helper(huffmanRoot, new HashTableSC<>(new SimpleHashFunction<>()), "");
	}

    /**
     * Given the encoding map, generates the Huffman encoded string of the given input string. For every character in
	 * the input string, retrieves their corresponding code from the encoding map and builds up the encoded result.
     *
	 * @param encodingMap A map containing key-value pairs of each character and their respective Huffman codes.
     * @param inputString The input string received from the given text file.
     * @return The Huffman encoded string of the {@code inputString} parameter.
     */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		StringBuilder output = new StringBuilder(); //SB is faster

		for(int i = 0; i < inputString.length(); i++){
			String character = String.valueOf(inputString.charAt(i));
			output.append(encodingMap.get(character)); //get the code of each character and append it to the result
		}
		return output.toString(); //encoded binary string
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with its frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		sortedList = orderFrequencies(fD);


		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t\t" + node.getKey() + " \t\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space required is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/

	/**
	 * Utilizes a {@code SortedList} to order the entries in a frequency distribution map by their frequencies,
	 * in ascending order. If two entries have the same frequency, compares the character's values instead as a tie-breaker.
	 *
	 * @param fD A given frequency distribution map of the input text.
	 * @return A {@code SortedList} with each entry sorted in ascending order, by frequency.
	 */
	public static SortedLinkedList<BTNode<Integer, String>> orderFrequencies(Map<String, Integer> fD) {
		SortedLinkedList<BTNode<Integer, String>> sll = new SortedLinkedList<BTNode<Integer, String>>();

		/* Places frequencies in SLL by <Integer frequency, String character> */
		for(String key : fD.getKeys()){
			sll.add(new BTNode<>(fD.get(key), key));
		}

		return sll;
	}

	/**
	 * Helper function which, given the root of a Huffman tree, traverses it and obtains the corresponding
	 * Huffman code of each character. Each character is located at the leaves of the Huffman tree. The code for each
	 * character is generated by adding a "0" or a "1" at the end of the string depending on whether the left or right
	 * subtrees are recursively searched, respectively. Upon reaching a leaf node, the accumulated code thus far
	 * is placed into a map.
	 *
	 * @param huffmanRoot The root of the Huffman tree to be traversed.
	 * @param map Places each character, the current leaf node's value, as the keys and their corresponding
	 *            Huffman codes as the value.
	 * @param code The Huffman code of a character, as a string.
	 * @return The final, updated map with each distinct character from the input text and their Huffman codes.
	 */
	public static Map<String, String> traversal_helper(BTNode<Integer,String> huffmanRoot, Map<String, String> map, String code) {
		if(huffmanRoot == null) return map;
		if(huffmanRoot.getLeftChild() == null && huffmanRoot.getRightChild() == null){ //leaf nodes are the characters
			map.put(huffmanRoot.getValue(), code);
		}

		/* Code is accumulated by adding a '0' or a '1', upon branching to the left and right subtrees. */
		traversal_helper(huffmanRoot.getLeftChild(), map, code + "0");
		traversal_helper(huffmanRoot.getRightChild(), map, code + "1");
		return map;
	}

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm.
	 *
	 * Used for output purposes.
	 *
	 * @param output - Encoded String
	 * @param lookupTable A map with the codes of each character from the input text as keys and the characters themselves
	 *                    as the values. Used to decode a Huffman encoded string.
	 * @return The decoded String. This should be the original input string parsed from the input file.
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { //Found it
				result += symbols.get(index);
				start = i;
			}
		}
		return result;
	}


}
