import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/*
    Task. You are given a series of incoming network packets, and your task is to simulate their processing.
    Packets arrive in some order. For each packet number π, you know the time when it arrived π΄π and the
    time it takes the processor to process it ππ (both in milliseconds). There is only one processor, and it
    processes the incoming packets in the order of their arrival. If the processor started to process some
    packet, it doesnβt interrupt or stop until it finishes the processing of this packet, and the processing of
    packet π takes exactly ππ milliseconds.
    The computer processing the packets has a network buffer of fixed size π. When packets arrive,
    they are stored in the buffer before being processed. However, if the buffer is full when a packet
    arrives (there are π packets which have arrived before this packet, and the computer hasnβt finished
    processing any of them), it is dropped and wonβt be processed at all. If several packets arrive at the
    same time, they are first all stored in the buffer (some of them may be dropped because of that β
    those which are described later in the input). The computer processes the packets in the order of
    their arrival, and it starts processing the next available packet from the buffer as soon as it finishes
    processing the previous one. If at some point the computer is not busy, and there are no packets in
    the buffer, the computer just waits for the next packet to arrive. Note that a packet leaves the buffer
    and frees the space in the buffer as soon as the computer finishes processing it.    

    Input Format. The first line of the input contains the size π of the buffer and the number π of incoming
    network packets. Each of the next π lines contains two numbers. π-th line contains the time of arrival
    π΄π and the processing time ππ (both in milliseconds) of the π-th packet. It is guaranteed that the
    sequence of arrival times is non-decreasing (however, it can contain the exact same times of arrival in
    milliseconds β in this case the packet which is earlier in the input is considered to have arrived earlier).

    Constraints. All the numbers in the input are integers. 1 β€ π β€ 10^5; 0 β€ π β€ 10^5; 0 β€ π΄π β€ 10^6;
    0 β€ ππ β€ 10^3; π΄π β€ π΄π+1 for 1 β€ π β€ π β 1.

    Output Format. For each packet output either the moment of time (in milliseconds) when the processor
    began processing it or β1 if the packet was dropped (output the answers for the packets in the same
    order as the packets are given in the input).
*/

// Good job! (Max time used: 0.57/6.00, max memory used: 76533760/2147483648.)
class Request {
    public Request(int arrival_time, int process_time) {
        this.arrival_time = arrival_time;
        this.process_time = process_time;
    }

    public int arrival_time;
    public int process_time;
}

class Response {
    public Response(boolean dropped, int start_time) {
        this.dropped = dropped;
        this.start_time = start_time;
    }

    public boolean dropped;
    public int start_time;
}

class Buffer {
    public Buffer(int size) {
        this.size_ = size;
        this.queue = new LinkedList<>();

    }

    public Response Process(Request request) {

        
        // if the queue of requests is empty, start to precess at this time
        if (queue.isEmpty() && size_ > 0) {
            endTime = Math.max(endTime, request.arrival_time) + request.process_time;
            queue.add(request);
            return new Response(false, request.arrival_time);
        }

        Request toProcess = null;
        while(elaborationTime <= request.arrival_time && !queue.isEmpty()) {
            toProcess = queue.peek();
            elaborationTime = Math.max(elaborationTime, toProcess.arrival_time);
            if(elaborationTime + toProcess.process_time <= request.arrival_time) {
                elaborationTime += toProcess.process_time;
                queue.poll();
            } else break;
        }
        
        if(queue.size() >= size_)
            return new Response(true, -1);
        
        endTime = Math.max(endTime, request.arrival_time) + request.process_time;
        queue.add(request);

        return new Response(false, endTime - request.process_time);
    }

    private int endTime = 0;
    private int elaborationTime = 0;
    private Queue<Request> queue;
    private int size_;
}

class process_packages {
    private static ArrayList<Request> ReadQueries(FastScanner scanner) throws IOException {
        int requests_count = scanner.nextInt();
        ArrayList<Request> requests = new ArrayList<Request>();
        for (int i = 0; i < requests_count; ++i) {
            int arrival_time = scanner.nextInt();
            int process_time = scanner.nextInt();
            requests.add(new Request(arrival_time, process_time));
        }
        return requests;
    }

    private static ArrayList<Response> ProcessRequests(ArrayList<Request> requests, Buffer buffer) {
        ArrayList<Response> responses = new ArrayList<Response>();
        for (int i = 0; i < requests.size(); ++i) {
            responses.add(buffer.Process(requests.get(i)));
        }
        return responses;
    }

    private static void PrintResponses(ArrayList<Response> responses) {
        for (int i = 0; i < responses.size(); ++i) {
            Response response = responses.get(i);
            if (response.dropped) {
                System.out.println(-1);
            } else {
                System.out.println(response.start_time);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();

        int buffer_max_size = scanner.nextInt();
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        if (requests == null || requests.isEmpty())
            return;
        ArrayList<Response> responses = ProcessRequests(requests, buffer);
        PrintResponses(responses);
    }

    static class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
