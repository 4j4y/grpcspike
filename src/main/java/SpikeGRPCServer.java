import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import spikegrpc.GreetingMessage;
import spikegrpc.Name;
import spikegrpc.SpikeGRPCGrpc;
import java.io.IOException;

/**
 * Simple Implementaion of gRPC Server and service(s) to fulfill the request from client
 */
public class SpikeGRPCServer {

    private final int port;
    private final Server server;

    public SpikeGRPCServer(int port) {
        this.port = port;
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(this.port).addService(new SpikeGRPCService());
        this.server = serverBuilder.build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (SpikeGRPCServer.this.server != null) {
                    SpikeGRPCServer.this.server.shutdown();
                    System.out.println("Server has shutdown");
                }
            }
        });
        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpikeGRPCServer spikeGRPCServer = new SpikeGRPCServer(8980);
        System.out.println("Server is Running");
        spikeGRPCServer.start();
    }

    private static class SpikeGRPCService extends SpikeGRPCGrpc.SpikeGRPCImplBase {
        @Override
        public void getHello(Name request, StreamObserver<GreetingMessage> responseObserver) {
            responseObserver.onNext(GreetingMessage.newBuilder().setMessage("Hello "+request.getName()).build());
            responseObserver.onCompleted();
        }
    }

}
