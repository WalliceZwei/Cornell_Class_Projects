package coding;

public class Q12 {

    public static void main(String[] args) {
        int[] a = {0, 2};
        int[] b = {0, 4, 0};
        int[] c = {0, 0};
        int[] d = {2, 4};
        int[] e = {3, 0};
        int[][] points = {a, b, c, d, e};
        System.out.println(b == findMaxZeros(points)); // outputs true
    }

    /**
     * Returns the array in points that contains the most number of 0's.
     * In the case of a tie, return the earlier array in points.
     *
     * @param points - an array of int arrays. points must be nonempty,
     *               but the elements of points can be empty. A helper method may be helpful.
     */
    public static int[] findMaxZeros(int[][] points) {
        int arrayIndex = 0;
        int numZeros = 0;
        // First for loop iterates over the indices of the array of arrays
        for(int x = 0; x < points.length; x++){
            int zeroCounter = 0;
            // Second for loop iterates over the array to check how many zeroes are in them, and records them in zeroCounter
            for(int y = 0; y<points[x].length;y++){
                if (points[x][y] == 0){
                    zeroCounter++;
                }
            }
            // If there are more zeroes in one array than the previously largest array
            // then the numZeros in an array is updated for future use
            // and the arrayIndex is updated to be the current array's index in points because it has the more zeroes than anything else for now
            if(zeroCounter > numZeros){
                numZeros = zeroCounter;
                arrayIndex = x;
            }
        }
        return points[arrayIndex];
    }
}
