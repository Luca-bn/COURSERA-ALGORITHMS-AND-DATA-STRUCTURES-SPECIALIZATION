import java.io.*;
import java.util.*;

/*
    Task. In this task your goal is to implement a simple phone book manager. It should be able to process the
    following types of user’s queries:
    ∙ add number name. It means that the user adds a person with name name and phone number
    number to the phone book. If there exists a user with such number already, then your manager
    has to overwrite the corresponding name.
    ∙ del number. It means that the manager should erase a person with number number from the phone
    book. If there is no such person, then it should just ignore the query.
    ∙ find number. It means that the user looks for a person with phone number number. The manager
    should reply with the appropriate name, or with string “not found" (without quotes) if there is
    no such person in the book.
    
    Input Format. There is a single integer 𝑁 in the first line — the number of queries. It’s followed by 𝑁
    lines, each of them contains one query in the format described above.
    
    Constraints. 1 ≤ 𝑁 ≤ 10^5. All phone numbers consist of decimal digits, they don’t have leading zeros, and
    each of them has no more than 7 digits. All names are non-empty strings of latin letters, and each of
    them has length at most 15. It’s guaranteed that there is no person with name “not found".
    
    Output Format. Print the result of each find query — the name corresponding to the phone number or
    “not found" (without quotes) if there is no person in the phone book with such phone number. Output
    one result per line in the same order as the find queries are given in the input. 
*/

// Good job! (Max time used: 0.70/6.00, max memory used: 464187392/2684354560.)
public class PhoneBook {

    public static void main(String[] args) throws Exception {
        // test();
        run();
    }

    static void run() throws FileNotFoundException {
        FastScanner in = new FastScanner();
        int q = in.nextInt();

        DirectAddressingMap map = new DirectAddressingMap();
        for(int i = 0; i < q; i++) {
            String type = in.next();
            if("add".equals(type)) 
                map.put(in.nextInt(), in.next());
            else if("del".equals(type))
                map.delete(in.nextInt());
            else {
                String name = map.find(in.nextInt());
                System.out.println(name != null ? name : "not found");
            }
        }
    }

    static class DirectAddressingMap {

        String[] directAddresses = new String[(int) Math.pow(10, 8)];

        void put(Integer key, String value) {
            directAddresses[key] = value;
        }

        void delete(Integer key) {
            directAddresses[key] = null;
        }

        String find(Integer key) {
            return directAddresses[key];
        }

    }

    private static void test() {
        String[] queries = new String[] {"add", "del", "find"};
        char[] chars = new char[] {'A', 'B', 'C', 'D', 'E', 'F'};
        Random r = new Random();
        int maxN = (int) Math.pow(10, 5);
        int maxPhoneNumber = (int) Math.pow(10, 8);
        boolean fail = false;
        int testCase = 1;
        while(!fail) {
            System.out.println("Test case " + testCase);
            int N = r.nextInt(maxN - 1) + 1;
            Map<Integer, String> map = new HashMap<>();
            DirectAddressingMap algoMap = new DirectAddressingMap();
            List<Integer> numbers = new ArrayList<>();
            List<String> q = new ArrayList<>();
            for(int i = 0; i < N; i++) {
                String queryType = queries[r.nextInt(3)];
                if("add".equals(queryType)) {
                    Integer phoneNumber = r.nextInt(maxPhoneNumber);
                    numbers.add(phoneNumber);
                    StringBuilder randomString = new StringBuilder();
                    int stringSize = r.nextInt(10 - 1) + 1;
                    for(int j = 0; j < stringSize; j++)
                        randomString.append(chars[r.nextInt(chars.length)]);
                    map.put(phoneNumber, randomString.toString());
                    algoMap.put(phoneNumber, randomString.toString());
                    q.add("add " + phoneNumber + " " + randomString);
                } else if("del".equals(queryType)) {
                    int toFind = r.nextInt((numbers.size() + 1) * 2);
                    Integer phoneNumber = toFind < numbers.size() ? numbers.get(toFind) : r.nextInt(maxPhoneNumber);
                    map.remove(phoneNumber);
                    algoMap.delete(phoneNumber);
                    q.add("del " + phoneNumber);
                } else {
                    int toFind = r.nextInt((numbers.size() + 1) * 2);
                    Integer phoneNumber = toFind < numbers.size() ? numbers.get(toFind) : r.nextInt(maxPhoneNumber);
                    q.add("find " + phoneNumber);
                    if(map.get(phoneNumber) == null && algoMap.find(phoneNumber) == null)
                        continue;
                    if(!map.get(phoneNumber).equals(algoMap.find(phoneNumber))) {
                        fail = true;
                        N = i + 1;
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(".\\test.txt")))) {
                            writer.write(N + "");
                            writer.newLine();
                            for(String query : q) {
                                writer.write(query);
                                writer.newLine();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("Failed test case: " + testCase);
                        System.out.println("pthoneNumber: " + phoneNumber);
                        System.out.println("expected value: " + map.get(phoneNumber));
                        System.out.println("returned value: " + algoMap.find(phoneNumber));
                        return;
                    }
                }
            }
            System.out.println("Ok for test case " + testCase++);
        }
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner() throws FileNotFoundException {
            br = new BufferedReader(new InputStreamReader(System.in));
            // br = new BufferedReader(new FileReader(new File(".\\test.txt")));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
