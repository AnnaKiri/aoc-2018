package Day2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part2 {

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
		
		findDifferences(coordinates);
	}
	
	public static void findDifferences(List<String> coordinates) {
		
		
		for (int i = 0; i < coordinates.size(); i++) {
			String str1 = coordinates.get(i);
			for (int j = i + 1; j < coordinates.size(); j++) {
				String str2 = coordinates.get(j);
				char[] oneLine = str1.toCharArray();
				char[] anotherLine = str2.toCharArray();
				int errorCounter = 0;
				for (int k = 0; k < oneLine.length; k++) {
					if (oneLine[k] != anotherLine[k]) {
						errorCounter++;
					}
					if (errorCounter > 1) {
						break;
					}
				}
				if (errorCounter == 1) {
					System.out.println(oneLine);
					System.out.println(anotherLine);
					return;
				} 
			}
		}
	}
}
