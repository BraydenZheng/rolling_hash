import javafx.scene.shape.Path;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Random;

public class Main
{
	public static void main(String[] args)
	{

		String s1 = "GAACGCGGCACACTTGATGAAGTCAAGACGAAATTAGACGTGCGGAAGACCGTCAAAATTATCGTGCAGAAGA";
		String s2 = "TATGCCCGCGATAGAACATGAAAATATCATTGACGTGGAATACAAAGGTAGAAAGGCGGTCATAGAAGCAAAATCCGATA";
		String s3 = fileImport("/Users/brayden/DATA/cu/course/7000/hw/problem_set_1/rolling_hash/rolling_hash/src/bacterial_genome_1.txt");
		String s4 = fileImport("/Users/brayden/DATA/cu/course/7000/hw/problem_set_1/rolling_hash/rolling_hash/src/bacterial_genome_2.txt");
		s3 = s3.substring(0,100000);
		s4 = s4.substring(0,100000);
		System.out.println(s3.length());
		System.out.println(s4.length());

		// Time counting start
		long start = System.currentTimeMillis();
//		final int b = 17;
		final int b = new Random().nextInt() * 100 + 10;
		final int p = 31513;
		HashMap<String, Integer> res = commonSubstring(s3, s4, 16, b, p);
		System.out.println(res);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		System.out.println("time take: " + elapsedTime/1000F);
	}

	/**
	 * @Source https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
	 * File import utils: solution from web blog
	 */
	private static  String fileImport(String path) {
		try
		{
			String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
			return content;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static HashMap<String, Integer> commonSubstring(String s1, String s2, int k, int b, int p) {
		HashMap<String, Integer> res = new HashMap<>();
		HashMap<Double, String> hashTable = new HashMap<>();
		String ori_first = s1.substring(0, k);

		// Fill hashTable with subString hash value
		double beginHash = multiPlicativeHash(s1, b, k, 0, p);
		hashTable.put(beginHash, ori_first);

		double previousHash = beginHash;
		for (int i = 1; i < s1.length() - k; i++) {
			String ori = s1.substring(i, i + k);
			double hash = rollingHash(s1, b, k, i, p, previousHash);
			// use for verify
//			double hash = multiPlicativeHash(s1, b, k, i, p);

			hashTable.put(hash, ori);
			previousHash = hash;
		}

		String sec_first = s2.substring(0, k);
		// Search second String from hashTable
		double beginHash_2 = multiPlicativeHash(s2, b, k, 0, p);
		String match = hashTable.get(beginHash_2);
		if (match != null)
		{
			if (res.get(match) == null)
			{
				res.put(match, 0);
			}
			else
			{
				res.put(match, res.get(match) + 1);
			} ;
		}

		previousHash = beginHash_2;
		for (int i = 1; i <= s2.length() - k; i++) {
//			String sec = s2.substring(i, i + k);
			double hash = rollingHash(s2, b, k, i, p, previousHash);
			// use for verify
//			double hash = multiPlicativeHash(s1, b, k, i, p);

			match = hashTable.get(hash);
			if (match != null)
			{
				if (res.get(match) == null)
				{
					res.put(match, 1);
				}
				else
				{
					res.put(match, res.get(match) + 1);
				} ;
			}
			previousHash = hash;
		}
		return res;
	}

	/**
	 * use for first time count hash (without referring other items)
	 * @param x input string
	 * @param b random salt
	 * @param k given range
	 * @param i start index
	 * @param p prime number
	 * @return hash value
	 */
//	private static double multiPlicativeHash(String x, int b, int k, int i, int p) {
//		double res = 0;
//		for (int j = 0; j < k; j++) {
//			//first index item to start with
//			char curr = x.charAt(i + j);
//			res = (res + (int) curr * Math.pow(b, k - i - 1)) % p;
//		}
//		return res;
//	}


	/**
	 * use for first time count hash (without referring other items)
	 * @param x input string
	 * @param b random salt
	 * @param k given range
	 * @param i start index
	 * @param p prime number
	 * @return hash value
	 */
	private static double multiPlicativeHash(String x, int b, int k, int i, int p) {
		double sum = 0;
		for (int j = 0; j < k; j++) {
			//first index item to start with
			char curr = x.charAt(i + j);
			sum = sum + (int) curr * Math.pow(b, k - j - 1);
		}
		return sum % p;
	}

	/**
	 * Rolling hash function for 1 unit at each time
	 * @param x
	 * @param b
	 * @param k
	 * @param i
	 * @param p
	 * @param previousHash
	 * @return
	 */
	private static double rollingHash(String x, int b, int k, int i, int p, double previousHash) {
		double res = previousHash;
		char first = x.charAt(i - 1);
		char insert = x.charAt(i + k - 1);
		res = ((previousHash - (int) first * Math.pow(b, k - 1) + p) % p) * b;
		res = (res % p + insert) % p;
		return res;
	}

private static int stringToInt(String s) { int total = 0; for (int i = 0; i < s.length(); i++) { total += s.charAt(i);
		}
		return total;
	}
}