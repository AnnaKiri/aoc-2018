package Day11;

public class Part2 {

    static final int GRID_SERIAL_NUMBER = 7165;

    public static void main(String[] args) {

        int [][] gridOfFuelCells = new int [300][300];

        for (int x = 0; x < gridOfFuelCells.length; x++) {
            for (int y = 0; y < gridOfFuelCells[x].length; y++) {
                int rackId = (x +1) + 10;
                int powerLevel = (rackId * (y +1) + GRID_SERIAL_NUMBER) * rackId;
                int result = (int) (powerLevel / 100) % 10 - 5;

                gridOfFuelCells[x][y] = result;
            }
        }

        int max = Integer.MIN_VALUE;
        int x = 0;
        int y = 0;
        int squareSize = 0;

        for (int size = 2; size < 300; size++) {
            for (int i = 0; i < gridOfFuelCells.length - (size - 1); i++) {
                for (int j = 0; j < gridOfFuelCells[i].length - (size - 1); j++) {

                    int sum = findSum(gridOfFuelCells, i, j, size);

                    if (sum > max) {
                        max = sum;
                        x = i;
                        y = j;
                        squareSize = size;
                    }
                }
            }
        }

        System.out.println((x + 1) + "," + (y + 1) + "," + squareSize);
    }

    public static int findSum(int[][] array, int i, int j, int size){
        int sum = 0;

        for (int k = i; k < i + size; k++){
            for (int l = j; l < j + size; l++) {
                sum += array[k][l];
            }
        }
        return sum;
    }
}
