package Day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Part1 {
	public static void main(String[] args) throws IOException {
		String path = ".\\src\\Day3\\data.txt";
		
		final String[] arr = Files.readString(Path.of(path)).lines().toArray(String[]::new);	
		
		// Files.readString(путь)		для чтения содержимого указанного файла, возвращает String, вызывает исключение IOException
		// есть две формы:
		// readString​(Path path)		Использует кодировку UTF-8, считывает все содержимое из файла в строку, декодируя байты в символы
		// readString​(Path path, Charset cs)		Charset - кодировка. Делает то же самое, используя указанную кодировку
		
		// Path 	интерфейс, для поиска файла в файловой системе
		// Path.of()  	Возвращает Path путем преобразования строки пути или последовательности строк, которые при соединении образуют строку пути
		
		// .lines()		Read all lines from a file as a Stream.
		// String[]::new
		
		Map<String, Integer> map = new HashMap<>();
		for (String s : arr) {
		    final String regex = "^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$";
		    String str = s.replaceAll(regex, "$1");		// знак $ в параметрах replaceAll(regex, "$1") указывает на id группы в регулярном выражении
		    final var id = Integer.parseInt(str);		// final var - превращает изменяемую локальную переменную в неизменяемую
		    str = s.replaceAll(regex, "$2");
		    final var x = Integer.parseInt(str);		// расстояние от левого края
		    str = s.replaceAll(regex, "$3");
		    final var y = Integer.parseInt(str);		// расстояние от верхнего края
		    str = s.replaceAll(regex, "$4");
		    final var width = Integer.parseInt(str);	// ширина
		    str = s.replaceAll(regex, "$5");
		    final var height = Integer.parseInt(str);	// высота
		    
		    for (int newY = y; newY < height + y; newY++) {
		    	for (int newX = x; newX < width + x; newX++) {
	    			String coord = newY + ";" + newX;
	    			
	    			if (map.containsKey(coord)) {
	    				map.put(coord, 1);
	    			} else {
	    				map.put(coord, 2);
	    			}	
				}
			} 
		}
		
		int counter = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 2) {
				counter++;
			}
		}
		
		System.out.println(counter);
	}
}
