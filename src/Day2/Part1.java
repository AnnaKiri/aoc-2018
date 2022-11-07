package Day2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Part1 {

	public static void main(String[] args) {
		File file = new File(".\\src\\Day2\\data.txt");
		
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return;
		}
		
		List<String> coordinates = new ArrayList<>();
		while (scan.hasNextLine()) {
			coordinates.add(scan.nextLine());
		}
		
		scan.close();	
		
		System.out.println(letterCount(coordinates));
	}
	
	public static int letterCount(List<String> coordinates) {
		int counter2Letters = 0;
		int counter3Letters = 0;

		for (int i = 0; i < coordinates.size(); i++) {
			String str = coordinates.get(i);
			char [] charArray = str.toCharArray();
			Map<Character, Integer> map = new HashMap<>();
			for (int j = 0; j < charArray.length; j++) {
				if (map.containsKey(charArray[j])) {
					map.put(charArray[j], map.get(charArray[j]) + 1);
				} else {
					map.put(charArray[j], 1);
				}
			}
			
			for (char c : map.keySet()) {			// проходимся по ключам map		Set<Character> hasSet = map.keySet();	for (char c : hasSet) {}
				if (map.get(c) == 2) {
					counter2Letters++;
					break;
				}
			}
			
			for (char c : map.keySet()) {	
				if (map.get(c) == 3) {
					counter3Letters++;
					break;
				}
			}

		}
		
		return counter2Letters * counter3Letters;
	}
}
