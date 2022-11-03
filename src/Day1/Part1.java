package Day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part1 {

	public static void main(String[] args) {
		String path = ".\\src\\Day1\\data.txt";
		File file = new File(path);
		
		try {
			Scanner scan = new Scanner(file);
			List<Integer> frequency = new ArrayList<>();
			
			while (scan.hasNextInt()) {
				frequency.add(scan.nextInt());
			}
			
			scan.close();
			
			int result = 0;
			for (int i = 0; i < frequency.size(); i++) {				
				result += frequency.get(i);
			}
			
			System.out.println(result);
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}	
	}
}
