import java.util.*;

/*
    Problem Description
    Input Format. The first line contains an integer 𝑑. The second line contains an integer 𝑚. The third line
    specifies an integer 𝑛. Finally, the last line contains integers stop1, stop2, . . . , stop𝑛.
    
    Output Format. Assuming that the distance between the cities is 𝑑 miles, a car can travel at most 𝑚 miles
    on a full tank, and there are gas stations at distances stop1, stop2, . . . , stop𝑛 along the way, output the
    minimum number of refills needed. Assume that the car starts with a full tank. If it is not possible to
    reach the destination, output −1.
    
    Constraints. 1 ≤ 𝑑 ≤ 10^5. 1 ≤ 𝑚 ≤ 400. 1 ≤ 𝑛 ≤ 300. 0 < stop1 < stop2 < · · · < stop𝑛 < 𝑑.
*/

// Good job! (Max time used: 0.07/1.50, max memory used: 29134848/2147483648.)
public class CarFueling {
    
    public static void main(String[] args) {
        
        Scanner reader = new Scanner(System.in);
        
        StringTokenizer st = new StringTokenizer(reader.nextLine());
        Integer distance = Integer.valueOf(st.nextToken());

        st = new StringTokenizer(reader.nextLine());
        Integer fuel = Integer.valueOf(st.nextToken());

        st = new StringTokenizer(reader.nextLine());
        Integer numberOfStop = Integer.valueOf(st.nextToken());

        st = new StringTokenizer(reader.nextLine());

        List<Integer> stops = new ArrayList<>();
        for(int i = 0; i < numberOfStop; i++) {
            stops.add(Integer.valueOf(st.nextToken()));
        }

        System.out.println(stopNeeded(distance, fuel, stops));

        reader.close();

    }

    private static Integer stopNeeded(int distance, int fuel, List<Integer> stops) {

        // no need to stop
        if(fuel >= distance)
            return 0;

        stops.add(distance);
        Integer result = 0;

        int lastStop = 0;
        int previousOne = -1;
        int currentOne = -1;
        int distanceFromLastStopToCurrentOne = -1;
        for(int i = 0; i < stops.size(); i++) {
            currentOne = stops.get(i);
            distanceFromLastStopToCurrentOne = currentOne - lastStop;
            
            // storing last available stop
            if(distanceFromLastStopToCurrentOne <= fuel) {
                previousOne = currentOne;
                continue;
            }

            // the previous stop is the max available distance with fuel
            if(distanceFromLastStopToCurrentOne > fuel) {
                // if is not present previous, cannot reach this one
                if(previousOne == -1)
                    return -1;
                
                // take previous stop and loop again this one
                lastStop = previousOne;
                previousOne = -1;
                result++;
                i--;
            }

        }

        return (distance - lastStop) > fuel ? -1 : result;

    }

}
