import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import spikegrpc.GreetingMessage;
import spikegrpc.Name;
import spikegrpc.SpikeGRPCGrpc;
import spikegrpc.SpikeGRPCGrpc.SpikeGRPCBlockingStub;

import java.util.concurrent.TimeUnit;


/**
 * Implementation of gRPC client which does requests to the gRPC server
 */
public class SpikeGRPCClient {

    private ManagedChannel channel;
    private SpikeGRPCBlockingStub blockingStub;

    public SpikeGRPCClient(String hostIP, int port) {
        ManagedChannelBuilder<?> managedChannelBuilder = ManagedChannelBuilder.forAddress(hostIP,port)
                .usePlaintext(true);
        this.channel = managedChannelBuilder.build();
        this.blockingStub = SpikeGRPCGrpc.newBlockingStub(this.channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        SpikeGRPCClient spikeGRPCClient = new SpikeGRPCClient("localhost", 8980);
        spikeGRPCClient.getHello("Ajay");
    }

    private void getHello(String name) {
        Name request = Name.newBuilder().setName(name).build();
        GreetingMessage message = blockingStub.getHello(request);
        System.out.println(message.getMessage());
    }

}
