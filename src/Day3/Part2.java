package Day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Part2 {
	public static void main(String[] args) throws IOException {
		String path = ".\\src\\Day3\\data.txt";
		
		final String[] arr = Files.readString(Path.of(path)).lines().toArray(String[]::new);
		
		
		
		
		
		Map<String, Integer> map = new HashMap<>();
		for (String s : arr) {
		    final String regex = "^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$";
		    String str = s.replaceAll(regex, "$1");
		    final var id = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$2");
		    final var x = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$3");
		    final var y = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$4");
		    final var width = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$5");
		    final var height = Integer.parseInt(str);
		    
		    for (int newY = y; newY < height + y; newY++) {
		    	for (int newX = x; newX < width + x; newX++) {
	    			String coord = newY + ";" + newX;
	    			
	    			if (!map.containsKey(coord)) {
	    				map.put(coord, 1);
	    			} else {
	    				map.put(coord, 2);
	    			}	
				}
			} 
		}
		
		int index = 0;
		for (String s : arr) {
		    final String regex = "^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$";
		    String str = s.replaceAll(regex, "$1");
		    final var id = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$2");
		    final var x = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$3");
		    final var y = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$4");
		    final var width = Integer.parseInt(str);
		    str = s.replaceAll(regex, "$5");
		    final var height = Integer.parseInt(str);
		    
		    boolean allIsOk = true;
		    for (int newY = y; newY < height + y; newY++) {
		    	for (int newX = x; newX < width + x; newX++) {
	    			String coord = newY + ";" + newX;

		    		if (map.get(coord) == 2) {
		    			allIsOk = false;
		    		}
				}
			}
		    
		    if (allIsOk) {
		    	index = id;
		    	break;
		    }
		}
		
		System.out.println(index);
		
	}
}
