package Day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Part2HashMap {

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
		Map<Integer, Integer> map = new HashMap<>();		// создали пустой map для  записи и счета частоты, сюда будет помещаться ключ - частота, а значение - счетчик 1
		
		int currentFreq = 0;
		while (true) {						// while (true) что бы цикл for крутился бесконечно
			for (int i = 0; i < frequency.size(); i++) {				
				currentFreq += frequency.get(i);		// считаем частоту на каждом шаге
				
				if (map.get(currentFreq) != null) {		// спрашиваем у map есть ли у него такой ключ - currentFreq(текущая частота)
					return currentFreq;					// возвращаем найденную частоту
				}
				
				map.put(currentFreq, 1);				// частота записывается как ключ, значение 1 значит, что встретилась эта частота первый раз
				
			}
		}

	}

}
