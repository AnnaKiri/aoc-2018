package Day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Part2HashSet {

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

			System.out.println(fineDoubleFreq(frequency));
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}	

	}
	
	public static int fineDoubleFreq(List<Integer> frequency) {
		Set<Integer> hashSet = new HashSet<>();		// создали пустой set для  записи частоты, сюда будет помещаться частота
		
		int currentFreq = 0;
		while (true) {								// while (true) что бы цикл for крутился бесконечно
			for (int i = 0; i < frequency.size(); i++) {				
				currentFreq += frequency.get(i);		// считаем частоту на каждом шаге
				
				if (hashSet.contains(currentFreq)) {		// спрашиваем у hashSet есть ли у него такоое значение
					return currentFreq;					// возвращаем найденное значение
				}
				
				hashSet.add(currentFreq);				// добавляем в hashSet новую частоту
			}
		}

	}
}
